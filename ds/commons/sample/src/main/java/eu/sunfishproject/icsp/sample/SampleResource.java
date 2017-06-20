/**
 * 
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@Path("sampleResource")
public class SampleResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/get")
  public String get() {
    return "HELO";
  }




  @POST
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Path("/post")
  public String post(@QueryParam("param1") String queryParam, @FormParam("formParam1") String formParam) {
    return "Query Param: " + queryParam + ", formParam1: " + formParam;
  }

  @GET
  @Produces(MediaType.APPLICATION_XML)
  @Path("/xml")
  public SampleData xml() {
    SampleData data = new SampleData("Some sample Name");
    return data;
  }

  @POST
  @Produces(MediaType.APPLICATION_XML)
  @Consumes(MediaType.APPLICATION_XML)
  @Path("/postXml")
  public SampleData postXml(SampleData data) {
    data.setName(data.getName() + " and some more data added on teh server side");
    return data;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_XML)
  @Path("/xml2json")
  public SampleData xml2json(SampleData data) {
    return data;
  }

  @POST
  @Produces(MediaType.APPLICATION_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/json2xml")
  public SampleData json2xml(SampleData data) {
    return data;
  }
}