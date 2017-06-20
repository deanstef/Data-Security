package org.sunfish.icsp.common.rest.pip;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorComparator;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorSet;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;


public class RestPIP implements PIPAdapter {

  private final String                         resource;
  private final WebTarget                      pip;
  private final SortedSet<AttributeDesignator> providedAttributes;

  public RestPIP(final String resource) {
    this.resource = resource;
    providedAttributes = new TreeSet<>(new AttributeDesignatorComparator());
    final Client client = CommonSetup.createClient();
    pip = client.target(resource);

  }

  @Override
  public String getName() {
    return resource;
  }

  @Override
  public Request request(final SunfishPIPRequest request) throws ICSPException {
    LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", pip.getUri(), "/",
        SunfishServices.PIP_PATH_REQUEST);
    // issue request
    final Response response = pip.path(SunfishServices.PIP_PATH_REQUEST)
        .request(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)
        .post(Entity.entity(request, ExtendedMediaType.APPLICATION_XML_XACML_TYPE));
    // success?
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new ICSPException("Failed : HTTP error", Status.fromStatusCode(response.getStatus())) {
      };
    }
    return response.readEntity(Request.class);
  }

  @Override
  public Set<AttributeDesignator> collect() throws ICSPException {
    LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", pip.getUri(), "/",
        SunfishServices.PIP_PATH_COLLECT);
    // issue request
    final Response response = pip.path(SunfishServices.PIP_PATH_COLLECT)
        .request(ExtendedMediaType.APPLICATION_XML_XACML_TYPE).get();
    // success?
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new ICSPException("Failed : HTTP error", Status.fromStatusCode(response.getStatus())) {
      };
    }
    return response.readEntity(AttributeDesignatorSet.class);
  }

  @Override
  public boolean matches(final SunfishPIPRequest req) {
    if (providedAttributes.isEmpty()) {
      try {
        final Set<AttributeDesignator> collect = collect();
        providedAttributes.addAll(collect);
      } catch (final ICSPException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return providedAttributes.containsAll(req.getAttributeDesignators());
  }

  @Override
  public SortedSet<AttributeDesignator> getMatchingAttributes(final SunfishPIPRequest req) {
    if (providedAttributes.isEmpty()) {
      try {
        providedAttributes.addAll(collect());
      } catch (final ICSPException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    final TreeSet<AttributeDesignator> treeSet = new TreeSet<AttributeDesignator>(
        providedAttributes.comparator());
    treeSet.addAll(providedAttributes);
    final TreeSet<AttributeDesignator> matching = treeSet;
    matching.retainAll(req.getAttributeDesignators());
    return matching;
  }

}
