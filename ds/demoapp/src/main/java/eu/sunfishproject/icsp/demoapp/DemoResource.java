package eu.sunfishproject.icsp.demoapp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.session.UserSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Path("/")
public class DemoResource {

  private static final List<JsonNode> rxData = new LinkedList<>();

  private static final Logger         log    = LogManager.getLogger(DemoResource.class);

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/hello")
  public String hello() {
    return "HELLO SUNFISH";
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/")
  public Response sunfish(@HeaderParam(SunfishServices.HEADER_USER_SESSION) final String userSessionString) {

    if (userSessionString != null) {

      final InputStream inputStream = DemoResource.class.getClassLoader().getResourceAsStream("demo.html");

      try {
        final ObjectMapper objectMapper = new ObjectMapper();
        final UserSession userSession = objectMapper.readValue(
            Base64.decodeBase64(userSessionString.getBytes(StandardCharsets.UTF_8)), UserSession.class);

        final byte[] content = IOUtils.toByteArray(inputStream);

        final String contentString = new String(content, StandardCharsets.UTF_8)
            .replace("{first.name}", userSession.getFirstName())
            .replace("{last.name}", userSession.getLastName())
            .replace("{birthday}", userSession.getBirthday())
            .replace("{user.role}",
                userSession.getUserRoles().toString().replace("[", "").replace("]", "").trim())
            .replace("{user.bpk}", userSession.getBpk())
            .replace("{user.issuing.nation}", userSession.getIssuingNation());

        return Response.ok(contentString).build();

      } catch (final IOException e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      } finally {
        try {
          inputStream.close();
        } catch (final Exception e) {

        }
      }

    } else {

      return Response.status(Response.Status.FORBIDDEN).entity("NOT Authorized!").build();

    }

  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/dm/{name}")
  public JsonNode dm(@PathParam("name") final String name) throws NoSuchAlgorithmException {
    final ObjectMapper om = new ObjectMapper();
    final ObjectNode node = om.createObjectNode();
    final ObjectNode business = om.createObjectNode();
    final ObjectNode dataSub = om.createObjectNode();
    final ObjectNode dProvDetails = om.createObjectNode();
    node.put("@SubType", "0");
    node.put("@XLVersion", "1.0");
    node.put("@XLName", "Users");
    node.put("@XLGuid", "e3ece4f7-8f0a-4622-a281-78f2f1d91040");
    final MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(name.getBytes());
    final byte[] digest = md5.digest();
    final String s = new BigInteger(digest).toString();
    dProvDetails.put("name", name);
    dProvDetails.put("phone", "(" + s.charAt(8) + "5" + s.charAt(9) + ") 1" + s.charAt(10) + "4-"
        + s.charAt(11) + "7" + s.charAt(12) + "4");
    dProvDetails.put("address1", "" + s.charAt(1) + s.charAt(2) + "/" + s.charAt(3));
    final String string = TimeZone.getAvailableIDs()[(s.charAt(7)*s.charAt(6)) % TimeZone.getAvailableIDs().length];
    if (string.contains(("/"))) {
      final String[] split = string.split(Pattern.quote("/"));
      dProvDetails.put("address2", split[0] + " Street");
      dProvDetails.put("address3", split[1]);
    } else {
      dProvDetails.put("address2", string + " Street");
    }
    dProvDetails.put("email", name + "@fake.com");

    dataSub.set("data_provider_details", dProvDetails);
    dataSub.set("fee_details", om.createArrayNode());
    business.set("private_schools_data_submission", dataSub);
    node.set("business", business);

    return node;
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/dum")
  public String dm(final JsonNode input) {
    synchronized (rxData) {
      rxData.add(input);
    }
    return "OK";
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/list")
  public List<JsonNode> list() {
    return rxData;
  }

  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  @Path("/list")
  public String delete() {
    synchronized (rxData) {
      rxData.clear();
    }
    return "OK";
  }

}
