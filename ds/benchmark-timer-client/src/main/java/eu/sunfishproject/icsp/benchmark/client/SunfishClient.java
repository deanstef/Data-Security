package eu.sunfishproject.icsp.benchmark.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.binary.Base64;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.mappers.JSONProvider;
import org.sunfish.icsp.common.rest.model.SunfishIssuer;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.model.SunfishService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishClient implements  TimerClient {


    WebTarget sampleResource = null;
    final JSONProvider.JSONMapper mapper = new JSONProvider.JSONMapper();
    final SunfishService srv = new SunfishService();
    final SunfishRequest req = new SunfishRequest();
    final SunfishRequestData data = new SunfishRequestData();
    final SunfishIssuer iss = new SunfishIssuer();

    private static String body = "<note><name>Johne Smith</name><amount>100</amount><currency>EUR</currency></note>";



    public void setup() throws Exception {

        final Client client = CommonSetup.createClient();

        // final WebTarget sampleResource =
        // client.target("http://localhost:8080/pep/v1/");
        sampleResource = client.target(ClientConfig.SRV_PROTO + "://localhost:" + ClientConfig.SRV_PORT);


        // HEADER Service
        srv.setId("dummy");

        // HEADER Request
        req.setMethod("GET");
        req.setPort(ClientConfig.SRV_PORT);
        req.setPath(ClientConfig.SRV_PATH+"1");
        req.setProtocol(ClientConfig.SRV_PROTO);

        final Map<String, String> mt = new HashMap<>();
        mt.put("Accept", ClientConfig.ACCEPTED_TYPE);
        mt.put("Accept-Encoding", ClientConfig.ENCODING);
        data.setHeaders(mt);

        iss.setMethod("jwt");
        iss.setToken(Base64.encodeBase64URLSafeString(CyberHyperSecurityModule.getInstance()
                .genJWT("dummy", "122424", new Date(System.currentTimeMillis() + 300000l)).getBytes()));



    }

    public void run() {

        // post xml
        final Response response;
        try {
            response = sampleResource.path("pep/v1/app-request").request(ExtendedMediaType.APPLICATION_XML_XACML)

                    .header(SunfishServices.HEADER_SERVICE, mapper.stringify(srv))
                    .header(SunfishServices.HEADER_REQUEST, mapper.stringify(req))
                    .header(SunfishServices.HEADER_REQUEST_DATA, mapper.stringify(data))
                    .header(SunfishServices.HEADER_ISSUER, mapper.stringify(iss))

                    .post(Entity.entity(body, ExtendedMediaType.APPLICATION_XML));

            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            } else {
//                System.out.println("Cyber-Request success!");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
    }



}
