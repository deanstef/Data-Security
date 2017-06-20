package eu.sunfishproject.icsp.pdp.net;

import java.util.Collection;
import java.util.Set;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyIdReferenceBase;
import org.sunfish.icsp.common.exceptions.ICSPException;

import eu.sunfishproject.icsp.pdp.PolicyCache;

public interface PRPAdapter {

  public PolicyCache.CachedPolicy collect(Request request, String serviceID, String policyType) throws ICSPException;

  public <V extends PolicyDef> Collection<PolicyCache.CachedPolicy> get(
      final Set<PolicyIdReferenceBase<V>> referencedPolicies) throws ICSPException;

  public String getName();
}
