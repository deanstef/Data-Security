/**
 * 
 */
package prp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.xacml.generated.ObjectFactory;
import org.sunfish.icsp.common.xacml.generated.PoliciesType;

import eu.sunfishproject.icsp.prp.util.Utils;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
public class SampleClient {

  /**
   * @param args
   */
  public static void main(String[] args) {

    Client client = CommonSetup.createClient();

    WebTarget sampleResource = client.target("http://localhost:8080/prp/v1/policies");
    Response response = null;
    String output = null;
    // post
    String entity = Utils.loadResource("Policies/IIC029Policy.xml");
    PoliciesType tmp = new PoliciesType();
    tmp.setRootPolicy(true);

	response = sampleResource//.path("post")//.queryParam("param1", "query parameter one")
			.request()
			.header("Content-Type", "application/xml+xacml").header(SunfishServices.HEADER_ISSUER, "Admin")
       // .request(ExtendedMediaType.APPLICATION_XML_XACML)
        .post(Entity.xml(entity ));
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }
    output = response.readEntity(String.class);
    System.out.print("\npost\nResponse: ");
    System.out.println(output);


  }

}
