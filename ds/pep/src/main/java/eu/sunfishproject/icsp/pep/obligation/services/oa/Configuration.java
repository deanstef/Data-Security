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
package eu.sunfishproject.icsp.pep.obligation.services.oa;



import eu.sunfishproject.icsp.pep.obligation.services.oa.exception.ConfigurationException;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MetaDataVerificationFilter;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MiscUtil;
import iaik.x509.X509Certificate;
import org.apache.commons.httpclient.HttpClient;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.security.x509.BasicX509Credential;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.KeyStore;
import java.util.Properties;
import java.util.Timer;


public class Configuration {


	private Properties props;
	private static final String SYSTEM_PROP_CONFIG = "moa.id.demoOA";

	public static final String CONF_DIR = System.getProperty("catalina.base") +File.separator + "conf" + File.separator + "sunfish" + File.separator + "moa-id-oa" + File.separator;
	private static String configFilePathReleativeToConfigDir = "oa.properties";


	private HTTPMetadataProvider idpMetadataProvider = null;
	private boolean pvp2logininitialzied = false;
	
	private String publicURLPreFix = null;
	private KeyStore keyStore = null;
	
	private static Configuration instance = null;
	
	public static Configuration getInstance() throws ConfigurationException {
		if (instance == null) {
			instance = new Configuration();

		}
		
		return instance;
	}

	public String getPublicUrlPreFix(HttpServletRequest request) {
		publicURLPreFix = props.getProperty("general.publicURLContext");
		
		if (MiscUtil.isEmpty(publicURLPreFix) && request != null) {
			String url = request.getRequestURL().toString();
			String contextpath = request.getContextPath();
			int index = url.indexOf(contextpath);
			publicURLPreFix = url.substring(0, index + contextpath.length() + 1);
		} 
		
		return publicURLPreFix;
	}
	
	
	public KeyStore getPVP2KeyStore() throws ConfigurationException {
		
		try {
			if (keyStore == null) {
				String keystoretype = getPVP2MetadataKeystoreType();
				if (MiscUtil.isEmpty(keystoretype)) {
					keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
					
				} else {
					keyStore = KeyStore.getInstance(keystoretype);
	
				}
						
				String file = getPVP2MetadataKeystoreURL();	
				if (MiscUtil.isEmpty(file)) {
					throw new ConfigurationException("KeyStoreURL is empty");
				}
				
				FileInputStream inputStream = new FileInputStream(file);
				keyStore.load(inputStream, getPVP2MetadataKeystorePassword().toCharArray());
				inputStream.close();
			}
			
			return keyStore;
			
		} catch (Exception e){
			throw new ConfigurationException("KeyStore intialization FAILED", e);
			
		}
		
	}
	
	public String getPVP2LoginEntityName() {
		return props.getProperty("general.login.pvp2.OA.metadata.entity.name");
	}
	
	public String getPVP2MetadataEntitiesName() {
		return props.getProperty("general.login.pvp2.OA.metadata.entities.name");
	}
	
	public String getPVP2MetadataKeystoreURL() {
		return props.getProperty("general.login.pvp2.OA.keystore.url");
	}
	
	public String getPVP2MetadataKeystorePassword() {
		return props.getProperty("general.login.pvp2.OA.keystore.password");
	}
	
	public String getPVP2MetadataKeystoreType() {
		return props.getProperty("general.login.pvp2.OA.keystore.type");
	}

	public String getPVP2KeystoreMetadataKeyAlias() {
		return props.getProperty("general.login.pvp2.OA.keystore.metadata.sign.key.alias");
	}
	
	public String getPVP2KeystoreMetadataKeyPassword() {
		return props.getProperty("general.login.pvp2.OA.keystore.metadata.sign.key.password");
	}
	
	public String getPVP2KeystoreAuthRequestKeyAlias() {
		return props.getProperty("general.login.pvp2.keystore.authrequest.sign.key.alias");
	}
	
