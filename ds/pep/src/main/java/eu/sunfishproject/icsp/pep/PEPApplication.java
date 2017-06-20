package eu.sunfishproject.icsp.pep;

import eu.sunfishproject.icsp.pep.net.api.v1.PEPResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.swagger.SwaggerUtil;

import javax.ws.rs.ApplicationPath;


/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */

@ApplicationPath("")
public class PEPApplication extends CommonSetup {
    public PEPApplication() {
        // packages(true, getClass().getPackage().getName());

        register(PEPResource.class);

        setupSwagger();

    }


    public void setupSwagger() {
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        String title = "SUNFISH Policy Enforcement Point (PEP) API";
        String description = Documentation.PEP_DESCRIPTION;
        String version = "1.0.0";

        String basePath = "pep/";
        String resourcePackage = "eu.sunfishproject.icsp.pep.net.api.v1,org.sunfish.icsp.common.rest.model";

        String[] schemes = new String[]{"http","https"};
        String email = "dominik.ziegler@a-sit.at";
        String author = "Dominik Ziegler";

        SwaggerUtil.setupSwagger(title, description, version, basePath, resourcePackage, schemes, author, email);


    }

}
