package eu.sunfishproject.icsp.pip;

import eu.sunfishproject.icsp.pip.net.api.v1.PIPResource;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.swagger.SwaggerUtil;

import javax.ws.rs.ApplicationPath;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@ApplicationPath("")
public class PIPApplication extends CommonSetup {

    public PIPApplication() {
        // packages(true, getClass().getPackage().getName());

        register(PIPResource.class);

        setupSwagger();

    }

    public void setupSwagger() {
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        String title = "SUNFISH Policy Information Point (PIP) API";
        String description = Documentation.PIP_DESCRIPTION;
        String version = "1.0.0";

        String basePath = "pip/";
        String resourcePackage = "eu.sunfishproject.icsp.pip.net.api.v1";

        String[] schemes = new String[]{"http","https"};
        String email = "dominik.ziegler@a-sit.at";
        String author = "Dominik Ziegler";

        SwaggerUtil.setupSwagger(title, description, version, basePath, resourcePackage, schemes, author, email);

    }

}