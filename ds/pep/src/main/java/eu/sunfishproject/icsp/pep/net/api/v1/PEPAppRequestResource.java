package eu.sunfishproject.icsp.pep.net.api.v1;

import eu.sunfishproject.icsp.pep.exceptions.PEPException;
import org.sunfish.icsp.common.session.UserSession;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.rest.model.SunfishSignature;
import org.sunfish.icsp.common.swagger.Documentation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Api
public class PEPAppRequestResource extends PEPRequestResource {


    Logger log = LogManager.getLogger(PEPAppRequestResource.class);

    public PEPAppRequestResource(final String issuer, final SunfishService service, final SunfishRequest request,
                                 final String requestParams, final SunfishRequestData requestData, final SunfishSignature signature, UserSession userSession,
                                 final HttpServletResponse servletResponse) {
        super(issuer, service, request, requestParams, requestData, signature, userSession, servletResponse);
    }


    @POST
    @Consumes({MediaType.WILDCARD})
    @ApiOperation(value = Documentation.APP_REQUEST, nickname = "app-request")
    @ApiResponses(value = @ApiResponse(code = 200, message = Documentation.BODY_TARGET, response = byte[].class))
    public javax.ws.rs.core.Response request(@ApiParam(value = Documentation.BODY, required = false)
                                             byte[] requestBody) throws PEPException {


        try {
            Response httpResponse = handleRequest(requestBody);

//            MultivaluedMap<String, Object> headers = httpResponse.getHeaders();
//            Iterator<String> it = headers.keySet().iterator();

//            while (it.hasNext()) {
//                String key = it.next();
//                if(!key.equals("Transfer-Encoding")) {
//                    servletResponse.addHeader(key, headers.getFirst(key).toString());
//                }
//            }

            return httpResponse;

        } catch (ICSPException e) {
            throw new PEPException("Could not process request: " + e.getStatus(), e.getStatus());
        } catch (Exception e) {
            throw new PEPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
        }


    }


}
