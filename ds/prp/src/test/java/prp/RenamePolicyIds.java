package prp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.dom.DOMPolicyDef;
import org.apache.openaz.xacml.util.XACMLPolicyAggregator;
import org.apache.openaz.xacml.util.XACMLPolicyScanner;
import org.apache.openaz.xacml.util.XACMLPolicyWriter;
import org.sunfish.icsp.common.exceptions.ConflictException;
import org.sunfish.icsp.common.xacml.PolicyAndPolicySetValidator;

//import eu.sunfishproject.icsp.prp.PolicyStoreFactory;
import eu.sunfishproject.icsp.prp.config.PRPConfig;
import eu.sunfishproject.icsp.prp.policyfinder.PolicyFinderIF;
//import eu.sunfishproject.icsp.prp.policyfinder.SimplePolicyFinderImpl;
import eu.sunfishproject.icsp.prp.util.Utils;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public class RenamePolicyIds {
	protected static ConcurrentHashMap<String, TreeMap<String, PolicyType>> rootPolicyMap = new ConcurrentHashMap<>();
	protected static ConcurrentHashMap<String, TreeMap<String, PolicyType>> subPolicyMap = new ConcurrentHashMap<>();
	protected static ConcurrentHashMap<String, TreeMap<String, PolicySetType>> rootPolicySetMap = new ConcurrentHashMap<>();
	protected static ConcurrentHashMap<String, TreeMap<String, PolicySetType>> subPolicySetMap = new ConcurrentHashMap<>();
	
	protected static ConcurrentHashMap<String, PolicyDef> rootPolicyDefMap = new ConcurrentHashMap<>();
	
//	protected static ConcurrentHashMap<String,PolicyType> rootPolicyMap = new ConcurrentHashMap<>();
//	protected static ConcurrentHashMap<String,PolicyType> subPolicyMap = new ConcurrentHashMap<>();
//	protected static ConcurrentHashMap<String,PolicySetType> rootPolicySetMap = new ConcurrentHashMap<>();
//	protected static ConcurrentHashMap<String,PolicySetType> subPolicySetMap = new ConcurrentHashMap<>();

	protected static AtomicLong lastModifiedTime = new AtomicLong(System.currentTimeMillis());

	private final static XACMLPolicyAggregator aggregator = new XACMLPolicyAggregator();
//	private static PolicyFinderIF policyFinder = new SimplePolicyFinderImpl();
//	private static final Logger log = LogManager.getLogger(PolicyStoreFactory.class);
	
	public static void main(String[] args) throws IOException {
		
//		while(loadFiles( PRPConfig.getInstance().getRootPolicyDirectory(), true)== true)
//		{
//			rootPolicyMap = new ConcurrentHashMap<>();
//			 subPolicyMap = new ConcurrentHashMap<>();
//			 rootPolicySetMap = new ConcurrentHashMap<>();
//			 subPolicySetMap = new ConcurrentHashMap<>();
//		}

	}
	
	private static boolean loadFiles(final String directory, final boolean rootPolicy) {
		final String[] extensions = {"xml"};
		final Collection<File> files = FileUtils.listFiles(new File(directory), extensions , false);
		int i=0;
		for(final File f:files)
		{
			try{
				final XACMLPolicyScanner scanner = new XACMLPolicyScanner(f.toPath(), aggregator);

				// The scanner returns us a policy object
				final Object policyObject = scanner.readPolicy(new FileInputStream(f));
				final PolicyDef policyDef = DOMPolicyDef.load(f);
				if(policyDef == null)
				{
//					log.debug("DOMPolicyDef.load() failed");
					continue;
				}

				if (policyObject instanceof PolicySetType) {
					final PolicySetType policySetType = (PolicySetType) policyObject;
					if(PolicyAndPolicySetValidator.validate(policySetType) == PolicyAndPolicySetValidator.OK)
					{
						try{
						addPolicySet(policySetType, rootPolicy);
						}catch(ConflictException ee)
						{
							policySetType.setPolicySetId(policySetType.getPolicySetId()+"x");
							f.renameTo(new File("D:\\Projekte\\SunFISH\\workspace\\parent\\prp\\src\\main\\resources\\Policies\\gehtNicht\\"+f.getName()));
							FileOutputStream entityStream = new FileOutputStream(f);
							 XACMLPolicyWriter.writePolicyFile(entityStream, policySetType);
							 entityStream.flush();
							 entityStream.close();
//							 addPolicySet(policySetType, rootPolicy);
							 return true;
						}
//						log.info(i++ + " Loading policyset: " + policySetType.getPolicySetId()+" "+f.getAbsolutePath());
						if(rootPolicy)
						{
							rootPolicyDefMap.put(Utils.getKey(policyDef), policyDef);
						}
					}
					else
					{
//						log.info(i++ + " NOT Loading policyset: " + policySetType.getPolicySetId());
					}

				} else if (policyObject instanceof PolicyType) {
					final PolicyType policyType = (PolicyType) policyObject;
					if(PolicyAndPolicySetValidator.validate(policyType) == PolicyAndPolicySetValidator.OK)
					{
						try{
						addPolicy(policyType, rootPolicy);
						}catch(ConflictException ee)
						{
							policyType.setPolicyId(policyType.getPolicyId()+"x");
							f.renameTo(new File("D:\\Projekte\\SunFISH\\workspace\\parent\\prp\\src\\main\\resources\\Policies\\verschoben\\"+f.getName()));
							FileOutputStream entityStream = new FileOutputStream(f);
							 XACMLPolicyWriter.writePolicyFile(entityStream, policyType);
							 entityStream.flush();
							 entityStream.close();
//							 addPolicy(policyType, rootPolicy);
							 return true;
						}
						
						
//						log.info(i++ +" Loading policy: " + policyType.getPolicyId()+" "+f.getAbsolutePath());
						if(rootPolicy)
						{
							rootPolicyDefMap.put(Utils.getKey(policyDef), policyDef);
						}
					}
					else
					{
//						log.info(i++ + " NOT Loading policy: " + policyType.getPolicyId());
					}
				}
			}catch(final Exception e)
			{
				e.printStackTrace();
//				log.warn("Failed to load: "+f.getAbsolutePath()+" "+e.getMessage());
				f.renameTo(new File("D:\\Projekte\\SunFISH\\workspace\\parent\\prp\\src\\main\\resources\\Policies\\gehtNicht\\"+f.getName()));
			}
		}
		 return false;
	}
	
	private static void addPolicy(final PolicyType policy, final ConcurrentHashMap<String,TreeMap<String,PolicyType>> map) throws ConflictException {
		final String id = policy.getPolicyId();
		final String version = policy.getVersion();
		if(map.containsKey(id))
		{
			//All entries have the same id, can identify the policy based on the version
			final TreeMap<String, PolicyType> mapOfPolicies = map.get(id);
			if(mapOfPolicies.containsKey(version))
			{
				throw new ConflictException();
			}
			else
			{
				mapOfPolicies.put(version, policy);
			}
		}
		else
		{
			final TreeMap<String, PolicyType> mapOfPolicies = new TreeMap<>();
			mapOfPolicies.put(version, policy);
			map.put(id, mapOfPolicies);
		}
		lastModifiedTime.set(System.currentTimeMillis());
	}


	public static void addPolicy(final PolicyType policy, final boolean rootPolicy) throws ConflictException {
		if(rootPolicy)
		{
			addPolicy(policy, rootPolicyMap);
		}
		else
		{
			addPolicy(policy, subPolicyMap);
		}
	}


	public static void addPolicySet(final PolicySetType policySet, final boolean rootPolicySet) throws ConflictException {
		if(rootPolicySet)
		{
			addPolicySet(policySet, rootPolicySetMap);
		}
		else
		{
			addPolicySet(policySet, subPolicySetMap);
		}
	}

	private static void addPolicySet(final PolicySetType policySet, final ConcurrentHashMap<String, TreeMap<String, PolicySetType>> map) throws ConflictException {
		final String id = policySet.getPolicySetId();
		final String version = policySet.getVersion();
		if(map.containsKey(id))
		{
			//All entries have the same id, can identify the policyset based on the version
			final TreeMap<String, PolicySetType> mapOfPolicySets = map.get(id);
			if(mapOfPolicySets.containsKey(version))
			{
				throw new ConflictException();
			}
			else
			{
				mapOfPolicySets.put(version, policySet);
			}
		}
		else
		{
			final TreeMap<String, PolicySetType> mapOfPolicySets =new TreeMap<>();
			mapOfPolicySets.put(version, policySet);
			map.put(id, mapOfPolicySets);
		}
		lastModifiedTime.set(System.currentTimeMillis());
	}
}
