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

import eu.sunfishproject.icsp.pep.obligation.services.oa.PVPConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.metadata.RequestedAttribute;

import java.util.ArrayList;
import java.util.List;


public class AttributeListBuilder implements PVPConstants {

	protected static RequestedAttribute buildReqAttribute(String name, String friendlyName, boolean required) {
		RequestedAttribute attribute = SAML2Utils.createSAMLObject(RequestedAttribute.class);
		attribute.setIsRequired(required);
		attribute.setName(name);
		attribute.setFriendlyName(friendlyName);
		attribute.setNameFormat(Attribute.URI_REFERENCE);
		return attribute;
	}
	
	public static List<RequestedAttribute> getRequestedAttributes() {
		List<RequestedAttribute> requestedAttributes = new ArrayList<RequestedAttribute>();
		
		
		//select PVP2 attributes which are needed for this application
		requestedAttributes.add(buildReqAttribute(PVP_VERSION_NAME, PVP_VERSION_FRIENDLY_NAME, true));
		requestedAttributes.add(buildReqAttribute(PRINCIPAL_NAME_NAME, PRINCIPAL_NAME_FRIENDLY_NAME, true));
		requestedAttributes.add(buildReqAttribute(GIVEN_NAME_NAME, GIVEN_NAME_FRIENDLY_NAME, true));
		requestedAttributes.add(buildReqAttribute(BIRTHDATE_NAME, BIRTHDATE_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(BPK_NAME, BPK_FRIENDLY_NAME, true));
		
		requestedAttributes.add(buildReqAttribute(EID_CITIZEN_QAA_LEVEL_NAME, EID_CITIZEN_QAA_LEVEL_FRIENDLY_NAME, true));
		requestedAttributes.add(buildReqAttribute(EID_ISSUING_NATION_NAME, EID_ISSUING_NATION_FRIENDLY_NAME, true));
		requestedAttributes.add(buildReqAttribute(EID_SECTOR_FOR_IDENTIFIER_NAME, EID_SECTOR_FOR_IDENTIFIER_FRIENDLY_NAME, true));
		requestedAttributes.add(buildReqAttribute(EID_SIGNER_CERTIFICATE_NAME, EID_SIGNER_CERTIFICATE_FRIENDLY_NAME, false));		
		requestedAttributes.add(buildReqAttribute(EID_CCS_URL_NAME, EID_CCS_URL_FRIENDLY_NAME, true));		
		requestedAttributes.add(buildReqAttribute(EID_AUTH_BLOCK_NAME, EID_AUTH_BLOCK_FRIENDLY_NAME, false));		
		requestedAttributes.add(buildReqAttribute(EID_IDENTITY_LINK_NAME, EID_IDENTITY_LINK_FRIENDLY_NAME, true));
		
		requestedAttributes.add(buildReqAttribute(MANDATE_TYPE_NAME, MANDATE_TYPE_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_FULL_MANDATE_NAME, MANDATE_FULL_MANDATE_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_LEG_PER_FULL_NAME_NAME, MANDATE_LEG_PER_FULL_NAME_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_LEG_PER_SOURCE_PIN_NAME, MANDATE_LEG_PER_SOURCE_PIN_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_LEG_PER_SOURCE_PIN_TYPE_NAME, MANDATE_LEG_PER_SOURCE_PIN_TYPE_FRIENDLY_NAME, false));
		
		requestedAttributes.add(buildReqAttribute(MANDATE_NAT_PER_BIRTHDATE_NAME, MANDATE_NAT_PER_BIRTHDATE_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_NAT_PER_BPK_NAME, MANDATE_NAT_PER_BPK_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_NAT_PER_FAMILY_NAME_NAME, MANDATE_NAT_PER_FAMILY_NAME_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_NAT_PER_GIVEN_NAME_NAME, MANDATE_NAT_PER_GIVEN_NAME_FRIENDLY_NAME, false));		
		requestedAttributes.add(buildReqAttribute(MANDATE_NAT_PER_SOURCE_PIN_NAME, MANDATE_NAT_PER_SOURCE_PIN_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_NAT_PER_SOURCE_PIN_TYPE_NAME, MANDATE_NAT_PER_SOURCE_PIN_TYPE_FRIENDLY_NAME, false));
		
		requestedAttributes.add(buildReqAttribute(MANDATE_REFERENCE_VALUE_NAME, MANDATE_REFERENCE_VALUE_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_PROF_REP_OID_NAME, MANDATE_PROF_REP_OID_FRIENDLY_NAME, false));
		requestedAttributes.add(buildReqAttribute(MANDATE_PROF_REP_DESC_NAME, MANDATE_PROF_REP_DESC_FRIENDLY_NAME, false));
		return requestedAttributes;
	}
}
