package eu.sunfishproject.icsp.prp;

import java.io.IOException;

import javax.ws.rs.ApplicationPath;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import org.sunfish.icsp.common.rest.CommonSetup;
import eu.sunfishproject.icsp.prp.api.v1.V1;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.swagger.SwaggerUtil;


@ApplicationPath("")
public class Init extends CommonSetup {
  public Init() throws IOException {
    register(AuthenticationtFilter.class);
    register(V1.class);
   
    setupSwagger();

  }

  public void setupSwagger() {
    register(io.swagger.jaxrs.listing.ApiListingResource.class);
    register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

    String title = "SUNFISH Policy Retrieval Point (PRP) API";
    String description = Documentation.PRP_DESCRIPTION;
    String version = "1.0.0";

    String basePath = "prp/";
    String resourcePackage = "eu.sunfishproject.icsp.prp.api.v1";

    String[] schemes = new String[]{"http","https"};
    String email = "alexander.marsalek@a-sit.at";
    String author = "Alexander Marsalek";

    SwaggerUtil.setupSwagger(title, description, version, basePath, resourcePackage, schemes, author, email);

  }

}
