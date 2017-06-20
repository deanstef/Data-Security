package eu.sunfishproject.icsp.prp;

import java.io.IOException;
import java.util.Collection;

import org.sunfish.icsp.common.exceptions.ConflictException;
import org.sunfish.icsp.common.exceptions.NotFoundException;
import org.sunfish.icsp.common.xacml.generated.IdReferenceListType;
import org.sunfish.icsp.common.xacml.generated.PolicyListPolicySetListType;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public interface PolicyStoreIF {

	public boolean SUCCESSFULLY_UPDATED = true;
	public boolean SUCCESSFULLY_ADDED = true;
	public boolean SUCCESSFULLY_DELETED = true;

	void addPolicy(PolicyType policy, boolean rootPolicy) throws ConflictException;
	void addPolicySet(PolicySetType policySet, boolean rootPolicySet) throws ConflictException;

	void updatePolicy(PolicyType policy, boolean rootPolicy) throws NotFoundException;
	void updatePolicySet(PolicySetType policySet, boolean rootPolicySet) throws NotFoundException;

	PolicyType deletePolicy(String id, String version, boolean rootPolicyt) throws NotFoundException;
	PolicySetType deletePolicySet(String id, String version, boolean rootPolicySet) throws NotFoundException;

	Collection<? extends PolicyType> getAllPolicies(Boolean rootPolicy);
	Collection<? extends PolicySetType> getAllPolicySets(Boolean rootPolicySet);
	long getLastModifiedTime();

	public PolicyType getPolicy(String id, boolean rootPolicy) throws NotFoundException;
	public PolicyType getPolicy(String id, String version, boolean rootPolicy) throws NotFoundException;

	public PolicySetType getPolicySet(String id, boolean rootPolicySet) throws NotFoundException;
	public PolicySetType getPolicySet(String id, String version, boolean rootPolicySet) throws NotFoundException;

	public PolicySetType getPoliciesForDecisionRequest(org.apache.openaz.xacml.api.Request decisionRequest);
	public PolicyListPolicySetListType getPoliciesList(IdReferenceListType idReferenceList) throws NotFoundException;
}
