package org.sunfish.icsp.common.rest.mappers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.apache.openaz.xacml.std.dom.DOMUtil;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.xacml.generated.PoliciesType;
import org.sunfish.icsp.common.xacml.generated.PolicySetsType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;


@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPolicyTypeReader implements MessageBodyReader<PolicyType> {

	private static JAXBContext jaxbContext = initContext();


	private static final Logger log = LogManager.getLogger(XACMLPolicyTypeReader.class);

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance(PolicyType.class);
		} catch (JAXBException e) {
			e.printStackTrace();
			//do something more useful
		}
		return null;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return PolicyType.class.isAssignableFrom(type);
	}

	@Override
	public PolicyType readFrom(Class<PolicyType> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
					throws IOException, WebApplicationException {
		try {
			DocumentBuilder db = DOMUtil.getDocumentBuilder();
	        Document doc = db.parse(entityStream);
	        

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			JAXBElement<PolicyType> policiesElement = unmarshaller.unmarshal(doc, PolicyType.class);

			PolicyType policies = policiesElement.getValue();
			return policies;
			
		} catch (JAXBException | SAXException | DOMStructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException(e);
		}
	}

}