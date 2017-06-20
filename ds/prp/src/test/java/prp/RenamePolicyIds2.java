package prp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class RenamePolicyIds2 {
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
	
	private static String name = "withTarget";
	public static void main(String[] args) throws IOException {
		
		loadFiles("Q:\\prp\\PolicyStore\\withTarget\\rootPolicies\\");

	}
	
	private static boolean loadFiles(final String directory ) {
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
				String id;
				if (policyObject instanceof PolicySetType) {
					final PolicySetType policySetType = (PolicySetType) policyObject;
					if(PolicyAndPolicySetValidator.validate(policySetType) == PolicyAndPolicySetValidator.OK)
					{
						id = policySetType.getPolicySetId();
						policySetType.setPolicySetId(id.replaceFirst("urn:", "urn:"+name+":"));
						storePolicySet(policySetType);
					}
					else
					{
//						log.info(i++ + " NOT Loading policyset: " + policySetType.getPolicySetId());
					}

				} else if (policyObject instanceof PolicyType) {
					final PolicyType policyType = (PolicyType) policyObject;
					if(PolicyAndPolicySetValidator.validate(policyType) == PolicyAndPolicySetValidator.OK)
					{
						id = policyType.getPolicyId();
						policyType.setPolicyId(id.replaceFirst("urn:", "urn:"+name+":"));
						storePolicy(policyType);
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
			}
		}
		 return false;
	}
	
	
	public static void  storePolicy(PolicyType policyType) throws IOException
	{
		File f = new File("Q:\\prp\\PolicyStore\\tmp\\"+ policyType.getPolicyId().replaceAll(":", "_")+"_v"+policyType.getVersion()+".xml");
		FileOutputStream entityStream = new FileOutputStream(f);
		 XACMLPolicyWriter.writePolicyFile(entityStream, policyType);
		 entityStream.flush();
		 entityStream.close();
	}

	public static void  storePolicySet(PolicySetType policySetType) throws IOException
	{
		File f = new File("Q:\\prp\\PolicyStore\\tmp\\"+ policySetType.getPolicySetId().replaceAll(":", "_")+"_v"+policySetType.getVersion()+".xml");
		FileOutputStream entityStream = new FileOutputStream(f);
		 XACMLPolicyWriter.writePolicyFile(entityStream, policySetType);
		 entityStream.flush();
		 entityStream.close();
	}

}
