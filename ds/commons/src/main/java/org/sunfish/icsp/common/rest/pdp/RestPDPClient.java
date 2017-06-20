package org.sunfish.icsp.common.rest.pdp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.Response;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.InvalidRequestException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.SunfishService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class RestPDPClient implements PDPAdapter {

    private WebTarget pdp;
    private static final Logger log = LogManager.getLogger(RestPDPClient.class);

    public RestPDPClient(String resource) {

        Client client = CommonSetup.createClient();
        pdp = client.target(resource);

    }


    @Override
    public AuthorizationResponse authorization(String signature, String reqReference, Request request, String serviceID, String policyType) throws ICSPException{


        log.debug("Issuing Request to {}{}", pdp.getUri(), "/",
                SunfishServices.PDP_PATH_AUTHORIZATION);

        // issue request
        javax.ws.rs.core.Response httpResponse = null;
        try {
            httpResponse = pdp.path(SunfishServices.PDP_PATH_AUTHORIZATION)
                    .request(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)
                    .header(SunfishServices.HEADER_SERVICE, serviceID)
                    .header(SunfishServices.HEADER_POLICY_TYPE, policyType)
                    .post(Entity.entity(request, ExtendedMediaType.APPLICATION_XML_XACML_TYPE));
        } catch(Exception e) {
            log.error("Could not process request", e);
            throw new InvalidRequestException();
        }

        // success?
        if (httpResponse.getStatusInfo().getFamily() != javax.ws.rs.core.Response.Status.Family.SUCCESSFUL) {
            throw new ICSPException("Failed : HTTP error", javax.ws.rs.core.Response.Status.fromStatusCode(httpResponse.getStatus())) {
            };
        }

        // return
        String signatureHeader = httpResponse.getHeaderString(SunfishServices.HEADER_SIGNATURE);
        try {
            Response response = httpResponse.readEntity(Response.class);
            return new AuthorizationResponse(signatureHeader, response);

        } catch(Exception e) {
            log.error("Could not process response", e);
            throw new InvalidRequestException();
        }


    }
}
