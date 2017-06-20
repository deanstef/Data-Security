package prp;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.sunfish.icsp.common.rest.SunfishServices;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public class Utils {

	public static String loadResource(String name)
	{
		ClassLoader classLoader = Test.class.getClassLoader();
		String s = null;
		try {
			s = IOUtils.toString(classLoader.getResourceAsStream(name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	
}
