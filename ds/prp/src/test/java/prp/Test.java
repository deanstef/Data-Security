package prp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.sunfish.icsp.common.rest.CommonSetup;

import eu.sunfishproject.icsp.prp.util.Utils;

public class Test {

	public static void main(String[] args) {
		Client client = CommonSetup.createClient();

	    WebTarget sampleResource = client.target("http://localhost:8080/prp/v1");
	    
	    String s = Utils.loadResource("Request/4.1.2-DecisionRequest.xml");
//	    ClassLoader classLoader = Test.class.getClassLoader();
//	    String s = null;
//	    try {
//			s = IOUtils.toString(classLoader.getResourceAsStream("Request/4.1.2-DecisionRequest.xml"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		 // post
	    Response response = sampleResource.path("collect").request(MediaType.TEXT_PLAIN).post(Entity.xml(s));
	    if (response.getStatusInfo().getFamily() != Status.Family.SUCCESSFUL) {
	      throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
	    }
	    String output = response.readEntity(String.class);
	    System.out.print("\npost\nResponse: ");
	    System.out.println(output);
	}

}
