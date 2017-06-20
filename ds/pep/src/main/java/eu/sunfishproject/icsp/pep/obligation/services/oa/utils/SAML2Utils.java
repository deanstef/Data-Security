/*******************************************************************************
 * Copyright 2014 Federal Chancellery Austria
 * MOA-ID has been developed in a cooperation between BRZ, the Federal
 * Chancellery Austria - ICT staff unit, and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 *******************************************************************************/
package eu.sunfishproject.icsp.pep.obligation.services.oa.utils;

import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class SAML2Utils {

	static {
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static DocumentBuilder builder;

	public static <T> T createSAMLObject(final Class<T> clazz) {
		try {

			XMLObjectBuilderFactory builderFactory = Configuration
					.getBuilderFactory();

			QName defaultElementName = (QName) clazz.getDeclaredField(
					"DEFAULT_ELEMENT_NAME").get(null);
			Map<QName, XMLObjectBuilder> builder = builderFactory.getBuilders();
			Iterator<QName> it = builder.keySet().iterator();

			while (it.hasNext()) {
				QName qname = it.next();
				if (qname.equals(defaultElementName)) {
					System.out.printf("Builder for: %s\n", qname.toString());
				}
			}
			XMLObjectBuilder xmlBuilder = builderFactory
					.getBuilder(defaultElementName);
			
			T object = (T) xmlBuilder.buildObject(defaultElementName);
			return object;
		} catch (Throwable e) {
			System.out.printf("Failed to create object for: %s\n",
					clazz.toString());
			e.printStackTrace();
			return null;
		}
	}

	public static Document asDOMDocument(XMLObject object) throws IOException,
			MarshallingException, TransformerException {
		Document document = builder.newDocument();
		Marshaller out = Configuration.getMarshallerFactory().getMarshaller(
				object);
		out.marshall(object, document);
		return (Document) document;
	}
	

	
}
