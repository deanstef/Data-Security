/**
 *
 */
package eu.sunfishproject.icsp.pdp.net;

import java.io.IOException;

import javax.ws.rs.ApplicationPath;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import org.apache.logging.log4j.LogManager;
import org.sunfish.icsp.common.rest.CommonSetup;

import eu.sunfishproject.icsp.pdp.PDPConfig;
import org.sunfish.icsp.common.rest.pip.RestPIP;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.swagger.SwaggerUtil;
import org.sunfish.icsp.common.util.Config;

import eu.sunfishproject.icsp.pdp.RestPRP;
import eu.sunfishproject.icsp.pdp.net.api.v1.PDPResource;
import eu.sunfishproject.icsp.sample.SamplePRP;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@ApplicationPath("")
public class PDPApplication extends CommonSetup {

  private static final String PDP_CONF = "pdp.config";
  private static final String PRPS     = "prps";
  private static final String PIPS     = "pips";

  public PDPApplication() {

    try {
      final Config config = Config.getInstance(PDP_CONF);

      for (final String prp : config.getArrayForProperty(PRPS, "http://localhost:8080/pdp/samplePRP")) {
      LogManager.getLogger().info("Adding PRP {} to PDP",prp);
        PDPConfig.addPRP(new RestPRP(prp));
      }

      for (final String pip : config.getArrayForProperty(PIPS, "http://localhost:8080/pip/v1")) {
        LogManager.getLogger().info("Adding PIP {} to PDP",pip);
        PDPConfig.addPIP(new RestPIP(pip));
      }

    } catch (final IOException e) {
      e.printStackTrace();
    }
    // packages(true, getClass().getPackage().getName());
    // PDPConfig.addPRP(new
    // RestPRP("http://mopsos:8080/"+SunfishServices.PRP_ENDPOINT));
    // PDPConfig.addPRP(new RestPRP("http://localhost:8080/pdp/samplePRP"));
    // PDPConfig.addPRP(new RestPRP("http://shared.iaik.tugraz.at:80/prp/v1"));
    // PDPConfig.addPRP(new PIPConnector());
    // register(SamplePIP1.class);
    // register(SamplePIP2.class);

    register(PDPResource.class);
    register(SamplePRP.class);

    setupSwagger();

  }



  public void setupSwagger() {
    register(io.swagger.jaxrs.listing.ApiListingResource.class);
    register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

    String title = "SUNFISH Policy Decision Point (PDP) API";
    String description = Documentation.PDP_DESCRIPTION;
    String version = "1.0.0";

    String basePath = "pdp/";
    String resourcePackage = "eu.sunfishproject.icsp.pdp.net.api.v1,org.sunfish.icsp.common.rest.model";

    String[] schemes = new String[]{"http","https"};
    String email = "bernd.pruenster@a-sit.at";
    String author = "Bernd Prünster";

    SwaggerUtil.setupSwagger(title, description, version, basePath, resourcePackage, schemes, author, email);

  }



}
