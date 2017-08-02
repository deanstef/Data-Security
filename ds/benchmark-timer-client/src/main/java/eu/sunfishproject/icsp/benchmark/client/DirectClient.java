package eu.sunfishproject.icsp.benchmark.client;

import org.sunfish.icsp.common.rest.CommonSetup;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class DirectClient implements TimerClient {


    WebTarget sampleResource = null;


    public void setup()  {
        final Client client = CommonSetup.createClient();
        sampleResource = client.target(ClientConfig.SRV_PROTO + "://localhost:" + ClientConfig.SRV_PORT + ClientConfig.SRV_PATH);
    }

    public void run() {

        // get xml
        final Response response = sampleResource.path("1").request(ClientConfig.ACCEPTED_TYPE).acceptEncoding(ClientConfig.ENCODING)
                .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

    }

    public void destroy() {
    }

}
