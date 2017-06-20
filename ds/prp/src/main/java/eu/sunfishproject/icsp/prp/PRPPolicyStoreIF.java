package eu.sunfishproject.icsp.prp;

import org.apache.openaz.xacml.api.Request;
import org.sunfish.icsp.common.exceptions.NotFoundException;
import org.sunfish.icsp.common.rest.model.ri.ReadResponse;
import eu.sunfishproject.icsp.prp.exception.PRPException;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

public interface PRPPolicyStoreIF {

	public ReadResponse getPolicy(String id) throws NotFoundException, PRPException;
	//public PolicyType getPolicy(String id, String version, boolean rootPolicy) throws NotFoundException;

//	public PolicySetType getPolicySet(String id, boolean rootPolicy) throws NotFoundException, PRPException;
	//public PolicySetType getPolicySet(String id, String version, boolean rootPolicy) throws NotFoundException;

//	public PolicySetType getPoliciesForDecisionRequest(org.apache.openaz.xacml.api.Request decisionRequest) throws PRPException;//Table 8

	PolicySetType getPoliciesForDecisionRequest(Request decisionRequest, String policyType, String serviceID)
			throws PRPException;

	//public PolicyListPolicySetListType getPoliciesList(IdReferenceListType idReferenceList) throws NotFoundException;
}
