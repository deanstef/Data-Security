package prp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.dom.DOMPolicyDef;
import org.apache.openaz.xacml.util.XACMLPolicyAggregator;
import org.apache.openaz.xacml.util.XACMLPolicyScanner;
import org.apache.openaz.xacml.util.XACMLPolicyWriter;
import org.sunfish.icsp.common.xacml.PolicyAndPolicySetValidator;
//import org.sunfish.icsp.common.xacml.generated.ObjectFactory;
import org.sunfish.icsp.common.xacml.generated.PolicySetsType;

import com.sun.org.apache.bcel.internal.generic.LoadClass;

//import eu.sunfishproject.icsp.prp.PolicyStoreFactory;
import eu.sunfishproject.icsp.prp.config.PRPConfig;
import eu.sunfishproject.icsp.prp.util.Utils;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public class ConvertPolicy {
	
//	  private static PRPConfig       config;
	  private static ExecutorService srv;
	  static {
//	    try {
////	      config = PRPConfig.getInstance();
////	     
//	    } catch (final IOException e) {
//	      // TODO Auto-generated catch block
//	      e.printStackTrace();
//	    }
	  }
	  
	private static final Logger log              = LogManager.getLogger(ConvertPolicy.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		 final String[] extensions = { "xml" };
	    final Collection<File> files = FileUtils.listFiles(new File("o:\\del\\"), extensions, false);
	    for(File f:files)
	    {
	    	load(f);
	    }
	}

	static void load(File f)
	{
		try {
	        final XACMLPolicyScanner scanner = new XACMLPolicyScanner(f.toPath(), new XACMLPolicyAggregator());

	        // The scanner returns us a policy object
	        final Object policyObject = scanner.readPolicy(new FileInputStream(f));
	        final PolicyDef policyDef = DOMPolicyDef.load(f);
	        
//	        File fff = new File("o:\\del\\"+"test1.xml");
//			FileOutputStream entityStream1 = new FileOutputStream(fff);
//			
//			 XACMLPolicyWriter.w(entityStream1, policyDef);
//			 entityStream1.flush();
//			 entityStream1.close();

//	        if (policyDef == null) {
//	          log.warn("DOMPolicyDef.load() failed, Failed to load: "+f.getAbsolutePath());
//	          
//	          return;
//	        }

	        if (policyObject instanceof PolicySetType) {
	          final PolicySetType policySetType = (PolicySetType) policyObject;
	          

	        } else if (policyObject instanceof PolicyType) {
	          final PolicyType policyType = (PolicyType) policyObject;
	          File ff = new File("o:\\del\\"+"test.xml");
				FileOutputStream entityStream = new FileOutputStream(ff);
				 XACMLPolicyWriter.writePolicyFile(entityStream, policyType);
				 entityStream.flush();
				 entityStream.close();
	        }
	      } catch (final Exception e) {
	        e.printStackTrace();
	        log.warn("Failed to load: " + f.getAbsolutePath() + " " + e.getMessage());
	       
	      }
	      
	    
	}
	
//	void test(PolicyDef policyDef)
//	{
//		JAXBElement<PolicySetsType> policySetElement = new ObjectFactory().cre;
//		try {
//			JAXBContext context = JAXBContext.newInstance(PolicySetsType.class);
//			Marshaller m = context.createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//			m.marshal(policySetElement, os);
//		} catch (JAXBException e) {
//			log.error("writePolicyFile failed: " + e.getLocalizedMessage());
//		}
//	}
}
