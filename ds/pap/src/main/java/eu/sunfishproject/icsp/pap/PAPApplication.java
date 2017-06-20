package eu.sunfishproject.icsp.pap;

import eu.sunfishproject.icsp.pap.net.api.v1.PAPResource;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.swagger.SwaggerUtil;

import javax.ws.rs.ApplicationPath;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@ApplicationPath("")
public class PAPApplication extends CommonSetup {

    public PAPApplication() {
        // packages(true, getClass().getPackage().getName());

        register(PAPResource.class);

        setupSwagger();

    }

    public void setupSwagger() {
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        String title = "SUNFISH Policy Administration Point (PAP) API";
        String description = Documentation.PAP_DESCRIPTION;
        String version = "1.0.0";

        String basePath = "pap/";
        String resourcePackage = "eu.sunfishproject.icsp.pap.net.api.v1";

        String[] schemes = new String[]{"http","https"};
        String email = "alexander.marsalek@a-sit.at";
        String author = "Alexander Marsalek";

        SwaggerUtil.setupSwagger(title, description, version, basePath, resourcePackage, schemes, author, email);

    }


}
