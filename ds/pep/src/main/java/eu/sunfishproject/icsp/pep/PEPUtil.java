package eu.sunfishproject.icsp.pep;

import eu.sunfishproject.icsp.pep.attributes.RequestAttributeEnhancer;
import org.sunfish.icsp.common.session.UserSession;
import org.apache.openaz.xacml.api.Request;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.model.SunfishHttpObject;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishResponse;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.util.CommonUtil;
import org.sunfish.icsp.common.xacml.XACMLConstants;
import org.sunfish.icsp.common.xacml.XACMLRequestBuilder;
import org.sunfish.icsp.common.xacml.exceptions.XACMLBuilderException;

import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PEPUtil {



    public static Response buildHttpResponse(final SunfishHttpObject sunfishHttpObject) {


        final javax.ws.rs.core.Response.ResponseBuilder responseBuilder = javax.ws.rs.core.Response.status(sunfishHttpObject.getResponseCode());


        final Iterator<String> it = sunfishHttpObject.getHeaders().keySet().iterator();
        while(it.hasNext()) {
            final String key = it.next();
            if(!key.equals("Content-Length") && !key.equals("Transfer-Encoding")) {
                final List<Object> headers = sunfishHttpObject.getHeaders().get(key);
                for (final Object header : headers) {
                    responseBuilder.header(key, header);
                }
            }
        }

// Already set with headers
//        Iterator<String> cookieIterator = sunfishHttpObject.getCookies().keySet().iterator();
//
//        while(cookieIterator.hasNext()) {
//            String key = it.next();
//            NewCookie cookie = sunfishHttpObject.getCookies().get(key);
//            responseBuilder.cookie(cookie);
//        }


        responseBuilder.entity(sunfishHttpObject.getBody());


        return responseBuilder.build();

    }




    public static SunfishHttpObject buildSunfishHttpObject(final javax.ws.rs.core.Response response) {



        final SunfishHttpObject sunfishHttpObject = new SunfishHttpObject();

        sunfishHttpObject.setResponseCode(response.getStatus());

        sunfishHttpObject.setHeaders(response.getHeaders());
        sunfishHttpObject.setCookies(response.getCookies());
        sunfishHttpObject.setBody(response.readEntity(byte[].class));

        return sunfishHttpObject;

    }


    public static Request buildPDPAuthorization(final SunfishRequest sunfishRequest, final SunfishService sunfishService, final SunfishHttpObject sunfishHttpObject, final UserSession userSession) throws ICSPException, XACMLBuilderException {


        XACMLRequestBuilder xacml = new XACMLRequestBuilder()
                .setCategory(XACMLConstants.CATEGORY_SERVICE)
                .addStringAttribute(XACMLConstants.ATTRIBUTE_ID, sunfishService.getId())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_HOST, sunfishService.getHost())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_ZONE, sunfishService.getZone())
                .setCategory(XACMLConstants.CATEGORY_APPLICATION)
                .addStringAttribute(XACMLConstants.ATTRIBUTE_ID, "TBD?!!")
                .addStringAttribute(XACMLConstants.ATTRIBUTE_APPLICATION_ZONE, "MEF") // TODO: JUST FOR DEBUG
                .addStringAttribute(XACMLConstants.ATTRIBUTE_APPLICATION_HOST, "TBD?!!")
                .setCategory(XACMLConstants.CATEGORY_REQUEST)
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_METHOD, sunfishRequest.getMethod())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_HOST, sunfishService.getHost())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_PATH, sunfishRequest.getPath())
                .addIntegerAttribute(XACMLConstants.ATTRIBUTE_REQUEST_PORT, sunfishRequest.getPort())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_PROTOCOL, sunfishRequest.getProtocol())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_CONTENT_TYPE, sunfishRequest.getContentType());


        if(userSession != null) {
            List<String> userRoles = userSession.getUserRoles();
            xacml = xacml.setCategory(XACMLConstants.CATEGORY_IDENTITY)
                    .addStringAttributes(XACMLConstants.ATTRIBUTE_TENANT_ROLE, userRoles);
        }


        final List<String> headerParameters = sunfishRequest.getHeaderParameters();
        for(final String headerParam : CommonUtil.emptyIfNull(headerParameters)) {
            xacml = xacml.addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_HEADER_PARAMETER, headerParam);
        }



        Request request = xacml.build();


        final String[] missingAttributes = new String[]{
                XACMLConstants.ATTRIBUTE_REQUEST_BODY_DATA,
                XACMLConstants.ATTRIBUTE_REQUEST_HEADER_PARAMETER,
                XACMLConstants.ATTRIBUTE_REQUEST_CONTENT_TYPE};



        request = RequestAttributeEnhancer.enhanceRequest(request, sunfishHttpObject, missingAttributes);



        return request;

    }


    public static Request buildPDPAuthorization(final SunfishResponse sunfishResponse, final SunfishService sunfishService, final SunfishRequest originalRequest, final SunfishHttpObject sunfishHttpObject, final UserSession userSession) throws ICSPException, XACMLBuilderException {


        XACMLRequestBuilder xacml = new XACMLRequestBuilder()
                .setCategory(XACMLConstants.CATEGORY_SERVICE)
                .addStringAttribute(XACMLConstants.ATTRIBUTE_ID, sunfishService.getId())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_HOST, sunfishService.getHost())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_ZONE, sunfishService.getZone())
                .setCategory(XACMLConstants.CATEGORY_APPLICATION)
                .addStringAttribute(XACMLConstants.ATTRIBUTE_ID, "TBD?!!")
                .addStringAttribute(XACMLConstants.ATTRIBUTE_APPLICATION_ZONE, "MEF") // TODO: JUST FOR DEBUG
                .addStringAttribute(XACMLConstants.ATTRIBUTE_APPLICATION_HOST, "TBD?!!")
                .setCategory(XACMLConstants.CATEGORY_RESPONSE)
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_METHOD, originalRequest.getMethod())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_REQUEST_PATH, originalRequest.getPath())
                .addIntegerAttribute(XACMLConstants.ATTRIBUTE_RESPONSE_CODE, sunfishResponse.getResponseCode())
                .addStringAttribute(XACMLConstants.ATTRIBUTE_RESPONSE_ENFORCED_ACTION, sunfishResponse.getEnforcedAction());

        final List<String> headerParameters = sunfishResponse.getHeaderParameters();
        for(final String headerParam : CommonUtil.emptyIfNull(headerParameters)) {
            xacml = xacml.addStringAttribute(XACMLConstants.ATTRIBUTE_RESPONSE_HEADER_PARAMETER, headerParam);
        }

        if(userSession != null) {
            List<String> userRoles = userSession.getUserRoles();
            xacml = xacml.setCategory(XACMLConstants.CATEGORY_IDENTITY)
                    .addStringAttributes(XACMLConstants.ATTRIBUTE_TENANT_ROLE, userRoles);
        }


        Request request = xacml.build();



        final String[] missingAttributes = new String[]{
                XACMLConstants.ATTRIBUTE_RESPONSE_BODY_DATA,
                XACMLConstants.ATTRIBUTE_RESPONSE_HEADER_PARAMETER,
                XACMLConstants.ATTRIBUTE_RESPONSE_CONTENT_TYPE};

        request = RequestAttributeEnhancer.enhanceRequest(request, sunfishHttpObject, missingAttributes);



        return request;

    }







}
