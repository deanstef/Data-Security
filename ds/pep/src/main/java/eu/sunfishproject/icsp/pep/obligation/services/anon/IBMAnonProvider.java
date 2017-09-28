package eu.sunfishproject.icsp.pep.obligation.services.anon;

import com.fasterxml.jackson.databind.JsonNode;
import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingDataType;
import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.ObligationException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.mappers.JSONProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class IBMAnonProvider implements AnonProvider {


    Logger log = LogManager.getLogger(IBMAnonProvider.class);

    private final WebTarget anonService;
    private final String    resource;


    private static final String PATH_FILE = "file";
    private static final String PATH_ANON = "micro";


    public IBMAnonProvider(final String resource) {
        this.resource = resource;
        final Client client = CommonSetup.createClient();
        anonService = client.target(resource);
    }


    @Override
    public byte[] anon(final byte[] content, MaskingDataType dataType, String configId) throws ICSPException {


        log.info("Issuing Request to {}{}", anonService.getUri() + "/" + PATH_FILE);

        // issue request
        final Response uploadResponse;

        MultiPart upload = new MultiPart();
        upload.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        StreamDataBodyPart body = new StreamDataBodyPart("file", new ByteArrayInputStream(content),
                "data.txt", MediaType.valueOf(getMediaTypeForMaskingDataType(dataType)));
        upload.bodyPart(body);


        uploadResponse = anonService.path(PATH_FILE)
                .request(MediaType.MULTIPART_FORM_DATA).accept(MediaType.WILDCARD)
                .post(Entity.entity(upload, upload.getMediaType()));

        // success?
        if (uploadResponse.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(uploadResponse.getStatus())) {
            };
        }

        JSONProvider.JSONMapper jsonMapper = new JSONProvider.JSONMapper();
        String                  key;
        try {
            JsonNode node = jsonMapper.readValue(uploadResponse.readEntity(byte[].class), JsonNode.class);
            key = node.get("key").asText();
        } catch (IOException e) {
            throw new ICSPException("Parsing File Upload Respomnse Failed : HTTP error", e, Response.Status.INTERNAL_SERVER_ERROR) {
            };
        }


        final Response anonResponse = anonService.path(PATH_ANON).queryParam("configuration", configId).queryParam("file", key).request().post(null);

        if (anonResponse.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(anonResponse.getStatus())) {
            };
        }


        try {
            JsonNode node = jsonMapper.readValue(anonResponse.readEntity(byte[].class), JsonNode.class);
            key = node.get("key").asText();
        } catch (IOException e) {
            throw new ICSPException("Parsing File Upload Respomnse Failed : HTTP error", e, Response.Status.INTERNAL_SERVER_ERROR) {
            };
        }

        final Response file = anonService.path(PATH_FILE).path(key).request().get();

        if (file.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new ICSPException("Failed : HTTP error", Response.Status.fromStatusCode(file.getStatus())) {
            };
        }

        return file.readEntity(byte[].class);

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
