package eu.sunfishproject.icsp.prp.api.v1;

import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.NotFoundException;
import org.sunfish.icsp.common.rest.SunfishServices;

import eu.sunfishproject.icsp.prp.PRPPolicyStore;
import eu.sunfishproject.icsp.prp.PRPPolicyStoreIF;
import eu.sunfishproject.icsp.prp.exception.PRPException;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import org.sunfish.icsp.common.swagger.Documentation;

public class PolicySetResource {
	private static final Logger log = LogManager.getLogger(PolicySetResource.class);
//	@Context
//	UriInfo uriInfo;
//	@Context
//	Request request;
	String id;
	String version;
	HttpServletResponse response;
	static PRPPolicyStoreIF prpStore = null;

	public PolicySetResource(final String id, final String version, final HttpServletResponse response) throws IOException {
//		this.uriInfo = uriInfo;
//		this.request = request;
		this.id = id;
		this.version = version;
		this.response = response;
		if(prpStore == null) {
      prpStore = new PRPPolicyStore();
    }
	}

	/*
	@GET
	@PermitAll
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@ApiOperation(value = Documentation.PRP_POLICYSET_ID_VERSION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Documentation.PRP_POLICYSET_ID_VERSION_200, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_EXPIRATION_TIME, description = Documentation.PRP_HEADER_EXPIRATION, response = String.class), response = String.class),
			@ApiResponse(code = 400, message = Documentation.PRP_POLICYSET_ID_VERSION_400),
			@ApiResponse(code = 403, message = Documentation.PRP_POLICYSET_ID_VERSION_403),
			@ApiResponse(code = 404, message = Documentation.PRP_POLICYSET_ID_VERSION_404)

	})
	public PolicySetType getPolicySet(@ApiParam(value=Documentation.PRP_QUERY_PARAM_ROOT_POLICY_SET, required = true)
									  @QueryParam(SunfishServices.QUERY_PARAMETER_ROOT_POLICY_SET)
									  final Boolean rootPolicySet) throws NotFoundException, PRPException {
		log.debug("get policyset:"+id);
//		final PolicySetType policySet = prpStore.getPolicySet(id, version, rootPolicySet);
		final PolicySetType policySet = prpStore.getPolicySet(id, rootPolicySet);
		final long expirationTime = System.currentTimeMillis()+5*60*1000;
		response.setHeader("expirationTime", String.valueOf(expirationTime));
		return policySet;
	}
	*/
}
