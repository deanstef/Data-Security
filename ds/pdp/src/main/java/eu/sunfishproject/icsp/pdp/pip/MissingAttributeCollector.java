package eu.sunfishproject.icsp.pdp.pip;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.pip.PIPRequest;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.rest.mappers.XACMLRequestProvider;

public abstract class MissingAttributeCollector {

  private static final Logger                       LOG                          = LogManager
      .getLogger(MissingAttributeCollector.class);

  private static final Map<String, Set<PIPRequest>> MISSING_ATTRS                = new ConcurrentHashMap<>();

  private static final Collection<String>           CURRENTLY_PROCESSED_REQUESTS = new HashSet<>();

  public static boolean startCollecting(Request req) throws IOException {
    String request = XACMLRequestProvider.stringify(req);
    synchronized (CURRENTLY_PROCESSED_REQUESTS) {
      LOG.debug("Try start collecting missing attributes for request {}", request);
      if (CURRENTLY_PROCESSED_REQUESTS.contains(request)) {
        LOG.debug("Already processing request {}", request);
        return false;
      }
      CURRENTLY_PROCESSED_REQUESTS.add(request);
      LOG.debug("Start processing request {}", request);
      return true;
    }
  }

  public static void collect(Request req, PIPRequest pipRequest) throws IOException {
    String request = XACMLRequestProvider.stringify(req);
    if (!MISSING_ATTRS.containsKey(request)) {
      MISSING_ATTRS.put(request, Collections.newSetFromMap(new ConcurrentHashMap<PIPRequest, Boolean>()));
    }
    LOG.debug("Collecting missing attribute {} for {} ", request, pipRequest);
    Set<PIPRequest> set = MISSING_ATTRS.get(request);

    set.add(pipRequest);
  }

  public static SunfishPIPRequest build(Request request) throws IOException {
    String req = XACMLRequestProvider.stringify(request);
    LOG.debug("Done processing request {}", req);
    Set<PIPRequest> missingAttrs;
    synchronized (CURRENTLY_PROCESSED_REQUESTS) {
      missingAttrs = MISSING_ATTRS.remove(req);
      CURRENTLY_PROCESSED_REQUESTS.remove(req);
    }
    if (missingAttrs == null) {
      LOG.debug("No missing attributes collected for request {}", req);
      return null;
    }
    LOG.debug("{} missing attributes collected for request {}:", missingAttrs.size(), req);
    Collection<AttributeDesignator> attrs = new LinkedList<>();
    for (PIPRequest attr : missingAttrs) {
      AttributeDesignator des = new AttributeDesignator();
      des.setAttributeId(attr.getAttributeId());
      LOG.debug("ID:  {}", attr.getAttributeId());
      des.setCategory(attr.getCategory());
      LOG.debug("CAT: {}", attr.getCategory());
      des.setDataTypeId(attr.getDataTypeId());
      LOG.debug("DAT: {}", attr.getDataTypeId());
      des.setIssuer(attr.getIssuer());
      LOG.debug("ISS: {}", attr.getIssuer());
      attrs.add(des);
    }
    return new SunfishPIPRequest(request, attrs);
  }

}
