package eu.sunfishproject.icsp.pep.net.api.v1;

import eu.sunfishproject.icsp.pep.exceptions.PEPException;
import org.sunfish.icsp.common.session.SessionStorage;
import org.sunfish.icsp.common.session.UserSession;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.rest.model.SunfishSignature;
import org.sunfish.icsp.common.swagger.Documentation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 *
 */

@Api
@Path(SunfishServices.PEP_VERSION)
public class PEPResource {

    private static final Logger log = LogManager.getLogger(PEPResource.class);


    @ApiOperation(hidden = true, value = "hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String get() {
        return "HELLO";
    }



    @Path(SunfishServices.PEP_PATH_APP_REQUEST)
    public PEPAppRequestResource appRequest(@ApiParam(value = Documentation.HEADER_ISSUER)
                                            @HeaderParam(SunfishServices.HEADER_ISSUER)
                                            String issuer,
                                            @ApiParam(value = Documentation.HEADER_SERVICE, required = true)
                                            @HeaderParam(SunfishServices.HEADER_SERVICE)
                                            SunfishService service,
                                            @ApiParam(value = Documentation.HEADER_REQUEST, required = true)
                                            @HeaderParam(SunfishServices.HEADER_REQUEST)
                                            SunfishRequest request,
                                            @ApiParam(value = Documentation.HEADER_REQUEST_PARAMETERS)
                                            @HeaderParam(SunfishServices.HEADER_REQUEST_PARAMETERS)
                                            String requestParams,
                                            @ApiParam(value = Documentation.HEADER_REQUEST_DATA, required = true)
                                            @HeaderParam(SunfishServices.HEADER_REQUEST_DATA)
                                            SunfishRequestData requestData,
                                            @ApiParam(value = Documentation.HEADER_SIGNATURE)
                                            @HeaderParam(SunfishServices.HEADER_SIGNATURE)
                                            SunfishSignature signature,
                                            @ApiParam(hidden = true)
                                            @HeaderParam("Content-Type") String contentType,
                                            @ApiParam(hidden = true)
                                            @Context HttpServletResponse servletResponse) throws PEPException {


        if(service == null || request == null || requestData == null || !request.isValid() || service.getId() == null) {
            throw new BadRequestException();
        }
        request.setContentType(contentType);

        String userSessionString = requestData.getQueryParams().get(SunfishServices.COOKIE_NAME_SESSION);
        String userSessionCookie = requestData.getCookies().get(SunfishServices.COOKIE_NAME_SESSION);

        UserSession userSession = retrieveUserSession(servletResponse, userSessionString, userSessionCookie);


        return new PEPAppRequestResource(issuer, service, request, requestParams, requestData, signature, userSession, servletResponse);

    }


    @Path(SunfishServices.PEP_PATH_REQUEST)
    public PEPInterRequestResource request(@ApiParam(value = Documentation.HEADER_ISSUER)
                                           @HeaderParam(SunfishServices.HEADER_ISSUER)
                                           String issuer,
                                           @ApiParam(value = Documentation.HEADER_SERVICE, required = true)
                                           @HeaderParam(SunfishServices.HEADER_SERVICE)
                                           SunfishService service,
                                           @ApiParam(value = Documentation.HEADER_REQUEST, required = true)
                                           @HeaderParam(SunfishServices.HEADER_REQUEST)
                                           SunfishRequest request,
                                           @ApiParam(value = Documentation.HEADER_REQUEST_PARAMETERS)
                                           @HeaderParam(SunfishServices.HEADER_REQUEST_PARAMETERS)
                                           String requestParams,
                                           @ApiParam(value = Documentation.HEADER_REQUEST_DATA, required = true)
                                           @HeaderParam(SunfishServices.HEADER_REQUEST_DATA)
                                           SunfishRequestData requestData,
                                           @ApiParam(value = Documentation.HEADER_SIGNATURE)
                                           @HeaderParam(SunfishServices.HEADER_SIGNATURE)
                                           SunfishSignature signature,
                                           @ApiParam(value = Documentation.HEADER_ACTIVITY_CONTEXT)
                                           @HeaderParam(SunfishServices.HEADER_ACTIVITY_CONTEXT) String activityContext,
                                           @ApiParam(hidden = true)
                                           @HeaderParam("Content-Type") String contentType,
                                           @ApiParam(hidden = true)
                                           @Context HttpHeaders headers,
                                           @ApiParam(hidden = true)
                                           @Context HttpServletResponse servletResponse) throws PEPException {


        if(service == null || request == null || requestData == null || !request.isValid()) {
            throw new BadRequestException();
        }
        request.setContentType(contentType);


        String userSessionString = requestData.getQueryParams().get(SunfishServices.COOKIE_NAME_SESSION);
        String userSessionCookie = requestData.getCookies().get(SunfishServices.COOKIE_NAME_SESSION);

        UserSession userSession = retrieveUserSession(servletResponse, userSessionString, userSessionCookie);

        return new PEPInterRequestResource(issuer, service, request, requestParams, requestData, signature, activityContext, userSession, servletResponse);

    }

    private UserSession retrieveUserSession(HttpServletResponse servletResponse, String userSessionString, String userSessionCookie) {

        UserSession userSession = null;
        if(userSessionString != null) {
            userSession = SessionStorage.getInstance().getUserSession(userSessionString);
            if(userSession != null) {
                setCookie(servletResponse, SunfishServices.COOKIE_NAME_SESSION, userSessionString);
            }
        } else if(userSessionCookie != null) {
            userSession = SessionStorage.getInstance().getUserSession(userSessionCookie);
        }

        return userSession;
    }


    private void setCookie(HttpServletResponse response, String name, String value) {
        Cookie myCookie = new Cookie(name, value);
        response.addCookie(myCookie);
    }







}
