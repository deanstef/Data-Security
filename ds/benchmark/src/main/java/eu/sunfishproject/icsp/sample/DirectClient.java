/**
 *
 */
package eu.sunfishproject.icsp.sample;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sunfish.icsp.common.rest.CommonSetup;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
public class DirectClient {

  public static final String ACCEPTED_TYPE = MediaType.APPLICATION_XML;
  public static final String SRV_PROTO     = "http";
  public static final String SRV_PATH      = "/resource/id/";
  public static final int    SRV_PORT      = 8080;
  public static final String ENCODING      = "gzip";

  /**
   * @param args
   */
  public static void main(final String[] args) {

    final Client client = CommonSetup.createClient();

    final WebTarget sampleResource = client.target(SRV_PROTO + "://localhost:" + SRV_PORT + SRV_PATH);

    // get xml
    final Response response = sampleResource.path("1").request(ACCEPTED_TYPE).acceptEncoding(ENCODING)
        .get();
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }

  }

}
