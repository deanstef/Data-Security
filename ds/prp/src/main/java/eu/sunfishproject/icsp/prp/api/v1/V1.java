package eu.sunfishproject.icsp.prp.api.v1;

import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.swagger.Documentation;

import eu.sunfishproject.icsp.prp.PRPPolicyStore;
import eu.sunfishproject.icsp.prp.PRPPolicyStoreIF;
import eu.sunfishproject.icsp.prp.exception.PRPException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;

/**
 * This class implements the API version 1
 *
 * @author amarsalek
 *
 */
@Path("v1")
@Api
public class V1 {
  private static final Logger     log            = LogManager.getLogger(V1.class);
  private static PRPPolicyStoreIF prpPolicyStore = null;
  // private static final PolicyStoreFactory store =
  // PolicyStoreFactory.getInstance();
  // @Context
  // UriInfo uriInfo;
  // @Context
  // Request request;

  public V1() throws IOException {
    if (prpPolicyStore == null) {
      prpPolicyStore = new PRPPolicyStore();
    }
  }

  // PRP Endpoint - Retrieving policies by evaluating a decision request
  @POST
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path(SunfishServices.PRP_PATH_COLLECT)
  @PermitAll
  @ApiOperation(value = Documentation.PRP_COLLECT, nickname = "collect", position = 1)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Documentation.PRP_COLLECT_200, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_EXPIRATION_TIME, description = Documentation.PRP_HEADER_EXPIRATION, response = String.class), response = String.class),
      @ApiResponse(code = 400, message = Documentation.PRP_COLLECT_400),
      @ApiResponse(code = 404, message = Documentation.PRP_COLLECT_404)

  })
  public PolicySetType collect(
      @ApiParam(value = Documentation.HEADER_POLICY_TYPE, required = true) @HeaderParam(SunfishServices.HEADER_POLICY_TYPE) final String policyType,
      @ApiParam(value = Documentation.HEADER_SERVICE, required = true) @HeaderParam(SunfishServices.HEADER_SERVICE) final String serviceID,
      @ApiParam(value = Documentation.PRP_COLLECT_PARM_BODY, required = true, type = "java.lang.String") final org.apache.openaz.xacml.api.Request decisionRequest)
      throws PRPException {
    log.error("Before collect");
    final PolicySetType tmp = prpPolicyStore.getPoliciesForDecisionRequest(decisionRequest, policyType,
        serviceID);
    log.error("after collect");
    return tmp;
  }

  /*
   * //PRP Endpoint - Retrieving a policy set by id & version
   *
   * @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
   *
   * @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
   *
   * @Path(SunfishServices.PRP_PATH_POLICYSET_BY_ID_VERSION)
   * public PolicySetResource
   * getPolicySet(@ApiParam(value=Documentation.PRP_PATH_PARM_POLICYSET_ID,
   * required = true)
   *
   * @PathParam("id")
   * final String id,
   *
   * @ApiParam(value=Documentation.PRP_PATH_PARM_POLICYSET_VERSION, required =
   * true)
   *
   * @PathParam("version")
   * final String version,
   *
   * @ApiParam(hidden = true)
   *
   * @Context final HttpServletResponse response) throws IOException {
   * System.out.println("PolicySetResource getPolicy():"+id);
   * return new PolicySetResource(id, version, response);
   * }
   *
   */

  /*
   * //PRP Endpoint - Retrieving a policy by id and version
   *
   * @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
   *
   * @Path(SunfishServices.PRP_PATH_POLICY_BY_ID_VERSION)
   *
   * @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
   * public PolicyResource
   * getPolicy(@ApiParam(value=Documentation.PRP_PATH_PARM_POLICY_ID, required =
   * true)
   *
   * @PathParam("id") final String id,
   *
   * @ApiParam(value=Documentation.PRP_PATH_PARM_POLICY_VERSION, required=true)
   *
   * @PathParam("version") final String version,
   *
   * @ApiParam(hidden = true)
   *
   * @Context final HttpServletResponse response) throws IOException {
   * System.out.println("PolicyResource getPolicy():"+id+" v2:"+version);
   * return new PolicyResource(id, version, response);
   * }
   */

  // PRP Endpoint - Retrieving a policy by id
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path(SunfishServices.PRP_PATH_POLICY_BY_ID)
  public PolicyResource getPolicyById(
      @ApiParam(value = Documentation.PRP_PATH_PARM_POLICY_ID, required = true) @PathParam("id") final String id,
      @ApiParam(hidden = true) @Context final HttpServletResponse response) throws IOException {
    System.out.println("PolicyResource getPolicy():" + id + " v1:" + null);
    return new PolicyResource(id, null, response);
  }

  // PRP Endpoint - Retrieving a policy set by id
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path(SunfishServices.PRP_PATH_POLICYSET_BY_ID)
  public PolicySetResource getPolicySetById(
      @ApiParam(value = Documentation.PRP_PATH_PARM_POLICYSET_ID, required = true) @PathParam("id") final String id,
      @ApiParam(hidden = true) @Context final HttpServletResponse response) throws IOException {
    System.out.println("PolicySetResource getPolicy():" + id);
    return new PolicySetResource(id, null, response);
  }

  /*
   * @POST
   *
   * @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
   *
   * @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
   *
   * @Path(SunfishServices.PRP_PATH_POLICIES_BY_REFERENCE)
   *
   * @PermitAll
   * public PolicyListPolicySetListType getReferencedPolicies(final
   * IdReferenceListType idReferenceList) throws NotFoundException {
   * final PolicyListPolicySetListType tmp =
   * prpPolicyStore.getPoliciesList(idReferenceList);
   * return tmp;
   * }
   */
}