	public String getPVP2KeystoreAuthRequestKeyPassword() {
		return props.getProperty("general.login.pvp2.keystore.authrequest.sign.key.password");
	}
	
	public String getPVP2KeystoreAuthRequestEncryptionKeyAlias() {
		return props.getProperty("general.login.pvp2.keystore.assertion.encryption.key.alias");
	}
	
	public String getPVP2KeystoreAuthRequestEncryptionKeyPassword() {
		return props.getProperty("general.login.pvp2.keystore.assertion.encryption.key.password");
	}
	
	public String getPVP2IDPMetadataURL() {
		return props.getProperty("general.login.pvp2.idp.metadata.url");
	}
	
	public String getPVP2IDPMetadataCertificate() {
		return props.getProperty("general.login.pvp2.idp.metadata.certificate");
	}
	
	public String getPVP2IDPMetadataEntityName() {
		return props.getProperty("general.login.pvp2.idp.metadata.entityID");
	}
	
	
	public void initializePVP2Login() throws ConfigurationException {
		if (!pvp2logininitialzied)
			initalPVP2Login();
		
	}
	
	public HTTPMetadataProvider getMetaDataProvier() throws ConfigurationException {
		
		if (!pvp2logininitialzied)
			initalPVP2Login();
		
		return idpMetadataProvider;
	}	
	
		
	private Configuration() throws ConfigurationException {
		inizialize();
	}
	
	private void inizialize() throws ConfigurationException {
		

		File propertiesFile = new File(CONF_DIR + configFilePathReleativeToConfigDir);
		FileInputStream fis;
		props = new Properties();
		
		try {
		
			fis = new FileInputStream(propertiesFile);
			props.load(fis);
			
			fis.close();
			
			//load OpenSAML library
			DefaultBootstrap.bootstrap();
			

		} catch ( FileNotFoundException e) {
			throw new ConfigurationException("DemoOA configuration is not found at " + propertiesFile.getAbsolutePath());
			
			
		} catch (IOException e) {
			throw new ConfigurationException("DemoOA configuration can not be read from file " + propertiesFile.getAbsolutePath());
			
		} catch (org.opensaml.xml.ConfigurationException e) {
			throw new ConfigurationException("OpenSAML library initialization FAILED");
			
		}
	}
	
	private void initalPVP2Login() throws ConfigurationException {
		try {
			
			//load IDP certificate to validate IDP metadata
			String metadataCert = getPVP2IDPMetadataCertificate();
			if (MiscUtil.isEmpty(metadataCert)) {
				throw new ConfigurationException("NO IDP Certificate to verify IDP Metadata");
			}
			
			InputStream certstream = new FileInputStream(metadataCert);
			X509Certificate cert = new X509Certificate(certstream);
			BasicX509Credential idpCredential = new BasicX509Credential();
			idpCredential.setEntityCertificate(cert);
			
			
			String metadataurl = getPVP2IDPMetadataURL();
			if (MiscUtil.isEmpty(metadataurl)) {
				throw new ConfigurationException("NO IDP Metadata URL.");
			}
		
			//load IDP metadata into metadataprovider
			idpMetadataProvider = new HTTPMetadataProvider(
					new Timer("demoOA", true), 
					new HttpClient(), metadataurl);  
			idpMetadataProvider.setRequireValidMetadata(true);  
			idpMetadataProvider.setParserPool(new BasicParserPool());
			idpMetadataProvider.setMetadataFilter(new MetaDataVerificationFilter(idpCredential));
			idpMetadataProvider.setMaxRefreshDelay(1000 * 3600 * 12 ); //refresh Metadata every 12h
			idpMetadataProvider.initialize(); 

			pvp2logininitialzied = true;
			

		} catch (Exception e) {
			throw new ConfigurationException("PVP2 authentification can not be initialized.", e);
		}	
	}

		
}
