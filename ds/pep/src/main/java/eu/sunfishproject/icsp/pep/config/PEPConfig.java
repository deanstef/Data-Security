package eu.sunfishproject.icsp.pep.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.pdp.PDPAdapter;
import org.sunfish.icsp.common.rest.pdp.RestPDPClient;
import org.sunfish.icsp.common.rest.pip.PIPAdapter;
import org.sunfish.icsp.common.rest.pip.RestPIP;
import org.sunfish.icsp.common.util.Config;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PEPConfig {


    private static PEPConfig instance = null;
    private static String configFilePathReleativeToConfigDir = "pep.config";
    private static Config config = null;

    private final Logger log  = LogManager.getLogger(PEPConfig.class);
    private final Map<String, PIPAdapter> pips = Collections.synchronizedMap(new HashMap<String, PIPAdapter>());
    private final Map<String, List<String>> userRoles = Collections.synchronizedMap(new HashMap<String, List<String>>());


    // DEBUG
    public static int REQUEST_COUNT = 0;
    // DEBUG END



    private PEPConfig()
    {
        try {
            config = Config.getInstance(configFilePathReleativeToConfigDir);
        } catch(IOException e) {
            log.error("Could not read config: ", e);
        }
    }

    public static PEPConfig getInstance()
    {
        if(instance == null) {
            instance = new PEPConfig();

            readPIPsFromConfig();
            readUserRolesFromConfig();

        }
        return instance;
    }



    public void addPIP(final PIPAdapter adapter) {
        log.debug("Adding PIP {} ({})", adapter.getName(), adapter.getClass());
        pips.put(adapter.getName(), adapter);
    }

    public Collection<PIPAdapter> getPIPs() {
        return new HashSet<>(pips.values());
    }



    public PDPAdapter getPDP() {
        String pdp = config.getProperty("pdp","http://localhost:8080/pdp/");
        return new RestPDPClient(pdp + SunfishServices.PDP_VERSION);
    }

    public String getZone() {
        String zone = config.getProperty("zone","xxx");
        return zone;
    }


    public String getOAServiceUrl() {
        String zone = config.getProperty("oa.service.url","http://localhost:8080/pep");
        return zone;
    }

    public String getDemoServiceUrl() {
        String zone = config.getProperty("demo.service.url","http://localhost:8080/demo-app");
        return zone;
    }



    public List<String> getUserRoles(String bpk) {

        List<String> userRoles = this.userRoles.get(bpk);
        if(userRoles != null) {
            return userRoles;
        }

        userRoles = new ArrayList<>();
        userRoles.add("user");
        return userRoles;
    }


    private static void readPIPsFromConfig() {

        String[] pips = config.getArrayForProperty("pips", "http://localhost:8080/pip/");
        for(String pip : pips) {
            instance.addPIP(new RestPIP(pip + SunfishServices.PIP_VERSION));
        }

    }

    private static void readUserRolesFromConfig() {

        String[] userRolesArray = config.getArrayForProperty("user.roles", "");

        for(String userRole : userRolesArray) {
            String[] keyValue = userRole.split("@");

            if(keyValue.length == 2) {
                String[] userRoles = keyValue[1].split(":");
                instance.userRoles.put(keyValue[0], Arrays.asList(userRoles));
            }
        }

    }




}
