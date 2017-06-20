package eu.sunfishproject.icsp.pdp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyIdReference;
import org.apache.openaz.xacml.pdp.policy.PolicyIdReferenceBase;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.xacml.generated.IdReferenceListType;

import eu.sunfishproject.icsp.pdp.PolicyCache.CachedPolicy;
import eu.sunfishproject.icsp.pdp.net.PRPAdapter;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;

public class RestPRP implements PRPAdapter {

  private final String    resource;
  private final WebTarget prp;

  public RestPRP(final String resource) {
    this.resource = resource;
    final Client client = CommonSetup.createClient();

    prp = client.target(resource);
  }

  @Override
  public CachedPolicy collect(final Request request, final String serviceID, final String policyType)
      throws ICSPException {

    LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", prp.getUri(), "/",
        SunfishServices.PRP_PATH_COLLECT);
    // issue request
    final Response response = prp.path(SunfishServices.PRP_PATH_COLLECT)
        .request(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)
        .header(SunfishServices.HEADER_SERVICE, serviceID)
        .header(SunfishServices.HEADER_POLICY_TYPE, policyType)
        .post(Entity.entity(request, ExtendedMediaType.APPLICATION_XML_XACML_TYPE));
    // success?
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new ICSPException("Failed : HTTP error", Status.fromStatusCode(response.getStatus())) {
      };
    }
    // return
    final String headerString = response.getHeaderString(SunfishServices.HEADER_EXPIRATION_TIME);
    long exp = -1L;
    if (headerString != null) {
      try {
        exp = Long.parseLong(headerString);
      } catch (final Exception e) {
        LogManager.getLogger(getClass()).debug("Expiration time '{}' could not be parsed", headerString);
      }
    }
    final PolicyDef policy = response.readEntity(PolicyDef.class);
    return new CachedPolicy(exp, policy, this);

  }

  @Override
  public String getName() {
    return resource;
  }

  @Override
  public <V extends PolicyDef> Collection<CachedPolicy> get(
      final Set<PolicyIdReferenceBase<V>> referencedPolicies) throws ICSPException {
    final IdReferenceListType ref = new IdReferenceListType();
    for (final PolicyIdReferenceBase<V> pol : referencedPolicies) {
      final IdReferenceType e = new IdReferenceType();
      e.setValue(pol.getIdReferenceMatch().getId().stringValue());
      e.setEarliestVersion(pol.getIdReferenceMatch().getEarliestVersion().getVersionMatch());
      e.setLatestVersion(pol.getIdReferenceMatch().getLatestVersion().getVersionMatch());
      e.setVersion(pol.getIdReferenceMatch().getVersion().getVersionMatch());
      if (pol instanceof PolicyIdReference) {
        ref.getPolicyIdReference().add(e);
      } else {
        ref.getPolicySetIdReference().add(e);
      }
    }

    LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", prp.getUri(), "/",
        SunfishServices.PRP_PATH_POLICIES_BY_REFERENCE);
    // issue request
    final Response response = prp.path(SunfishServices.PRP_PATH_POLICIES_BY_REFERENCE)
        .request(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)
        .post(Entity.entity(ref, ExtendedMediaType.APPLICATION_XML_XACML_TYPE));

    // success?
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      // TODO check 404, and abort in order to reach indeterminate
      throw new ICSPException("Failed : HTTP error", Status.fromStatusCode(response.getStatus())) {
      };
    }
    final GenericType<List<PolicyDef>> type = new GenericType<List<PolicyDef>>() {
    };

    final List<PolicyDef> result = response.readEntity(type);
    final List<CachedPolicy> cachedPolicies = new LinkedList<>();
    for (final PolicyDef def : result) {
      cachedPolicies.add(new CachedPolicy(def, this));
    }
    return cachedPolicies;
  }

}
