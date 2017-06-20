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
import eu.sunfishproject.icsp.pep.obligation.services.oa.exception.ConfigurationException;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MiscUtil;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.SAML2Utils;
import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.SingleSignOnService;
import org.opensaml.saml2.metadata.impl.SingleSignOnServiceBuilder;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.ws.transport.http.HttpServletResponseAdapter;
import org.opensaml.xml.security.x509.KeyStoreX509CredentialAdapter;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Map;


/**
 * Servlet implementation class Authenticate
 */
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory
			.getLogger(Authenticate.class);	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Authenticate() {
		super();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			builder = factory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			log.warn("PVP2 AuthenticationServlet can not be initialized.", e);
		}
	}

	DocumentBuilder builder;


	//generate AuthenticationRequest
	protected void process(HttpServletRequest request,
			HttpServletResponse response, Map<String,String> legacyParameter) throws ServletException, IOException {
		try {

			String referer = request.getParameter("referrer");

			HttpSession session = request.getSession();
			session.setAttribute("referrer", referer);

			Configuration config = Configuration.getInstance();
			config.initializePVP2Login();
			
			AuthnRequest authReq = SAML2Utils
					.createSAMLObject(AuthnRequest.class);
			SecureRandomIdentifierGenerator gen = new SecureRandomIdentifierGenerator();
			authReq.setID(gen.generateIdentifier());
			
			
			authReq.setAssertionConsumerServiceIndex(0);
			authReq.setAttributeConsumingServiceIndex(0);
			
			authReq.setIssueInstant(new DateTime());
//			Subject subject = SAML2Utils.createSAMLObject(Subject.class);
//			NameID name = SAML2Utils.createSAMLObject(NameID.class);
			Issuer issuer = SAML2Utils.createSAMLObject(Issuer.class);
			
			String serviceURL = config.getPublicUrlPreFix(request);
			if (!serviceURL.endsWith("/"))
				serviceURL = serviceURL + "/";
			//name.setValue(serviceURL);
			issuer.setValue(serviceURL);
			
//			subject.setNameID(name);
//			authReq.setSubject(subject);
			issuer.setFormat(NameIDType.ENTITY);
			authReq.setIssuer(issuer);
			
			NameIDPolicy policy = SAML2Utils
					.createSAMLObject(NameIDPolicy.class);
			policy.setAllowCreate(true);			
			policy.setFormat(NameID.PERSISTENT);			
			authReq.setNameIDPolicy(policy);
			
			String entityname = config.getPVP2IDPMetadataEntityName();
			if (MiscUtil.isEmpty(entityname)) {
				log.info("No IDP EntityName configurated");
				throw new ConfigurationException("No IDP EntityName configurated");
			}
			
			//get IDP metadata from metadataprovider
			HTTPMetadataProvider idpmetadata = config.getMetaDataProvier();			
			EntityDescriptor idpEntity = idpmetadata.getEntityDescriptor(entityname);
			if (idpEntity == null) {
				log.info("IDP EntityName is not found in IDP Metadata");
				throw new ConfigurationException("IDP EntityName is not found in IDP Metadata");
			}
			
			//select authentication-service url from metadata
			SingleSignOnService redirectEndpoint = null;  
			for (SingleSignOnService sss : 
					idpEntity.getIDPSSODescriptor(SAMLConstants.SAML20P_NS).getSingleSignOnServices()) {
				
//				//Get the service address for the binding you wish to use
//				if (sss.getBinding().equals(SAMLConstants.SAML2_POST_BINDING_URI)) { 
//					redirectEndpoint = sss;  
//				}
				
				//Get the service address for the binding you wish to use
				if (sss.getBinding().equals(SAMLConstants.SAML2_REDIRECT_BINDING_URI)) { 
					redirectEndpoint = sss;  
				}  
				
			}
			authReq.setDestination(redirectEndpoint.getLocation());
			
			//authReq.setDestination("http://test.test.test");
			
			
			RequestedAuthnContext reqAuthContext = 
					SAML2Utils.createSAMLObject(RequestedAuthnContext.class);
			
			AuthnContextClassRef authnClassRef = 
					SAML2Utils.createSAMLObject(AuthnContextClassRef.class);
			
			authnClassRef.setAuthnContextClassRef("http://www.stork.gov.eu/1.0/citizenQAALevel/4");

			reqAuthContext.setComparison(AuthnContextComparisonTypeEnumeration.MINIMUM);
			
			reqAuthContext.getAuthnContextClassRefs().add(authnClassRef);
			
			authReq.setRequestedAuthnContext(reqAuthContext);
			
			//sign authentication request
			KeyStore keyStore = config.getPVP2KeyStore();
			X509Credential authcredential = new KeyStoreX509CredentialAdapter(
					keyStore, 
					config.getPVP2KeystoreAuthRequestKeyAlias(), 
					config.getPVP2KeystoreAuthRequestKeyPassword().toCharArray());

			Signature signer = SAML2Utils.createSAMLObject(Signature.class);
			signer.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
			signer.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
			signer.setSigningCredential(authcredential);
			authReq.setSignature(signer);

			//generate Http-POST Binding message
//			VelocityEngine engine = new VelocityEngine();
//			engine.setProperty(RuntimeConstants.ENCODING_DEFAULT, "UTF-8");
//			engine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");
//			engine.setProperty(RuntimeConstants.ENCODING_DEFAULT, "UTF-8");
//			engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
//			engine.setProperty("classpath.resource.loader.class",
//					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//			engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
//					"org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
//			engine.init();
//
//			HTTPPostEncoder encoder = new HTTPPostEncoder(engine,
//					"templates/pvp_postbinding_template.html");
//			HttpServletResponseAdapter responseAdapter = new HttpServletResponseAdapter(
//					response, true);
//			BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> context = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>();
//			SingleSignOnService service = new SingleSignOnServiceBuilder()
//					.buildObject();
//			service.setBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
//			service.setLocation(redirectEndpoint.getLocation());;
//			
//			context.setOutboundSAMLMessageSigningCredential(authcredential);
//			context.setPeerEntityEndpoint(service);
//			context.setOutboundSAMLMessage(authReq);
//			context.setOutboundMessageTransport(responseAdapter);

			//generate Redirect Binding message
			HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();
			HttpServletResponseAdapter responseAdapter = new HttpServletResponseAdapter(
					response, true);
			BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> context = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>();
			SingleSignOnService service = new SingleSignOnServiceBuilder()
					.buildObject();
			service.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
			service.setLocation(redirectEndpoint.getLocation());
			context.setOutboundSAMLMessageSigningCredential(authcredential);
			context.setPeerEntityEndpoint(service);
			context.setOutboundSAMLMessage(authReq);
			context.setOutboundMessageTransport(responseAdapter);
			//context.setRelayState(relayState);
			
			encoder.encode(context);

		} catch (Exception e) {
			log.warn("Authentication Request can not be generated", e);
			throw new ServletException("Authentication Request can not be generated.", e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
				
		process(request, response, null);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response, null);
	}

}
