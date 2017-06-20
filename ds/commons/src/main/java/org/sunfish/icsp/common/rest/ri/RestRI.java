package org.sunfish.icsp.common.rest.ri;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.DeserializationException;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.ri.*;
import org.sunfish.icsp.common.util.CommonUtil;
import org.sunfish.icsp.common.xacml.generated.PolicyListType;

import com.fasterxml.jackson.databind.ObjectMapper;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class RestRI implements RIAdapter {


	private static final Logger log = LogManager.getLogger(RestRI.class);
	private final String                         resource;
	private final WebTarget ri;

	public RestRI(final String resource) {
		this.resource = resource;
		final Client client = CommonSetup.createClient();
		ri = client.target(resource);

	}


	@Override
	public String store(final String policy, final String requestorID, final String token, final String expirationTime, final String id, final String serviceID, final String policyType) throws ICSPException{


		LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", ri.getUri(), "/", SunfishServices.RI_PATH_STORE);


		final String base64Policy = Base64.encodeBase64String(policy.getBytes());

		final StoreBody body = new StoreBody(requestorID, token, base64Policy, expirationTime, id, serviceID, policyType);



		final Response response = ri.path(SunfishServices.RI_PATH_STORE)
				.request(ExtendedMediaType.APPLICATION_JSON)
				.post(Entity.entity(body, ExtendedMediaType.APPLICATION_JSON));


		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
			};
		}

		return response.readEntity(String.class);
	}


	@Override
	public String update(final String policy, final String requestorID, final String token, final String id) throws ICSPException {

		LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", ri.getUri(), "/", SunfishServices.RI_PATH_UPDATE);


		final String base64Policy = Base64.encodeBase64String(policy.getBytes());

		final UpdateBody body = new UpdateBody(requestorID, token, id, base64Policy);


		final Response response = ri.path(SunfishServices.RI_PATH_UPDATE)
				.request(ExtendedMediaType.APPLICATION_JSON)
				.post(Entity.entity(body, ExtendedMediaType.APPLICATION_JSON));


		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
			};
		}

		return response.readEntity(String.class);
	}


	@Override
	public String delete(final String requestorID, final String token, final String id) throws ICSPException {

		LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", ri.getUri(), "/", SunfishServices.RI_PATH_DELETE);


		final DeleteBody body = new DeleteBody(requestorID, token, id);


		final Response response = ri.path(SunfishServices.RI_PATH_DELETE)
				.request(ExtendedMediaType.APPLICATION_JSON)
				.post(Entity.entity(body, ExtendedMediaType.APPLICATION_JSON));


		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
			};
		}

		return response.readEntity(String.class);

	}

	@Override
	public ServiceResponse service(final String requestorID, final String token, final String serviceID, final String policyType) throws ICSPException {

		LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", ri.getUri(), "/", SunfishServices.RI_PATH_SERVICE);


		final ServiceBody body = new ServiceBody(requestorID, token, serviceID, policyType);


		final Response response = ri.path(SunfishServices.RI_PATH_SERVICE)
				.request(ExtendedMediaType.APPLICATION_JSON)
				.post(Entity.entity(body, ExtendedMediaType.APPLICATION_JSON));


		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
			};
		}

		return response.readEntity(ServiceResponse.class);

	}


	@Override
	public ReadResponse getPolicy(final String requestorId, final String token, final String id) throws ICSPException {
		LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", ri.getUri(), "/", SunfishServices.RI_PATH_READ);

		final ReadBody body = new ReadBody(requestorId, token, id);


		final Response response = ri.path(SunfishServices.RI_PATH_READ)
				.request(ExtendedMediaType.APPLICATION_JSON)
				.post(Entity.entity(body, ExtendedMediaType.APPLICATION_JSON));


		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
			};
		}

		final ReadResponse readResponse =  response.readEntity(ReadResponse.class);
		return readResponse;
	}


//	@Override
//	public PolicySetType getPolicySet(String requestorId, String token, String id, boolean rootPolicySet) {
//		// TODO Auto-generated method stub
//		//FIXME
//		return null;
//	}


	@Override
	public PolicyListType serviceList(final String requestorId, final String token, final String serviceID, final String policyType) throws ICSPException {
		LogManager.getLogger(getClass()).debug("Issuing Request to {}{}", ri.getUri(), "/", SunfishServices.RI_PATH_SERVICE);

		final ServiceBody body = new ServiceBody(requestorId, token, serviceID, policyType);


		final Response response = ri.path(SunfishServices.RI_PATH_SERVICE)
				.request(ExtendedMediaType.APPLICATION_JSON)
				.post(Entity.entity(body, ExtendedMediaType.APPLICATION_JSON));

		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			if(response.getStatusInfo().getStatusCode() == Response.Status.NOT_FOUND.getStatusCode())
			{
				return new PolicyListType();//change for PDP
			}
			else
			{
				throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
				};
			}
		}

		final String bodyString = response.readEntity(String.class);
		final PolicyListType policyListType = new PolicyListType();

		try {
			final ObjectMapper mapper = new ObjectMapper();
			final ServiceResponse serviceResponse = mapper.readValue(bodyString, ServiceResponse.class);

			final List<ServiceresponseList> list = serviceResponse.getList();
			for(final ServiceresponseList serviceResponseList : list) {
				final String policyString = new String(Base64.decodeBase64(serviceResponseList.getPolicy()));

				final PolicyType policyType_ = CommonUtil.stringToPolicyType(policyString);

				if(policyType_ != null) {
					policyListType.getPolicy().add(policyType_);
				}
			}
		} catch(final Exception e) {
			log.error("Could not deserialize" , e);
			throw new DeserializationException("Invalid json", Response.Status.INTERNAL_SERVER_ERROR);
		}
		return policyListType;
	}





}
