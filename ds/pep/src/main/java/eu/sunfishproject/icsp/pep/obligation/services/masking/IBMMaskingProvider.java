package eu.sunfishproject.icsp.pep.obligation.services.masking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class IBMMaskingProvider implements MaskingProvider {


    Logger log = LogManager.getLogger(IBMMaskingProvider.class);

    private final WebTarget maskingService;
    private final String resource;


    private static final String PATH_PROCESS = "process";



    public IBMMaskingProvider(final String resource) {
        this.resource = resource;
        final Client client = CommonSetup.createClient();
        maskingService = client.target(resource);
    }


    @Override
    public byte[] mask(final byte[] content, MaskingDataType dataType, String policyId, String maskingContext) throws ICSPException {


        log.info("Issuing Request to {}{}",maskingService.getUri() +"/" + PATH_PROCESS);

        // issue request
        final Response response;

        String mediaType = getMediaTypeForMaskingDataType(dataType);

        response = maskingService.path(PATH_PROCESS)
                .queryParam("policy", policyId)
                .queryParam("context", maskingContext)
                .queryParam("encrypt", true)
                .request(mediaType)
                .post(Entity.entity(content, mediaType));

        // success?
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
            };
        }

        return response.readEntity(byte[].class);

    }


    @Override
    public byte[] unmask(final byte[] content, MaskingDataType dataType, String policyId, String maskingContext) throws ICSPException {


        log.info("Issuing Request to {}{}",maskingService.getUri() +"/" + PATH_PROCESS);

        // issue request
        final Response response;

        String mediaType = getMediaTypeForMaskingDataType(dataType);

        response = maskingService.path(PATH_PROCESS)
                .queryParam("policy", policyId)
                .queryParam("context", maskingContext)
                .queryParam("decrypt", true)
                .request(mediaType)
                .post(Entity.entity(content, mediaType));

        // success?
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(response.getStatus())) {
            };
        }

        return response.readEntity(byte[].class);

    }



    private String getMediaTypeForMaskingDataType(MaskingDataType dataType) {


        String mediaType = null;
        switch (dataType) {
            case TYPE_JSON:
                mediaType = MediaType.APPLICATION_JSON;
                break;
            case TYPE_TEXT:
                mediaType = MediaType.TEXT_PLAIN;
                break;
            case TYPE_XML:
                mediaType = MediaType.APPLICATION_XML;
                break;
            default:

        }

        return mediaType;


    }



}
