package eu.sunfishproject.icsp.prp.policyfinder;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public interface PolicyFinderIF {
//	public PolicySetType getPoliciesForDecisionRequest(Request decisionRequest, ConcurrentHashMap<String, PolicyType> policyMap, ConcurrentHashMap<String, PolicySetType> policySetMap);

	public PolicySetType getPoliciesForDecisionRequest(Request decisionRequest, ConcurrentHashMap<String, TreeMap<String, PolicyType>> rootPolicyMap, ConcurrentHashMap<String, TreeMap<String, PolicySetType>> rootPolicySetMap, ConcurrentHashMap<String, PolicyDef> rootPolicyDefMap);
		
}
