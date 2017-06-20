/**
 *
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.dom.DOMRequest;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorSet;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@Path("samplePIP2")
public class SamplePIP2 {

  private static final Logger log = LogManager.getLogger(SamplePRP.class);

  @POST
  @Path(SunfishServices.PIP_PATH_REQUEST)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  public Request request(final SunfishPIPRequest request) throws ICSPException {
    log.debug("Delivering sample PIP-enhanced request");
    try {
      return DOMRequest.load(getClass().getResourceAsStream("/requests/samplePermitRequest.xml"));
    } catch (final DOMStructureException e) {
      throw new ICSPException("Sampe Request could not be read", Status.INTERNAL_SERVER_ERROR) {
      };
    }
  }

  @GET
  @Path(SunfishServices.PIP_PATH_COLLECT)
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  public AttributeDesignatorSet collect(){
    final AttributeDesignatorSet attrs = new AttributeDesignatorSet();
    final AttributeDesignator des = new AttributeDesignator();
    des.setAttributeId(new IdentifierImpl("urn:oasis:names:tc:xacml:2.0:conformance-test:some-attribute"));
    des.setCategory(new IdentifierImpl("urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"));
    des.setDataTypeId(new IdentifierImpl("http://www.w3.org/2001/XMLSchema#string"));
    attrs.add(des);
    return attrs;
  }
}