/**
 *
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.mappers.JSONProvider.JSONMapper;
import org.sunfish.icsp.common.rest.model.SunfishIssuer;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@Path("1")
public class SampleResource {

  final JSONMapper mapper = new JSONMapper();

  @GET
  @Produces(MediaType.APPLICATION_XML)
  public String get() {
    return "<note><name>Johne Smith</name><amount>100</amount><currency>EUR</currency></note>";
  }

  @POST
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  public String demo(@HeaderParam(SunfishServices.HEADER_ISSUER) final String issuer) throws Exception {
    final SunfishIssuer iss = mapper.readValue(issuer, SunfishIssuer.class);
    CyberHyperSecurityModule.getInstance().validateJWS(new String(Base64.decodeBase64(iss.getToken())));
    return "";
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/json")
  public String json() {

    return "{\"phones\":{\"home\":\"(855) 003-8973\",\"work\":\"(456) 194-3754\",\"mobile\":\"(395) 383-3875\"},\"email\":\"my.example@fake.com\"}";

  }


}