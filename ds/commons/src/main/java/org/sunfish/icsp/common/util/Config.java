package org.sunfish.icsp.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static Logger log = LogManager.getLogger(Config.class);
	
	private Properties prop = new Properties();
	private static HashMap<String, Config> instances = new HashMap<>();
	public static final String CONFIG_PARAMETER   = "testenvironment_config_uri";
	public static final String CONF_DIR    = System.getProperty("catalina.base") +File.separator + "conf" + File.separator + "sunfish" + File.separator;
		
	private Config(String absoluteConfigFilePath) throws IOException {
//		String configPath = CONF_DIR + configFilePathReleativeToConfigDir;
		log.info("Using Configuration file:"+absoluteConfigFilePath);
		FileInputStream fis = new FileInputStream(absoluteConfigFilePath);
		prop.load(fis);
		
	}
	public static Config getInstance(String configFilePath, boolean absolutePath) throws IOException
	{
		if(!absolutePath)
			configFilePath = CONF_DIR + configFilePath;
		Config config = null;
		if(instances.containsKey(configFilePath) == false) {
			config = new Config(configFilePath);
			instances.put(configFilePath, config);
		}
		else
			config = instances.get(configFilePath);
		return config;
	}
	
	public static Config getInstance(String configFilePathReleativeToConfigDir) throws IOException
	{
		return getInstance(configFilePathReleativeToConfigDir, false);
	}

	public String getProperty(String key) {
		String property =  prop.getProperty(key);
		return property;
	}

	public String getProperty(String key, String defaultValue) {
		String property =  prop.getProperty(key, defaultValue);
		return property;
	}

	public String[] getArrayForProperty(String key, String defaultValue) {
		String property = prop.getProperty(key, defaultValue);
		return property.split(",");
	}




}
