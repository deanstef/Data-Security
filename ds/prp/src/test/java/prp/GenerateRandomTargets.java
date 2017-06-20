package prp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

public class GenerateRandomTargets {
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


		addTarget("o:\\addtarget");
//		removeTarget("o:\\rootPolicyies\\");
	}
	private static void removeTarget(String directory) {
		final String[] extensions = {"xml"};
		Collection<File> files = FileUtils.listFiles(new File(directory), extensions , false);
		int j=0;
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

					if(policySetType.getTarget() != null)
					{
						policySetType.setTarget(new TargetType());
						FileOutputStream entityStream = new FileOutputStream(f);
						 XACMLPolicyWriter.writePolicyFile(entityStream, policySetType);
						 entityStream.flush();
						 entityStream.close();
						 j++;
					}

				} else if (policyObject instanceof PolicyType) {
					final PolicyType policyType = (PolicyType) policyObject;

					if(policyType.getTarget() != null)
					{
						policyType.setTarget(new TargetType());
						FileOutputStream entityStream = new FileOutputStream(f);
						 XACMLPolicyWriter.writePolicyFile(entityStream, policyType);
						 entityStream.flush();
						 entityStream.close();
						 j++;
					}
				}
			}catch(final Exception e)
			{
				e.printStackTrace();

			}
			finally {
				System.out.println("policies changed:"+j);
			}
			
				
			
		}

	}
	private static void addTarget(String directory) {
		final String[] extensions = {"xml"};
		Collection<File> files = FileUtils.listFiles(new File(directory), extensions , false);
		
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

					if(policySetType.getTarget() == null || policySetType.getTarget().getAnyOf() == null || policySetType.getTarget().getAnyOf().size() == 0)
					{
						policySetType.setTarget(getRandomTarget());
						FileOutputStream entityStream = new FileOutputStream(f);
						 XACMLPolicyWriter.writePolicyFile(entityStream, policySetType);
						 entityStream.flush();
						 entityStream.close();
					}

				} else if (policyObject instanceof PolicyType) {
					final PolicyType policyType = (PolicyType) policyObject;

					if(policyType.getTarget() == null || policyType.getTarget().getAnyOf() == null || policyType.getTarget().getAnyOf().size() == 0)
					{
						policyType.setTarget(getRandomTarget());
						FileOutputStream entityStream = new FileOutputStream(f);
						 XACMLPolicyWriter.writePolicyFile(entityStream, policyType);
						 entityStream.flush();
						 entityStream.close();
					}
				}
			}catch(final Exception e)
			{
				e.printStackTrace();

			}
		}

	}

	private static TargetType getRandomTarget() {
		int maxNumberOfAllOfBlocks = (int)(Math.random()*10);
		TargetType target = new TargetType();
		AnyOfType anyOf = new AnyOfType();
		target.getAnyOf().add(anyOf);
		Collection<AllOfType> allOfList = new ArrayList();
		for(int i=0;i<=maxNumberOfAllOfBlocks;i++)
		{
			AllOfType allOf = new AllOfType();
			List<MatchType> matches = allOf.getMatch();
			matches.addAll(getMatches());
			allOfList.add(allOf);
		}
		anyOf.getAllOf().addAll(allOfList );
		return target;
	}

	private static Collection<? extends MatchType> getMatches() {
		Collection<MatchType> matches = new ArrayList();
		int maxNumberOfMatches = (int)(Math.random()*3);
		for(int i=0;i<=maxNumberOfMatches;i++)
		{
			MatchType match = new MatchType();
			match.setAttributeDesignator(getAttributeDesignatorType());
			match.setAttributeValue(getAttributeValueType());
			match.setMatchId(getMatchId());
			matches.add(match);
		}
		return matches;
	}

	private static String getMatchId() {
		int i = (int)(Math.random()*6.0);
		switch(i)
		{
//		case 0:
//			return "urn:oasis:names:tc:xacml:1.0:function:string-equal";
		case 1:
			return "urn:oasis:names:tc:xacml:3.0:function:string-equal-ignore-case";
		case 2:
			return "urn:oasis:names:tc:xacml:3.0:function:string-starts-with";
		case 3:
			return "urn:oasis:names:tc:xacml:1.0:function:string-regexp-match";
		default:
			return "urn:oasis:names:tc:xacml:1.0:function:string-equal";
		}
	}

	private static AttributeValueType getAttributeValueType() {
		AttributeValueType value = new AttributeValueType();
		value.setDataType("http://www.w3.org/2001/XMLSchema#string");
		value.getContent().add(RandomStringUtils.random((int)(Math.random()*5), "abcdefghijklmnopqrstuvwxyzäöüABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ"));
		return value;
	}

	private static AttributeDesignatorType getAttributeDesignatorType() {
		AttributeDesignatorType designator = new AttributeDesignatorType();
		if(Math.random()>0.5)
		{
			designator.setAttributeId("urn:sunfish:attribute:request:body-data");
			designator.setDataType("http://www.w3.org/2001/XMLSchema#string");
			designator.setCategory("urn:sunfish:attribute-category:request");
			designator.setMustBePresent(true);
		}
		else
		{
			designator.setAttributeId("urn:sunfish:attribute:id");
			designator.setDataType("http://www.w3.org/2001/XMLSchema#string");
			designator.setCategory("urn:sunfish:attribute-category:application");
			designator.setMustBePresent(true);
		}
		return designator;
	}


}
