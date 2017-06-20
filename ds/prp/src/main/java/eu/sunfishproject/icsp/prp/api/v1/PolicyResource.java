/**
 *
 */
package eu.sunfishproject.icsp.prp.api.v1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.InvalidRequestException;
import org.sunfish.icsp.common.exceptions.NotFoundException;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.ri.ReadResponse;

import eu.sunfishproject.icsp.prp.exception.PRPException;
import eu.sunfishproject.icsp.prp.PRPPolicyStore;
import eu.sunfishproject.icsp.prp.PRPPolicyStoreIF;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.util.CommonUtil;

public class PolicyResource {
  private static final Logger log      = LogManager.getLogger(PolicyResource.class);
  // @Context
  // UriInfo uriInfo;
  // @Context
  // Request request;
  String                      id;
  String                      version;
  HttpServletResponse         response;
  PRPPolicyStoreIF            prpStore = null;

  public PolicyResource(final String id, final String version, final HttpServletResponse response)
      throws IOException {
    // this.uriInfo = uriInfo;
    // this.request = request;
    this.id = id;
    this.version = version;
    this.response = response;
    if (prpStore == null) {
      prpStore = new PRPPolicyStore();
    }
  }

  @GET
  @PermitAll
  // @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  @ApiOperation(value = Documentation.PRP_POLICY_ID_VERSION)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Documentation.PRP_POLICY_ID_VERSION_200, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_EXPIRATION_TIME, description = Documentation.PRP_HEADER_EXPIRATION, response = String.class), response = String.class),
      @ApiResponse(code = 400, message = Documentation.PRP_POLICY_ID_VERSION_400),
      @ApiResponse(code = 403, message = Documentation.PRP_POLICY_ID_VERSION_403),
      @ApiResponse(code = 404, message = Documentation.PRP_POLICY_ID_VERSION_404)

  })
  public PolicyType getPolicy() throws NotFoundException, InvalidRequestException, PRPException {

    PolicyType policy = null;
    // policy = prpStore.getPolicy(id, version, rootPolicy);
    final ReadResponse readResponse = prpStore.getPolicy(id);
    try {
      final String policyString = new String(Base64.getDecoder().decode(readResponse.getPolicy()), "UTF-8");
      policy = CommonUtil.stringToPolicyType(policyString);
    } catch (final UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (policy == null) {
      throw new NotFoundException("The policy with the specified id was not found");
    }
    log.debug("Found policy:" + id);
    final long expirationTime = Long.parseLong(readResponse.getExpirationTime());
    response.setHeader("expirationTime", String.valueOf(expirationTime));
    return policy;
  }

}