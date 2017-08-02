package eu.sunfishproject.icsp.pap.net.api.v1;

import eu.sunfishproject.icsp.pap.config.PAPConfig;
import eu.sunfishproject.icsp.pap.exceptions.PAPException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.ri.ServiceResponse;
import org.sunfish.icsp.common.rest.model.ri.ServiceresponseList;
import org.sunfish.icsp.common.rest.ri.RIAdapter;
import org.sunfish.icsp.common.rest.ri.RestRI;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.util.CommonUtil;
import org.sunfish.icsp.common.xacml.generated.PolicyListType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PAPPolicyResource {

    private static final Logger log = LogManager.getLogger(PAPPolicyResource.class);



    String issuer = null;

    public PAPPolicyResource(final String issuer) {
        this.issuer = issuer;
    }


    @GET
    @ApiOperation(value = Documentation.PAP_GET_POLICIES, nickname = "get-policies")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Documentation.PAP_GET_POLICIES_200, response = String.class),
            @ApiResponse(code = 400, message = Documentation.PAP_GET_POLICIES_400),
            @ApiResponse(code = 403, message = Documentation.PAP_GET_POLICIES_403),
            @ApiResponse(code = 404, message = Documentation.PAP_GET_POLICIES_404)

    })
    @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
    public PolicyListType getPolicies(@ApiParam(value=Documentation.PAP_GET_POLICIES_SERVICE_ID, required = true)
                                      @QueryParam("serviceID")
                                      final String serviceID,
                                      @ApiParam(value=Documentation.PAP_GET_POLICIES_POLICY_TYPE, required = true)
                                      @QueryParam("policyType")
                                      final String policyType) throws PAPException {

        final String requestorID = PAPConfig.getInstance().getRIRequestorID();
        final String token = PAPConfig.getInstance().getRIToken();


        if(serviceID == null || serviceID.isEmpty() || policyType == null || policyType.isEmpty()) {
            throw new PAPException("Bad Request", Response.Status.BAD_REQUEST);
        }


        final RIAdapter riAdapter = new RestRI(PAPConfig.getInstance().getRIUrl());


        try {
            final ServiceResponse serviceResponse = riAdapter.service(requestorID, token, serviceID, policyType);
            return generatePolicyListType(serviceResponse);

        } catch(final ICSPException e) {
            throw new PAPException("Could not process request: " + e.getStatus(), e.getStatus());
        } catch (final Exception e) {
            log.error("Erroro retrieving policies", e);
            throw new PAPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    @POST
    @ApiOperation(value = Documentation.PAP_ADD_POLICIES, nickname = "add-policies")
    @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Documentation.PAP_ADD_POLICIES_200, response = String.class),
            @ApiResponse(code = 400, message = Documentation.PAP_ADD_POLICIES_400),
            @ApiResponse(code = 403, message = Documentation.PAP_ADD_POLICIES_403),
            @ApiResponse(code = 409, message = Documentation.PAP_ADD_POLICIES_409)

    })
    public String addPolicy(@ApiParam(value=Documentation.PAP_ADD_POLICIES_SERVICE_ID, required = true)
                            @QueryParam("serviceID")
                            final String serviceID,
                            @ApiParam(value=Documentation.PAP_ADD_POLICIES_POLICY_TYPE, required = true)
                            @QueryParam("policyType")
                            final String policyType,
                            @ApiParam(value=Documentation.PAP_ADD_POLICIES_EXPIRATION_TIME, required = true)
                            @QueryParam("expirationTime")
                            final String expirationTime,
                            @ApiParam(value = Documentation.PAP_ADD_POLICIES_BODY, required = true) final
                            PolicyType policy) throws PAPException {


        final String policyString = CommonUtil.policyTypeToString(policy);
        final String requestorID = PAPConfig.getInstance().getRIRequestorID();
        final String token = PAPConfig.getInstance().getRIToken();
        final String id = policy.getPolicyId();


        if(serviceID == null || serviceID.isEmpty() || policyType == null || policyType.isEmpty() || expirationTime == null || expirationTime.isEmpty()) {
            throw new PAPException("Bad Request", Response.Status.BAD_REQUEST);
        }


        final RIAdapter riAdapter = new RestRI(PAPConfig.getInstance().getRIUrl());


        try {
            riAdapter.store(policyString, requestorID, token, expirationTime, id, serviceID, policyType);
        } catch(final ICSPException e) {
            throw new PAPException("Could not process request: " + e.getStatus(), e.getStatus());
        } catch (final Exception e) {
            throw new PAPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return "Success";

    }

//    @PUT
//    @ApiOperation(value = Documentation.PAP_UPDATE_POLICIES, nickname = "update-policies")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = Documentation.PAP_UPDATE_POLICIES_200, response = String.class),
//            @ApiResponse(code = 400, message = Documentation.PAP_UPDATE_POLICIES_400),
//            @ApiResponse(code = 403, message = Documentation.PAP_UPDATE_POLICIES_403),
//            @ApiResponse(code = 404, message = Documentation.PAP_UPDATE_POLICIES_404)
//
//    })
//    public String updatePolicy(@ApiParam(value = Documentation.PAP_ADD_POLICIES_BODY, required = true)
//                               PolicyType policy) throws PAPException {
//
//
//        String policyString = CommonUtil.policyTypeToString(policy);
//        String requestorID = PAPConfig.getInstance().getRIRequestorID();
//        String token = PAPConfig.getInstance().getRIToken();
//        String id = policy.getPolicyId();
//
//        RIAdapter riAdapter = new RestRI(PAPConfig.getInstance().getRIUrl());
//
//        try {
//            riAdapter.update(policyString, requestorID, token, id);
//        } catch(ICSPException e) {
//            throw new PAPException("Could not process request: " + e.getStatus(), e.getStatus());
//        }
//
//        return "Success";
//    }


    @DELETE
    @Path(SunfishServices.PAP_PATH_ID)
    @ApiOperation(value = Documentation.PAP_DELETE_POLICIES, nickname = "delete-policies")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Documentation.PAP_DELETE_POLICIES_200, response = String.class),
            @ApiResponse(code = 400, message = Documentation.PAP_DELETE_POLICIES_400),
            @ApiResponse(code = 403, message = Documentation.PAP_DELETE_POLICIES_403),
            @ApiResponse(code = 404, message = Documentation.PAP_DELETE_POLICIES_404)

    })
    public String deletePolicy(@ApiParam(value=Documentation.PAP_DELETE_POLICIES_ID, required = true)
                               @PathParam("id")
                               final String id) throws PAPException {


        final String requestorID = PAPConfig.getInstance().getRIRequestorID();
        final String token = PAPConfig.getInstance().getRIToken();

        final RIAdapter riAdapter = new RestRI(PAPConfig.getInstance().getRIUrl());

        try {
            riAdapter.delete(requestorID, token, id);
        } catch(final ICSPException e) {
            throw new PAPException("Could not process request: " + e.getStatus(), e.getStatus());
        }  catch (final Exception e) {
            throw new PAPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
        }

        return "Success";

    }



    private PolicyListType generatePolicyListType(final ServiceResponse serviceResponse) throws PAPException {


        final PolicyListType policyListType = new PolicyListType();

        final List<ServiceresponseList> list = serviceResponse.getList();

        for(final ServiceresponseList serviceResponseList : list) {

            final String policyString = new String(Base64.decodeBase64(serviceResponseList.getPolicy()));


            final PolicyType policyType = CommonUtil.stringToPolicyType(policyString);

            if(policyType != null) {
                policyListType.getPolicy().add(policyType);
            }

        }

        return policyListType;

    }

}
