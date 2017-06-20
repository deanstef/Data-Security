package eu.sunfishproject.icsp.pep.net.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sunfishproject.icsp.pep.exceptions.PEPException;
import org.sunfish.icsp.common.session.UserSession;
import io.swagger.annotations.*;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.*;
import org.sunfish.icsp.common.swagger.Documentation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Api
public class PEPInterRequestResource extends PEPRequestResource {


    String activityContext;


    public PEPInterRequestResource(final String issuer, final SunfishService service, final SunfishRequest request,
                                   final String requestParams, final SunfishRequestData requestData,
                                   final SunfishSignature signature, final String activityContext, UserSession userSession,
                                   final HttpServletResponse servletResponse) {

        super(issuer, service, request, requestParams, requestData, signature, userSession, servletResponse);
        this.activityContext = activityContext;
    }



    @POST
    @Consumes({MediaType.WILDCARD})
    @ApiOperation(value = Documentation.INTER_REQUEST, nickname = "inter-app-request")
    @ApiResponses(value = @ApiResponse(code = 200, message = Documentation.BODY, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_RESPONSE_DATA, description = Documentation.HEADER_RESPONSE_DATA, response = String.class), response = byte[].class))
    public javax.ws.rs.core.Response request(@ApiParam(value = Documentation.BODY, required = false)
                                             byte[] requestBody) throws PEPException {

        // TODO: At some point we will handle activityContext here
        // NOTE: TBD

        try {

            javax.ws.rs.core.Response httpResponse = handleRequest(requestBody);

            String reason = "TBD?!";
            int status = httpResponse.getStatus();

            Map<String, String> headersMap = new HashMap<>();

            MultivaluedMap<String, Object> headers = httpResponse.getHeaders();
            Iterator<String> it = headers.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next();
                headersMap.put(key, headers.getFirst(key).toString());
            }


            ObjectMapper mapper = new ObjectMapper();

            SunfishResponseData sunfishResponseData = new SunfishResponseData(status, reason, headersMap);
            servletResponse.setHeader(SunfishServices.HEADER_RESPONSE_DATA, mapper.writeValueAsString(sunfishResponseData));

            return httpResponse;

        } catch (JsonProcessingException e) {
            throw new PEPException("Error Processing json parameters", Response.Status.INTERNAL_SERVER_ERROR);
        } catch (ICSPException e) {
            throw new PEPException("Could not process request", e.getStatus());
        } catch (Exception e) {
            throw new PEPException("Internal Error", Response.Status.INTERNAL_SERVER_ERROR);
        }


    }





}
