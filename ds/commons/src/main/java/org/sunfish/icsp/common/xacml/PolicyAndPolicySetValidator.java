package org.sunfish.icsp.common.xacml;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.SunfishServices;

public class PolicyAndPolicySetValidator {

	private static final Logger log = LogManager.getLogger(PolicyAndPolicySetValidator.class);
	public static final boolean OK = false;
	public static final boolean NOK = !OK;
	
	public static boolean validate(PolicyType policy)
	{
		if(isIdValid(policy.getPolicyId()) == NOK)
		{
			log.debug("policy id not OK:"+policy.getPolicyId());
			return NOK;
		}
		if(isVersionValid(policy.getVersion()) == NOK)
		{
			log.debug("policy version not OK:"+policy.getVersion());
			return NOK;
		}

		return OK;
	}

	public static boolean validate(PolicySetType policySet)
	{
		if(isIdValid(policySet.getPolicySetId()) == NOK)
			return NOK;
		if(isVersionValid(policySet.getVersion()) == NOK)
			return NOK;
		
		return OK;
	}


	private static boolean isIdValid(String id)
	{
		if(id.contains(SunfishServices.ID_SEPARATOR))
			return NOK;
		//TODO check if id contains chars that are not allowed in filenames
		//TODO check urn?
		return OK;
	}

	public static boolean isVersionValid(String version)
	{	
		try
		{
			Float f = Float.parseFloat(version);
			String text = f.toString();
			int decimalLength = text.length() - text.indexOf('.') - 1;
			if(f < 0)
				return NOK;
			if(decimalLength > 2)
				return NOK;

			return OK;
		}catch(Exception e)
		{
			return NOK;
		}
	}
	
	
}
