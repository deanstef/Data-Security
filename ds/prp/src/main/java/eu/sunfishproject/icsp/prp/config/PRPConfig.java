package eu.sunfishproject.icsp.prp.config;

import java.io.File;
import java.io.IOException;

import org.sunfish.icsp.common.util.Config;

public class PRPConfig{

	private static PRPConfig instance = null;
	private static String configFilePathReleativeToConfigDir = "prp.config";
	private static Config config = null;
	
	private PRPConfig() throws IOException
	{
		config = Config.getInstance(configFilePathReleativeToConfigDir);
	}
	
	public static PRPConfig getInstance() throws IOException
	{
		if(instance == null)
			instance = new PRPConfig();
		return instance;
	}

//	public String getRootPolicyDirectory() {
//		String dir =  config.getProperty("rootPolicy.dir","G:\\Projekte\\SunFISH\\workspace\\parent\\prp\\src\\main\\resources\\Policies\\");
//		return dir;
//	}
	
//	public Integer getMaxPolicyCount() {
//		String count =  config.getProperty("maxPolicyCount","1000");
//		return Integer.parseInt(count);
//	}
	
	public Integer getMaxThreads() {
		String count =  config.getProperty("maxThreads","4");
		return Integer.parseInt(count);
	}
	
	public String getRIUrl() {
        String riUrl =  config.getProperty("ri","http://localhost:8080/ri/mocked");//FIXME
        return riUrl;
    }
}
