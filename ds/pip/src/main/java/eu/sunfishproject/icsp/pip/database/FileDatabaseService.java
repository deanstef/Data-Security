package eu.sunfishproject.icsp.pip.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * Created by dziegler on 18/10/2016.
 */
public class FileDatabaseService implements DatabaseService {

    private static FileDatabaseService instance = null;
    private Properties prop = new Properties();


    public static final String CONF_DIR = System.getProperty("catalina.base") + File.separator + "conf" +
            File.separator + "sunfish" + File.separator + "pip" + File.separator +  "database";
    public static final String DATABASE_NAME = "pip_database.config";

    private final Logger log  = LogManager.getLogger(FileDatabaseService.class);




    private FileDatabaseService()
    {
        try {
            String filePath = CONF_DIR + File.separator +  DATABASE_NAME;
            FileInputStream fis = new FileInputStream(filePath);
            prop.load(fis);
        } catch(IOException e) {
            log.error("Could not read config: ", e);
        }
    }

    public static FileDatabaseService getInstance()
    {
        if(instance == null) {
            instance = new FileDatabaseService();
        }
        return instance;
    }


    @Override
    public String getHostForServiceId(String id) {
        String key = "host." + sanitizeId(id);
        return prop.getProperty(key, "");
    }

    @Override
    public String getZoneForServiceId(String id) {
        String key = "zone." + sanitizeId(id);
        return prop.getProperty(sanitizeId(key), "");
    }

    @Override
    public String getPEPForZone(String id) {
        String key = "pep." + sanitizeId(id);
        return prop.getProperty(key,  "");
    }

    private String sanitizeId(String id) {
        if(id == null) {
            return null;
        }

        return id.replaceAll(":", ".");

    }


}
