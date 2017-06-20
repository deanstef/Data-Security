package eu.sunfishproject.icsp.proxy.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.pdp.PDPAdapter;
import org.sunfish.icsp.common.rest.pdp.RestPDPClient;
import org.sunfish.icsp.common.util.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class ProxyConfig {


    private Properties properties = new Properties();

    private static ProxyConfig instance = null;
    private static String CONFIG_FILE_NAME = "./proxy.config";

    private final Logger log  = LogManager.getLogger(ProxyConfig.class);


    private ProxyConfig()
    {
        try {

            log.info("Using Configuration file:"+CONFIG_FILE_NAME);
            FileInputStream fis = new FileInputStream(CONFIG_FILE_NAME);
            properties.load(fis);

        } catch(IOException e) {
            log.error("Could not read config: ", e);
            String workingDir = System.getProperty("user.dir");
            log.info("Current working directory : " + workingDir);
            log.info("Using default config instead...");
        }
    }

    public static ProxyConfig getInstance()
    {
        if(instance == null) {
            instance = new ProxyConfig();
        }
        return instance;
    }


    public String getProxyIP() {
        String ip = properties.getProperty("proxy.ip",Constants.IP);
        return ip;
    }


    public int getProxyPort() {
        int port = Integer.parseInt(properties.getProperty("proxy.port", ""+Constants.PORT));
        return port;
    }

    public String getPEPUrl() {
        String url = properties.getProperty("pep.url", Constants.PEP_URL);
        return url;
    }



}
