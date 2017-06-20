package org.sunfish.icsp.common.rest.pep;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.rest.model.SunfishSignature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class RestPEP implements PEPAdapter {


    private Logger log = LogManager.getLogger(RestPEP.class);


    private final String resource;
    private final WebTarget pep;

    public RestPEP(final String resource) {
        this.resource = resource;
        final Client client = CommonSetup.createClient();
        pep = client.target(resource);
    }


    @Override
    public String getName() {
        return resource;
    }

    @Override
    public Response request(String issuer, SunfishService service, SunfishRequest request, String requestParams,
                            SunfishRequestData requestData, SunfishSignature signature, byte[] body) throws ICSPException {



        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);


        log.debug("Issuing Request to {}{}",pep.getUri(), "/",SunfishServices.PEP_PATH_REQUEST);

        // issue request
        final Response response;
        try {
            Invocation.Builder requestBuilder = pep.path(SunfishServices.PEP_PATH_REQUEST)
                                        .request(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)
                                        .header(SunfishServices.HEADER_ISSUER, issuer)
                                        .header(SunfishServices.HEADER_SERVICE, mapper.writeValueAsString(service))
                                        .header(SunfishServices.HEADER_REQUEST, mapper.writeValueAsString(request));

            if(requestParams != null) {
                requestBuilder.header(SunfishServices.HEADER_REQUEST_PARAMETERS, mapper.writeValueAsString(requestParams));
            }


            if(requestData != null) {
                requestBuilder.header(SunfishServices.HEADER_REQUEST_DATA, mapper.writeValueAsString(requestData));
            }
            if(signature != null) {
                requestBuilder.header(SunfishServices.HEADER_SIGNATURE, mapper.writeValueAsString(signature));
            }

            response = requestBuilder.post(Entity.entity(body, ExtendedMediaType.WILDCARD));

            // success?
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
                };
            }

            return response;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("Could not serialize header:", e);
        }

        return null;




    }


}
