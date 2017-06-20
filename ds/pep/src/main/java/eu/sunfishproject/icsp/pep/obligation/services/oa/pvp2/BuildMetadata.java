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
package eu.sunfishproject.icsp.pep.obligation.services.oa.pvp2;


import eu.sunfishproject.icsp.pep.obligation.services.oa.Configuration;
import eu.sunfishproject.icsp.pep.obligation.services.oa.Constants;
import eu.sunfishproject.icsp.pep.obligation.services.oa.exception.ConfigurationException;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.AttributeListBuilder;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MiscUtil;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.SAML2Utils;
import org.joda.time.DateTime;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.metadata.*;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.x509.KeyStoreX509CredentialAdapter;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.security.x509.X509KeyInfoGeneratorFactory;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;


public class BuildMetadata extends HttpServlet {


	private static final Logger log = LoggerFactory.getLogger(BuildMetadata.class);

	private static final long serialVersionUID = 1L;
	
	private static final int VALIDUNTIL_IN_HOURS = 24;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BuildMetadata() {
		super();
	}

	protected static Signature getSignature(Credential credentials) {
		Signature signer = SAML2Utils.createSAMLObject(Signature.class);
		signer.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
		signer.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
		signer.setSigningCredential(credentials);
		return signer;
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Configuration config = Configuration.getInstance();
						
			SecureRandomIdentifierGenerator idGen = new SecureRandomIdentifierGenerator();
			
			EntitiesDescriptor spEntitiesDescriptor = SAML2Utils.
					createSAMLObject(EntitiesDescriptor.class);
			
			DateTime date = new DateTime();	
			spEntitiesDescriptor.setValidUntil(date.plusHours(VALIDUNTIL_IN_HOURS));
			
			String name = config.getPVP2MetadataEntitiesName();
			if (MiscUtil.isEmpty(name)) {
				throw new ConfigurationException("NO Metadata EntitiesName configurated");
			}
			
			spEntitiesDescriptor.setName(name);
			spEntitiesDescriptor.setID(idGen.generateIdentifier());
			
			//set period of validity for metadata information
			DateTime validUntil =  new DateTime();
			spEntitiesDescriptor.setValidUntil(validUntil.plusDays(7));
			
			
			EntityDescriptor spEntityDescriptor = SAML2Utils
					.createSAMLObject(EntityDescriptor.class);

			spEntityDescriptor.setValidUntil(date.plusDays(VALIDUNTIL_IN_HOURS));
			
			spEntitiesDescriptor.getEntityDescriptors().add(spEntityDescriptor);
			
			//set OA-ID (PublicURL Prefix) as identifier
			String serviceURL = config.getPublicUrlPreFix(request);
			if (!serviceURL.endsWith("/"))
				serviceURL = serviceURL + "/";
			
			spEntityDescriptor.setEntityID(serviceURL);

			SPSSODescriptor spSSODescriptor = SAML2Utils
					.createSAMLObject(SPSSODescriptor.class);

			spSSODescriptor.setAuthnRequestsSigned(true);
			spSSODescriptor.setWantAssertionsSigned(true);

			X509KeyInfoGeneratorFactory keyInfoFactory = new X509KeyInfoGeneratorFactory();
			keyInfoFactory.setEmitEntityCertificate(true);
			KeyInfoGenerator keyInfoGenerator = keyInfoFactory.newInstance();

			
			KeyStore keyStore = config.getPVP2KeyStore();

			X509Credential signingcredential = new KeyStoreX509CredentialAdapter(
					keyStore, 
					config.getPVP2KeystoreMetadataKeyAlias(), 
					config.getPVP2KeystoreMetadataKeyPassword().toCharArray());

			
			//Set MetaData Signing key
			KeyDescriptor entitiesSignKeyDescriptor = SAML2Utils
					.createSAMLObject(KeyDescriptor.class);
			entitiesSignKeyDescriptor.setUse(UsageType.SIGNING);
			entitiesSignKeyDescriptor.setKeyInfo(keyInfoGenerator.generate(signingcredential));
			Signature entitiesSignature = getSignature(signingcredential);
			spEntitiesDescriptor.setSignature(entitiesSignature);

			
			//Set AuthRequest Signing certificate
			X509Credential authcredential = new KeyStoreX509CredentialAdapter(
					keyStore, 
					config.getPVP2KeystoreAuthRequestKeyAlias(), 
					config.getPVP2KeystoreAuthRequestKeyPassword().toCharArray());			
			KeyDescriptor signKeyDescriptor = SAML2Utils
					.createSAMLObject(KeyDescriptor.class);
			
			signKeyDescriptor.setUse(UsageType.SIGNING);
			signKeyDescriptor.setKeyInfo(keyInfoGenerator.generate(authcredential));
			
			spSSODescriptor.getKeyDescriptors().add(signKeyDescriptor);
			
			
			//set AuthRequest encryption certificate
			if (MiscUtil.isNotEmpty(config.getPVP2KeystoreAuthRequestEncryptionKeyAlias()) ||
					MiscUtil.isNotEmpty(config.getPVP2KeystoreAuthRequestEncryptionKeyPassword())) {
				X509Credential authEncCredential = new KeyStoreX509CredentialAdapter(
						keyStore, 
						config.getPVP2KeystoreAuthRequestEncryptionKeyAlias(), 
						config.getPVP2KeystoreAuthRequestEncryptionKeyPassword().toCharArray());			
				KeyDescriptor encryKeyDescriptor = SAML2Utils
						.createSAMLObject(KeyDescriptor.class);
				encryKeyDescriptor.setUse(UsageType.ENCRYPTION);
				encryKeyDescriptor.setKeyInfo(keyInfoGenerator.generate(authEncCredential));
				
				//set encryption methode
//				EncryptionMethod encMethode = SAML2Utils.createSAMLObject(EncryptionMethod.class);
//				encMethode.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128_GCM);				
//				encryKeyDescriptor.getEncryptionMethods().add(encMethode);
//				
//				EncryptionMethod keyencMethode = SAML2Utils.createSAMLObject(EncryptionMethod.class);
//				keyencMethode.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);				
//				encryKeyDescriptor.getEncryptionMethods().add(keyencMethode);
				
				spSSODescriptor.getKeyDescriptors().add(encryKeyDescriptor);
				
			} else {
				System.out.println("No Assertion Encryption-Key defined. This setting is not recommended!");
				
			}
			
			
			NameIDFormat persistentnameIDFormat = SAML2Utils.createSAMLObject(NameIDFormat.class);
			persistentnameIDFormat.setFormat(NameIDType.PERSISTENT);
			
			spSSODescriptor.getNameIDFormats().add(persistentnameIDFormat);
			
			NameIDFormat transientnameIDFormat = SAML2Utils.createSAMLObject(NameIDFormat.class);
			transientnameIDFormat.setFormat(NameIDType.TRANSIENT);
			
			spSSODescriptor.getNameIDFormats().add(transientnameIDFormat);
			
			NameIDFormat unspecifiednameIDFormat = SAML2Utils.createSAMLObject(NameIDFormat.class);
			unspecifiednameIDFormat.setFormat(NameIDType.UNSPECIFIED);
			
			spSSODescriptor.getNameIDFormats().add(unspecifiednameIDFormat);

			//set HTTP-POST Binding assertion consumer service
			AssertionConsumerService postassertionConsumerService = 
					SAML2Utils.createSAMLObject(AssertionConsumerService.class);
			
			postassertionConsumerService.setIndex(0);
			postassertionConsumerService.setBinding(SAMLConstants.SAML2_POST_BINDING_URI);
			postassertionConsumerService.setLocation(serviceURL + Constants.SERVLET_PVP2ASSERTION);
			spSSODescriptor.getAssertionConsumerServices().add(postassertionConsumerService);
			
			//set Single Log-Out service
			SingleLogoutService sloService =  SAML2Utils.createSAMLObject(SingleLogoutService.class);
			sloService.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
			sloService.setLocation(serviceURL + Constants.SERVLET_PVPSINGLELOGOUT);
			spSSODescriptor.getSingleLogoutServices().add(sloService);
			
			spSSODescriptor.addSupportedProtocol(SAMLConstants.SAML20P_NS);
			
			spEntityDescriptor.getRoleDescriptors().add(spSSODescriptor);
			
			AttributeConsumingService attributeService = 
					SAML2Utils.createSAMLObject(AttributeConsumingService.class);
			
			attributeService.setIndex(0);
			attributeService.setIsDefault(true);
			ServiceName serviceName = SAML2Utils.createSAMLObject(ServiceName.class);
			serviceName.setName(new LocalizedString("Default Service", "de"));
			attributeService.getNames().add(serviceName);
			
			//set attributes which are requested
			attributeService.getRequestAttributes().addAll(AttributeListBuilder.getRequestedAttributes());
			spSSODescriptor.getAttributeConsumingServices().add(attributeService);

			
			//build metadata
			DocumentBuilder builder;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			Marshaller out = org.opensaml.Configuration.getMarshallerFactory().getMarshaller(spEntitiesDescriptor);
			out.marshall(spEntitiesDescriptor, document);
			
			Signer.signObject(entitiesSignature);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			DOMSource source  = new DOMSource(document);
			transformer.transform(source, sr);
			sw.close();
			
			String metadataXML = sw.toString();
						
			response.setContentType("text/xml");
			response.getOutputStream().write(metadataXML.getBytes());
			
			response.getOutputStream().close();
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
			
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
							
		} catch (ParserConfigurationException e) {
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
							
		} catch (TransformerConfigurationException e) {
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
			
		} catch (TransformerFactoryConfigurationError e) {
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
			
		} catch (TransformerException e) {
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
		}
		
		catch (Exception e) {
			throw new ServletException("MetaData can not be created. Look into LogFiles for more details.");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}