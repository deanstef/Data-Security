package eu.sunfishproject.icsp.prp;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.NotFoundException;
import org.sunfish.icsp.common.rest.model.ri.ReadResponse;
import org.sunfish.icsp.common.rest.ri.RIAdapter;
import org.sunfish.icsp.common.rest.ri.RestRI;
import org.sunfish.icsp.common.xacml.generated.PolicyListType;
import eu.sunfishproject.icsp.prp.config.PRPConfig;
import eu.sunfishproject.icsp.prp.exception.PRPException;
import eu.sunfishproject.icsp.prp.policyfinder.MultiThreadedPolicyFinderWithSmartTargetRiImpl;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

public class PRPPolicyStore implements PRPPolicyStoreIF {

 // INSTANCE;

//	private  PolicyStoreIF instance = null;
	private static final Logger log = LogManager.getLogger(PRPPolicyStore.class);

	private final String requestorId = "TODO";
	private final String token = "TODO";

	public PRPPolicyStore(){
//	  instance = PolicyStoreFactory.getInstance();
	}

	@Override
	public PolicySetType getPoliciesForDecisionRequest(final org.apache.openaz.xacml.api.Request decisionRequest, String policyType, String serviceID) throws PRPException
	{
        try {
        	final RIAdapter riAdapter = new RestRI(PRPConfig.getInstance().getRIUrl());

        	final PolicyListType result = riAdapter.serviceList(requestorId, token, serviceID, policyType);

			return new MultiThreadedPolicyFinderWithSmartTargetRiImpl().getPoliciesForDecisionRequest(decisionRequest, result.getPolicy());

        } catch(final ICSPException e) {
            throw new PRPException("Could not process request: " + e.getStatus(), e.getStatus());
        } catch (final Exception e) {
            throw new PRPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
        }

//		return instance.getPoliciesForDecisionRequest(decisionRequest);
	}

	@Override
	public ReadResponse getPolicy(final String id) throws NotFoundException, PRPException {
//		return instance.getPolicy(id, rootPolicy);
		try {
			final RIAdapter riAdapter = new RestRI(PRPConfig.getInstance().getRIUrl());
			final ReadResponse readResponse =  riAdapter.getPolicy(requestorId, token, id);
			return readResponse;
		} catch (final Exception e) {
			throw new PRPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

//	@Override
//	public PolicyType getPolicy(final String id, final String version, final boolean rootPolicy) throws NotFoundException {
//		return instance.getPolicy(id, version, rootPolicy);
//	}

	/*
	@Override
	public PolicySetType getPolicySet(final String id, final boolean rootPolicySet) throws NotFoundException, PRPException {
		try {
			RIAdapter riAdapter = new RestRI(PRPConfig.getInstance().getRIUrl());
			return riAdapter.getPolicySet(requestorId, token, id, rootPolicySet);
		} catch (Exception e) {
			throw new PRPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
*/

//	@Override
//	public PolicySetType getPolicySet(final String id, final String version, final boolean rootPolicySet) throws NotFoundException {
//		return instance.getPolicySet(id, version, rootPolicySet);
//	}

//	@Override
//	public PolicyListPolicySetListType getPoliciesList(IdReferenceListType idReferenceList) throws NotFoundException {
//		return instance.getPoliciesList(idReferenceList);
//	}

}
