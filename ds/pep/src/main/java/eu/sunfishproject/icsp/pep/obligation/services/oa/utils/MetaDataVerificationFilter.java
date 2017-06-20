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

import org.opensaml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.saml2.metadata.provider.FilterException;
import org.opensaml.saml2.metadata.provider.MetadataFilter;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;

public class MetaDataVerificationFilter implements MetadataFilter {

	BasicX509Credential credential;
	
	public MetaDataVerificationFilter(BasicX509Credential credential) {
		this.credential = credential;
	}
	
	
	public void doFilter(XMLObject metadata) throws FilterException {
		if (metadata instanceof EntitiesDescriptor) {
			EntitiesDescriptor entitiesDescriptor = (EntitiesDescriptor) metadata;
			
			if(entitiesDescriptor.getSignature() == null) {
				throw new FilterException("IDP metadata is not signed", null);
			}
			
			Signature sign = entitiesDescriptor.getSignature();
			
			try {
			
			//Validate signature
				SAMLSignatureProfileValidator profileValidator = new SAMLSignatureProfileValidator();
				profileValidator.validate(sign);

				
			//Verify signature
			SignatureValidator sigValidator = new SignatureValidator(credential);
			sigValidator.validate(sign);
							
			} catch (ValidationException e) {
				throw new FilterException("IDP metadata validation error", e);
				
			}
			
		}
	}
	
}
