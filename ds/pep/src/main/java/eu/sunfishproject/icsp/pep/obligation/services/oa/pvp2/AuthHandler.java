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


import eu.sunfishproject.icsp.pep.config.PEPConfig;
import org.sunfish.icsp.common.session.SessionStorage;
import eu.sunfishproject.icsp.pep.obligation.services.oa.Configuration;
import eu.sunfishproject.icsp.pep.obligation.services.oa.Constants;
import eu.sunfishproject.icsp.pep.obligation.services.oa.PVPConstants;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.ApplicationBean;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.DOMUtils;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.SAML2Utils;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.binding.decoding.HTTPPostDecoder;
import org.opensaml.saml2.core.*;
import org.opensaml.saml2.encryption.Decrypter;
import org.opensaml.saml2.encryption.EncryptedElementTypeEncryptedKeyResolver;
import org.opensaml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.security.MetadataCredentialResolver;
import org.opensaml.security.MetadataCredentialResolverFactory;
import org.opensaml.security.MetadataCriteria;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.xml.encryption.ChainingEncryptedKeyResolver;
import org.opensaml.xml.encryption.InlineEncryptedKeyResolver;
import org.opensaml.xml.encryption.SimpleRetrievalMethodEncryptedKeyResolver;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.credential.UsageType;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.criteria.UsageCriteria;
import org.opensaml.xml.security.keyinfo.BasicProviderKeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.KeyInfoProvider;
import org.opensaml.xml.security.keyinfo.StaticKeyInfoCredentialResolver;
import org.opensaml.xml.security.keyinfo.provider.DSAKeyValueProvider;
import org.opensaml.xml.security.keyinfo.provider.InlineX509DataProvider;
import org.opensaml.xml.security.keyinfo.provider.RSAKeyValueProvider;
import org.opensaml.xml.security.x509.KeyStoreX509CredentialAdapter;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.impl.ExplicitKeySignatureTrustEngine;
import org.sunfish.icsp.common.rest.SunfishServices;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class AuthHandler extends HttpServlet {

	private static final long serialVersionUID = -2129228304760706063L;
	
	
	
	private void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		
		ApplicationBean bean = new ApplicationBean();
		
		
		String method = request.getMethod();
		HttpSession session = request.getSession();
		if (session == null) {
			bean.setErrorMessage("NO HTTP session");
			setAnswer(request, response, bean);
			return;
		}
		
		if (method.equals("POST")) {
		
			try {
				Configuration config = Configuration.getInstance();
				
				//Decode with HttpPost Binding
				HTTPPostDecoder decode = new HTTPPostDecoder(new BasicParserPool());
				BasicSAMLMessageContext<Response, ?, ?> messageContext = new BasicSAMLMessageContext<Response, SAMLObject, SAMLObject>();
				messageContext
					.setInboundMessageTransport(new HttpServletRequestAdapter(
							request));
				decode.decode(messageContext);
				
				Response samlResponse = (Response) messageContext.getInboundMessage();
			
				Signature sign = samlResponse.getSignature();
				if (sign == null) {
					bean.setErrorMessage("Only http POST Requests can be used");
					setAnswer(request, response, bean);
					return;
				}
				
				//Validate Signature
				SAMLSignatureProfileValidator profileValidator = new SAMLSignatureProfileValidator();
				profileValidator.validate(sign);
				
				//Verify Signature
				List<KeyInfoProvider> keyInfoProvider = new ArrayList<KeyInfoProvider>();
				keyInfoProvider.add(new DSAKeyValueProvider());
				keyInfoProvider.add(new RSAKeyValueProvider());
				keyInfoProvider.add(new InlineX509DataProvider());

				KeyInfoCredentialResolver keyInfoResolver = new BasicProviderKeyInfoCredentialResolver(
						keyInfoProvider);
				
				MetadataCredentialResolverFactory credentialResolverFactory = MetadataCredentialResolverFactory.getFactory();    
				MetadataCredentialResolver credentialResolver = credentialResolverFactory.getInstance(config.getMetaDataProvier());  
				  
				CriteriaSet criteriaSet = new CriteriaSet();  
				criteriaSet.add(new MetadataCriteria(IDPSSODescriptor.DEFAULT_ELEMENT_NAME, SAMLConstants.SAML20P_NS));  
				criteriaSet.add(new EntityIDCriteria(config.getPVP2IDPMetadataEntityName()));
				criteriaSet.add(new UsageCriteria(UsageType.SIGNING));
				  				
				ExplicitKeySignatureTrustEngine trustEngine = new ExplicitKeySignatureTrustEngine(credentialResolver, keyInfoResolver);
				trustEngine.validate(sign, criteriaSet);
				

				//set assertion
				org.w3c.dom.Document doc = SAML2Utils.asDOMDocument(samlResponse);
				String assertion = DOMUtils.serializeNode(doc);
				bean.setAssertion(assertion);
				
				if (samlResponse.getStatus().getStatusCode().getValue().equals(StatusCode.SUCCESS_URI)) {
			
					List<org.opensaml.saml2.core.Assertion> saml2assertions = new ArrayList<org.opensaml.saml2.core.Assertion>();
					
					//check encrypted Assertion
					List<EncryptedAssertion> encryAssertionList = samlResponse.getEncryptedAssertions();
					if (encryAssertionList != null && encryAssertionList.size() > 0) {
						//decrypt assertions

						KeyStore keyStore = config.getPVP2KeyStore();
						
						X509Credential authDecCredential = new KeyStoreX509CredentialAdapter(
								keyStore, 
								config.getPVP2KeystoreAuthRequestEncryptionKeyAlias(), 
								config.getPVP2KeystoreAuthRequestEncryptionKeyPassword().toCharArray());
						
						
						StaticKeyInfoCredentialResolver skicr =
								  new StaticKeyInfoCredentialResolver(authDecCredential);
						
						ChainingEncryptedKeyResolver encryptedKeyResolver = new ChainingEncryptedKeyResolver();
						encryptedKeyResolver.getResolverChain().add( new InlineEncryptedKeyResolver() );
						encryptedKeyResolver.getResolverChain().add( new EncryptedElementTypeEncryptedKeyResolver() );
						encryptedKeyResolver.getResolverChain().add( new SimpleRetrievalMethodEncryptedKeyResolver() );
						
						Decrypter samlDecrypter =
								new Decrypter(null, skicr, encryptedKeyResolver);
						
						for (EncryptedAssertion encAssertion : encryAssertionList) {							
							saml2assertions.add(samlDecrypter.decrypt(encAssertion));

						}
						

					} else {
						saml2assertions = samlResponse.getAssertions();
				
					}
					
					String givenName = null;
					String familyName = null;
					String birthday = null;
					String bpk = null;
					String issuingNation = null;
					
					for (org.opensaml.saml2.core.Assertion saml2assertion : saml2assertions) {
						
						//loop through the nodes to get what we want
						List<AttributeStatement> attributeStatements = saml2assertion.getAttributeStatements();
						for (int i = 0; i < attributeStatements.size(); i++)
						{
							List<Attribute> attributes = attributeStatements.get(i).getAttributes();
							for (int x = 0; x < attributes.size(); x++)
							{
								String strAttributeName = attributes.get(x).getDOM().getAttribute("Name");

								if (strAttributeName.equals(PVPConstants.PRINCIPAL_NAME_NAME))
									familyName = attributes.get(x).getAttributeValues().get(0).getDOM().getFirstChild().getNodeValue();
								if (strAttributeName.equals(PVPConstants.GIVEN_NAME_NAME))
									givenName = attributes.get(x).getAttributeValues().get(0).getDOM().getFirstChild().getNodeValue();
								
								if (strAttributeName.equals(PVPConstants.BIRTHDATE_NAME)) {
									birthday = attributes.get(x).getAttributeValues().get(0).getDOM().getFirstChild().getNodeValue();
								}

								if (strAttributeName.equals(PVPConstants.BPK_NAME)) {
									bpk = attributes.get(x).getAttributeValues().get(0).getDOM().getFirstChild().getNodeValue();
								}
								if (strAttributeName.equals(PVPConstants.EID_ISSUING_NATION_NAME)) {
									issuingNation = attributes.get(x).getAttributeValues().get(0).getDOM().getFirstChild().getNodeValue();
								}

							}
						}						
						request.getSession().setAttribute(Constants.SESSION_NAMEIDFORMAT,
								saml2assertion.getSubject().getNameID().getFormat());
						request.getSession().setAttribute(Constants.SESSION_NAMEID, 
								saml2assertion.getSubject().getNameID().getValue());
						
					}



					List<String> userRoles = PEPConfig.getInstance().getUserRoles(bpk);
					String userSession = generateNewSession(givenName, familyName, birthday, userRoles, bpk, issuingNation);
										
					bean.setDateOfBirth(birthday);
					bean.setFamilyName(familyName);
					bean.setGivenName(givenName);
					bean.setSession(userSession);
					bean.setLogin(true);

					setAnswer(request, response, bean);
					return;
					
					
				} else {
					bean.setErrorMessage("Der Anmeldevorgang wurde abgebrochen.<br>Eine genaue Beschreibung des Fehlers finden Sie in der darunterliegenden Assertion.");
					setAnswer(request, response, bean);
					return;
					
				}
				
			} catch (Exception e) {
				bean.setErrorMessage("Internal Error: " + e.getMessage());
				setAnswer(request, response, bean);
				return;
			}
			
		} else {
			bean.setErrorMessage("Die Demoapplikation unterstützt nur SAML2 POST-Binding.");
			setAnswer(request, response, bean);
			return;
			
		}
	}	
	
	private void setAnswer(HttpServletRequest request, HttpServletResponse response, ApplicationBean answersBean) throws ServletException, IOException {
        // store bean in session
        request.setAttribute("answers", answersBean);

		// you now can forward to some view, for example some results.jsp
        //request.getRequestDispatcher("demoapp.jsp").forward(request, response);

		HttpSession session = request.getSession();
		String referrer = (String)session.getAttribute("referrer");

		if(answersBean.getSession() != null) {
			response.sendRedirect(referrer+"?" + SunfishServices.COOKIE_NAME_SESSION + "=" + answersBean.getSession());
		} else {
			response.sendRedirect(referrer);
		}
		
	}



	private String generateNewSession(String firstName, String lastName, String birthday, List<String> userRoles, String bpk, String issuingNation) {

		SessionStorage sessionStorage = SessionStorage.getInstance();

		return sessionStorage.generateUserSession(firstName, lastName, birthday, userRoles, bpk, issuingNation);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
				
		process(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
}
