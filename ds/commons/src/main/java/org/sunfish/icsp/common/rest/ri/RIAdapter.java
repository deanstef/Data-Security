package org.sunfish.icsp.common.rest.ri;

import org.sunfish.icsp.common.exceptions.DeserializationException;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.xacml.generated.PolicyListType;
import org.sunfish.icsp.common.rest.model.ri.ReadResponse;
import org.sunfish.icsp.common.rest.model.ri.ServiceResponse;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface RIAdapter {

    String store(String policy, String requestorID, String token, String expirationTime, String id, String serviceID, String policyType) throws ICSPException;

    //String update(String policy, String requestorID, String token, String id) throws ICSPException;

    String delete(String requestorID, String token, String id) throws ICSPException;

    ServiceResponse service(String requestorID, String token, String serviceID, String policyType) throws ICSPException;

	ReadResponse getPolicy(String requestorId, String token, String id) throws ICSPException;

	//PolicySetType getPolicySet(String requestorId, String token, String id, boolean rootPolicySet);

	PolicyListType serviceList(String requestorId, String token, String serviceID, String policyType) throws DeserializationException, ICSPException;

}
