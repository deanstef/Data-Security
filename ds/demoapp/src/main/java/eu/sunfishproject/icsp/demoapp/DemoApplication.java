package eu.sunfishproject.icsp.demoapp;

import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.swagger.SwaggerUtil;

import javax.ws.rs.ApplicationPath;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@ApplicationPath("")
public class DemoApplication extends CommonSetup {

    public DemoApplication() {
        register(DemoResource.class);
    }


}
