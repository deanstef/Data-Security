package eu.sunfishproject.icsp.pap.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.util.Config;

import java.io.IOException;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PAPConfig {

    private static PAPConfig instance = null;
    private static String configFilePathReleativeToConfigDir = "pap.config";
    private static Config config = null;

    private final Logger log  = LogManager.getLogger(PAPConfig.class);

    private PAPConfig()
    {
        try {
            config = Config.getInstance(configFilePathReleativeToConfigDir);
        } catch(IOException e) {
            log.error("Could not read config: ", e);
        }
    }

    public static PAPConfig getInstance()
    {
        if(instance == null) {
            instance = new PAPConfig();

        }
        return instance;
    }


    public String getRIUrl() {
        String riUrl =  config.getProperty("ri","http://localhost:8080/ri/mocked");
        return riUrl;
    }


    public String getRIToken() {
        String token = config.getProperty("token", "1");
        return token;
    }

    public String getRIRequestorID() {
        String requestorID = config.getProperty("requestorID", "PAP");
        return requestorID;
    }


}

