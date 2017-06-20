package eu.sunfishproject.icsp.prp.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.sunfish.icsp.common.rest.SunfishServices;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public class Utils {

	public static String loadResource(String name)
	{
		ClassLoader classLoader = Utils.class.getClassLoader();
		String s = null;
		try {
			s = IOUtils.toString(classLoader.getResourceAsStream(name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	public static String getKey(final String id, final String version) {
		if(version != null && version.length() > 0) {
			return id + SunfishServices.ID_SEPARATOR + version;
		} else {
			return id;
		}
	}

	public static String getKey(PolicyType policy) {
		String id = policy.getPolicyId();
		String version = policy.getVersion();
		return getKey(id, version);
	}

	public static String getKey(PolicySetType policySet) {
		String id = policySet.getPolicySetId();
		String version = policySet.getVersion();
		return getKey(id, version);
	}

	public static String getKey(PolicyDef policyDef) {
		String id = policyDef.getIdentifier().stringValue();
		String version = policyDef.getVersion().getVersion();
		return getKey(id, version);
	}
}
