/**
 * 
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sunfish.icsp.common.rest.CommonSetup;

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

    WebTarget sampleResource = client.target("http://localhost:8080/sample/sampleResource");

    // get
    Response response = sampleResource.path("get").request(MediaType.TEXT_PLAIN).get();
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }
    String output = response.readEntity(String.class);
    System.out.print("\nget\nResponse: ");
    System.out.println(output);

    // post
    Form form = new Form();
    form.param("formParam1", "This is the first form parameter");
    response = sampleResource.path("post").queryParam("param1", "query parameter one")
        .request(MediaType.TEXT_PLAIN).post(Entity.form(form));
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }
    output = response.readEntity(String.class);
    System.out.print("\npost\nResponse: ");
    System.out.println(output);

    // get xml
    response = sampleResource.path("xml").request(MediaType.APPLICATION_XML).get();
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }

    SampleData data = response.readEntity(SampleData.class);
    System.out.println("\nxml\nResponse data.name: " + data.getNameData());
    System.out.println("Response data.name: " + data.getName());
    System.out.println("Response data.date: " + data.getDate());

    // post xml
    response = sampleResource.path("postXml").request(MediaType.APPLICATION_XML)
        .post(Entity.xml(new SampleData("from client")));
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }

    data = response.readEntity(SampleData.class);
    System.out.println("\npostXml\nResponse data.name: " + data.getNameData());
    System.out.println("Response data.name: " + data.getName());
    System.out.println("Response data.date: " + data.getDate());

    // xml2json
    response = sampleResource.path("xml2json").request(MediaType.APPLICATION_JSON)
        .post(Entity.xml(new SampleData("from client")));
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }
    
    data = response.readEntity(SampleData.class);
    System.out.println("\nxml2json\nResponse data.name: " + data.getNameData());
    System.out.println("Response data.name: " + data.getName());
    System.out.println("Response data.date: " + data.getDate());

    // json2xml
    response = sampleResource.path("json2xml").request(MediaType.APPLICATION_XML)
        .post(Entity.json(new SampleData("from client")));
    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
    }
    
    data = response.readEntity(SampleData.class);
    System.out.println("\njon2xml\nResponse data.name: " + data.getNameData());
    System.out.println("Response data.name: " + data.getName());
    System.out.println("Response data.date: " + data.getDate());

  }

}
