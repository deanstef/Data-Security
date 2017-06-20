/**
 *
 */
package eu.sunfishproject.icsp.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Request;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.util.XACMLIO;
import org.sunfish.icsp.common.util.XACMLIO.PolicyReader;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@Path("samplePRP")
public class SamplePRP {

  private static final Logger log = LogManager.getLogger(SamplePRP.class);

  @POST
  @Path("collect")
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  public Object collect(final Request request) throws ICSPException {
    log.debug("Delivering sample policy");
    XACMLIO.PolicyReaderPool policyReaderPool = null;
    Object policy = null;
    PolicyReader reader = null;
    try {
      policyReaderPool = new XACMLIO.PolicyReaderPool(1, false);
      reader = policyReaderPool.acquire();
      policy = reader.readPolicy(getClass().getResourceAsStream("/obligationPolicy.xml"));
    } catch (final Exception e) {
      e.printStackTrace();
    }

    if (policyReaderPool != null && reader != null) {
      policyReaderPool.recycle(reader);
    }
    if (policy == null) {
      throw new ICSPException("Policy could not be read", Status.INTERNAL_SERVER_ERROR) {
      };
    }
    return policy;

  }

}