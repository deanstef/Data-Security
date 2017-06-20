package eu.sunfishproject.icsp.prp.policyfinder;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBElement;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyFinder;
import org.apache.openaz.xacml.pdp.policy.PolicyFinderFactory;
import org.apache.openaz.xacml.pdp.policy.Target;
import org.sunfish.icsp.common.rest.SunfishServices;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

public class ReturnAllPolicyFinderImpl implements PolicyFinderIF {

	@Override
	public PolicySetType getPoliciesForDecisionRequest(Request decisionRequest, ConcurrentHashMap<String, TreeMap<String, PolicyType>> rootPolicyMap, ConcurrentHashMap<String, TreeMap<String, PolicySetType>> rootPolicySetMap, ConcurrentHashMap<String, PolicyDef> rootPolicyDefMap) {
		PolicySetType policySet = new PolicySetType();
		policySet.setDescription("Auto generated "+new Date());
		policySet.setTarget(new TargetType());
		policySet.setVersion("1.0");//TODO use hash of all concatenated ids and versions??
		policySet.setPolicySetId(UUID.randomUUID().toString());
		policySet.setPolicyCombiningAlgId(XACML3.ID_POLICY_DENY_OVERRIDES.stringValue());
		List<JAXBElement<?>> set = policySet.getPolicySetOrPolicyOrPolicySetIdReference();
		for(TreeMap<String, PolicyType> treeMap:rootPolicyMap.values())
		{
			PolicyType policy = treeMap.firstEntry().getValue();
			if(policyMatches(decisionRequest, policy) && set.size()<1)//TODO
				set.add(new ObjectFactory().createPolicy(policy));
		}
		for(TreeMap<String, PolicySetType> treeMap:rootPolicySetMap.values())
		{
			PolicySetType policySet_ = treeMap.firstEntry().getValue();
			if(policySetMatches(decisionRequest, policySet_) && set.size()<1)//TODO
				set.add(new ObjectFactory().createPolicySet(policySet_));
		}
		return policySet;
	}

	private boolean policySetMatches(Request decisionRequest, PolicySetType policySet_) {
		return true;
	}

	private boolean policyMatches(Request decisionRequest, PolicyType policy) {
		return true;
	}
}
