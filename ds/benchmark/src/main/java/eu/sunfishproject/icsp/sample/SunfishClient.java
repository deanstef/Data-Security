/**
 *
 */
package eu.sunfishproject.icsp.sample;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.std.dom.DOMRequest;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.mappers.JSONProvider.JSONMapper;
import org.sunfish.icsp.common.rest.model.SunfishIssuer;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.util.CommonUtil;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
public class SunfishClient {


  private static Request request;
  static {
    try {
      request = DOMRequest.load(ClassLoader.getSystemResourceAsStream("AuthRequest.xml"));
    } catch (final DOMStructureException e) {
      e.printStackTrace();
    }
  }

  private static String body = "<note><name>Johne Smith</name><amount>100</amount><currency>EUR</currency></note>";

  /**
   * @param args
   * @throws Exception
   */
  public static void main(final String[] args) throws Exception {

    final Client client = CommonSetup.createClient();

    // final WebTarget sampleResource =
    // client.target("http://localhost:8080/pep/v1/");
    final WebTarget sampleResource = client
        .target(DirectClient.SRV_PROTO + "://localhost:" + DirectClient.SRV_PORT);
    final JSONMapper mapper = new JSONMapper();

    // HEADER Service
    final SunfishService srv = new SunfishService() {
    };
    srv.setId("dummy");

    // HEADER Request
    final SunfishRequest req = new SunfishRequest();
    req.setMethod("GET");
    req.setPort(DirectClient.SRV_PORT);
    req.setPath(DirectClient.SRV_PATH+"1");
    req.setProtocol(DirectClient.SRV_PROTO);

    final SunfishRequestData data = new SunfishRequestData();
    final Map<String, String> mt = new HashMap<>();
    mt.put("Accept", DirectClient.ACCEPTED_TYPE);
    mt.put("Accept-Encoding", DirectClient.ENCODING);
    data.setHeaders(mt);

    final SunfishIssuer iss = new SunfishIssuer();
    iss.setMethod("jwt");
    iss.setToken(Base64.encodeBase64URLSafeString(CyberHyperSecurityModule.getInstance()
        .genJWT("dummy", "122424", new Date(System.currentTimeMillis() + 300000l)).getBytes()));

    // post xml
    final Response response = sampleResource.path("pep/v1/app-request").request(ExtendedMediaType.APPLICATION_XML_XACML)

        .header(SunfishServices.HEADER_SERVICE, mapper.stringify(srv))
        .header(SunfishServices.HEADER_REQUEST, mapper.stringify(req))
        .header(SunfishServices.HEADER_REQUEST_DATA, mapper.stringify(data))
        .header(SunfishServices.HEADER_ISSUER, mapper.stringify(iss))

        .post(Entity.entity(body, ExtendedMediaType.APPLICATION_XML));
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    } else {
      System.out.println("Cyber-Request success!");
    }
  }

}
