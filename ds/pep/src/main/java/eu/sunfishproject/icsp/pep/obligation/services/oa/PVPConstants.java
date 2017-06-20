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

public interface PVPConstants {
	public static final String URN_OID_PREFIX = "urn:oid:";
	
	public static final String PVP_VERSION_OID = "1.2.40.0.10.2.1.1.261.10";
	public static final String PVP_VERSION_NAME = URN_OID_PREFIX + PVP_VERSION_OID;
	public static final String PVP_VERSION_FRIENDLY_NAME = "PVP-VERSION";
	public static final String PVP_VERSION_2_1 = "2.1";
	
	public static final String SECCLASS_FRIENDLY_NAME = "SECCLASS";
	
	public static final String PRINCIPAL_NAME_OID = "1.2.40.0.10.2.1.1.261.20";
	public static final String PRINCIPAL_NAME_NAME = URN_OID_PREFIX + PRINCIPAL_NAME_OID;
	public static final String PRINCIPAL_NAME_FRIENDLY_NAME = "PRINCIPAL-NAME";
	public static final int PRINCIPAL_NAME_MAX_LENGTH = 128;
	
	public static final String GIVEN_NAME_OID = "2.5.4.42";
	public static final String GIVEN_NAME_NAME = URN_OID_PREFIX + GIVEN_NAME_OID;
	public static final String GIVEN_NAME_FRIENDLY_NAME = "GIVEN-NAME";
	public static final int GIVEN_NAME_MAX_LENGTH = 128;
	
	public static final String BIRTHDATE_OID = "1.2.40.0.10.2.1.1.55";
	public static final String BIRTHDATE_NAME = URN_OID_PREFIX + BIRTHDATE_OID;
	public static final String BIRTHDATE_FRIENDLY_NAME = "BIRTHDATE";
	public static final String BIRTHDATE_FORMAT_PATTERN = "yyyy-MM-dd";
	
	public static final String USERID_OID = "0.9.2342.19200300.100.1.1";
	public static final String USERID_NAME = URN_OID_PREFIX + USERID_OID;
	public static final String USERID_FRIENDLY_NAME = "USERID";
	public static final int USERID_MAX_LENGTH = 128;
	
	public static final String GID_OID = "1.2.40.0.10.2.1.1.1";
	public static final String GID_NAME = URN_OID_PREFIX + GID_OID;
	public static final String GID_FRIENDLY_NAME = "GID";
	public static final int GID_MAX_LENGTH = 128;
	
	public static final String BPK_OID = "1.2.40.0.10.2.1.1.149";
	public static final String BPK_NAME = URN_OID_PREFIX + BPK_OID;
	public static final String BPK_FRIENDLY_NAME = "BPK";
	public static final int BPK_MAX_LENGTH = 1024;
	
	public static final String ENC_BPK_LIST_OID = "1.2.40.0.10.2.1.1.261.22";
	public static final String ENC_BPK_LIST_NAME = URN_OID_PREFIX+ENC_BPK_LIST_OID;
	public static final String ENC_BPK_LIST_FRIENDLY_NAME = "ENC-BPK-LIST";
	public static final int ENC_BPK_LIST_MAX_LENGTH = 32767;
	
	public static final String MAIL_OID = "0.9.2342.19200300.100.1.3";
	public static final String MAIL_NAME = URN_OID_PREFIX + MAIL_OID;
	public static final String MAIL_FRIENDLY_NAME = "MAIL";
	public static final int MAIL_MAX_LENGTH = 128;
	
	public static final String TEL_OID = "2.5.4.20";
	public static final String TEL_NAME = URN_OID_PREFIX + TEL_OID;
	public static final String TEL_FRIENDLY_NAME = "TEL";
	public static final int TEL_MAX_LENGTH = 32;
	
	public static final String PARTICIPANT_ID_OID = "1.2.40.0.10.2.1.1.71";
	public static final String PARTICIPANT_ID_NAME = URN_OID_PREFIX + PARTICIPANT_ID_OID;
	public static final String PARTICIPANT_ID_FRIENDLY_NAME = "PARTICIPANT-ID";
	public static final int PARTICIPANT_MAX_LENGTH = 39;
	
	public static final String PARTICIPANT_OKZ_OID = "1.2.40.0.10.2.1.1.261.24";
	public static final String PARTICIPANT_OKZ_NAME = URN_OID_PREFIX + PARTICIPANT_OKZ_OID;
	public static final String PARTICIPANT_OKZ_FRIENDLY_NAME = "PARTICIPANT-OKZ";
	public static final int PARTICIPANT_OKZ_MAX_LENGTH = 32;
	
	public static final String OU_OKZ_OID = "1.2.40.0.10.2.1.1.153";
	public static final String OU_OKZ_NAME =  URN_OID_PREFIX + OU_OKZ_OID;
	public static final int OU_OKZ_MAX_LENGTH = 32;
	
	public static final String OU_GV_OU_ID_OID = "1.2.40.0.10.2.1.1.3";
	public static final String OU_GV_OU_ID_NAME = URN_OID_PREFIX + OU_GV_OU_ID_OID;
	public static final String OU_GV_OU_ID_FRIENDLY_NAME = "OU-GV-OU-ID";
	public static final int OU_GV_OU_ID_MAX_LENGTH = 39;
	
	public static final String OU_OID = "2.5.4.11";
	public static final String OU_NAME = URN_OID_PREFIX + OU_OID;
	public static final String OU_FRIENDLY_NAME = "OU";
	public static final int OU_MAX_LENGTH = 64;
	
	public static final String FUNCTION_OID = "1.2.40.0.10.2.1.1.33";
	public static final String FUNCTION_NAME = URN_OID_PREFIX + FUNCTION_OID;
	public static final String FUNCTION_FRIENDLY_NAME = "FUNCTION";
	public static final int FUNCTION_MAX_LENGTH = 32;
	
	public static final String ROLES_OID = "1.2.40.0.10.2.1.1.261.30";
	public static final String ROLES_NAME = URN_OID_PREFIX + ROLES_OID;
	public static final String ROLES_FRIENDLY_NAME = "ROLES";
	public static final int ROLES_MAX_LENGTH = 32767;
	
	public static final String EID_CITIZEN_QAA_LEVEL_OID = "1.2.40.0.10.2.1.1.261.94";
	public static final String EID_CITIZEN_QAA_LEVEL_NAME = URN_OID_PREFIX + EID_CITIZEN_QAA_LEVEL_OID;
	public static final String EID_CITIZEN_QAA_LEVEL_FRIENDLY_NAME = "EID-CITIZEN-QAA-LEVEL";
	
	public static final String EID_ISSUING_NATION_OID = "1.2.40.0.10.2.1.1.261.32";
	public static final String EID_ISSUING_NATION_NAME = URN_OID_PREFIX + EID_ISSUING_NATION_OID;
	public static final String EID_ISSUING_NATION_FRIENDLY_NAME = "EID-ISSUING-NATION";
	public static final int EID_ISSUING_NATION_MAX_LENGTH = 2;
	
	public static final String EID_SECTOR_FOR_IDENTIFIER_OID = "1.2.40.0.10.2.1.1.261.34";
	public static final String EID_SECTOR_FOR_IDENTIFIER_NAME = URN_OID_PREFIX + EID_SECTOR_FOR_IDENTIFIER_OID;
	public static final String EID_SECTOR_FOR_IDENTIFIER_FRIENDLY_NAME = "EID-SECTOR-FOR-IDENTIFIER";
	public static final int EID_SECTOR_FOR_IDENTIFIER_MAX_LENGTH = 255;
	
	public static final String EID_SOURCE_PIN_OID = "1.2.40.0.10.2.1.1.261.36";
	public static final String EID_SOURCE_PIN_NAME = URN_OID_PREFIX + EID_SOURCE_PIN_OID;
	public static final String EID_SOURCE_PIN_FRIENDLY_NAME = "EID-SOURCE-PIN";
	public static final int EID_SOURCE_PIN_MAX_LENGTH = 128;
	
	public static final String EID_SOURCE_PIN_TYPE_OID = "1.2.40.0.10.2.1.1.261.104";
	public static final String EID_SOURCE_PIN_TYPE_NAME = URN_OID_PREFIX + EID_SOURCE_PIN_TYPE_OID;
	public static final String EID_SOURCE_PIN_TYPE_FRIENDLY_NAME = "EID-SOURCE-PIN-TYPE";
	public static final int EID_SOURCE_PIN_TYPE_MAX_LENGTH = 128;
	
	public static final String EID_IDENTITY_LINK_OID = "1.2.40.0.10.2.1.1.261.38";
	public static final String EID_IDENTITY_LINK_NAME = URN_OID_PREFIX + EID_IDENTITY_LINK_OID;
	public static final String EID_IDENTITY_LINK_FRIENDLY_NAME = "EID-IDENTITY-LINK";
	public static final int EID_IDENTITY_LINK_MAX_LENGTH = 32767;
	
	public static final String EID_AUTH_BLOCK_OID = "1.2.40.0.10.2.1.1.261.62";
	public static final String EID_AUTH_BLOCK_NAME = URN_OID_PREFIX + EID_AUTH_BLOCK_OID;
	public static final String EID_AUTH_BLOCK_FRIENDLY_NAME = "EID-AUTH-BLOCK";
	public static final int EID_AUTH_BLOCK_MAX_LENGTH = 32767;
	
	public static final String EID_CCS_URL_OID = "1.2.40.0.10.2.1.1.261.64";
	public static final String EID_CCS_URL_NAME = URN_OID_PREFIX + EID_CCS_URL_OID;
	public static final String EID_CCS_URL_FRIENDLY_NAME = "EID-CCS-URL";
	public static final int EID_CCS_URL_MAX_LENGTH = 1024;
	
	public static final String EID_SIGNER_CERTIFICATE_OID = "1.2.40.0.10.2.1.1.261.66";
	public static final String EID_SIGNER_CERTIFICATE_NAME = URN_OID_PREFIX + EID_SIGNER_CERTIFICATE_OID;
	public static final String EID_SIGNER_CERTIFICATE_FRIENDLY_NAME = "EID-SIGNER-CERTIFICATE";
	public static final int EID_SIGNER_CERTIFICATE_MAX_LENGTH = 32767;
	
	public static final String EID_STORK_TOKEN_OID = "1.2.40.0.10.2.1.1.261.96";
	public static final String EID_STORK_TOKEN_NAME = URN_OID_PREFIX + EID_STORK_TOKEN_OID;
	public static final String EID_STORK_TOKEN_FRIENDLY_NAME = "EID-STORK-TOKEN";
	public static final int EID_STORK_TOKEN_MAX_LENGTH = 32767;
	
	public static final String MANDATE_TYPE_OID = "1.2.40.0.10.2.1.1.261.68";
	public static final String MANDATE_TYPE_NAME = URN_OID_PREFIX + MANDATE_TYPE_OID;
	public static final String MANDATE_TYPE_FRIENDLY_NAME = "MANDATE-TYPE";
	public static final int MANDATE_TYPE_MAX_LENGTH = 256;
	
	public static final String MANDATE_NAT_PER_SOURCE_PIN_OID = "1.2.40.0.10.2.1.1.261.70";
	public static final String MANDATE_NAT_PER_SOURCE_PIN_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_SOURCE_PIN_OID;
	public static final String MANDATE_NAT_PER_SOURCE_PIN_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-SOURCE-PIN";
	public static final int MANDATE_NAT_PER_SOURCE_PIN_MAX_LENGTH = 128;
	
	public static final String MANDATE_LEG_PER_SOURCE_PIN_OID = "1.2.40.0.10.2.1.1.261.100";
	public static final String MANDATE_LEG_PER_SOURCE_PIN_NAME = URN_OID_PREFIX + MANDATE_LEG_PER_SOURCE_PIN_OID;
	public static final String MANDATE_LEG_PER_SOURCE_PIN_FRIENDLY_NAME = "MANDATOR-LEGAL-PERSON-SOURCE-PIN";
	public static final int MANDATE_LEG_PER_SOURCE_PIN_MAX_LENGTH = 128;
	
	public static final String MANDATE_NAT_PER_SOURCE_PIN_TYPE_OID = "1.2.40.0.10.2.1.1.261.102";
	public static final String MANDATE_NAT_PER_SOURCE_PIN_TYPE_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_SOURCE_PIN_TYPE_OID;
	public static final String MANDATE_NAT_PER_SOURCE_PIN_TYPE_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-SOURCE-PIN-TYPE";
	public static final int MANDATE_NAT_PER_SOURCE_PIN_TYPE_MAX_LENGTH = 128;
	
	public static final String MANDATE_LEG_PER_SOURCE_PIN_TYPE_OID = "1.2.40.0.10.2.1.1.261.76";
	public static final String MANDATE_LEG_PER_SOURCE_PIN_TYPE_NAME = URN_OID_PREFIX + MANDATE_LEG_PER_SOURCE_PIN_TYPE_OID;
	public static final String MANDATE_LEG_PER_SOURCE_PIN_TYPE_FRIENDLY_NAME = "MANDATOR-LEGAL-PERSON-SOURCE-PIN-TYPE";
	public static final int MANDATE_LEG_PER_SOURCE_PIN_TYPE_MAX_LENGTH = 128;
	
	public static final String MANDATE_NAT_PER_BPK_OID = "1.2.40.0.10.2.1.1.261.98";
	public static final String MANDATE_NAT_PER_BPK_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_BPK_OID;
	public static final String MANDATE_NAT_PER_BPK_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-BPK";
	public static final int MANDATE_NAT_PER_BPK_MAX_LENGTH = 1024;
	
	public static final String MANDATE_NAT_PER_ENC_BPK_LIST_OID = "1.2.40.0.10.2.1.1.261.72";
	public static final String MANDATE_NAT_PER_ENC_BPK_LIST_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_ENC_BPK_LIST_OID;
	public static final String MANDATE_NAT_PER_ENC_BPK_LIST_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-ENC-BPK-LIST";
	public static final int MANDATE_NAT_PER_ENC_BPK_LIST_MAX_LENGTH = 32767;
	
	public static final String MANDATE_NAT_PER_GIVEN_NAME_OID = "1.2.40.0.10.2.1.1.261.78";
	public static final String MANDATE_NAT_PER_GIVEN_NAME_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_GIVEN_NAME_OID;
	public static final String MANDATE_NAT_PER_GIVEN_NAME_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-GIVEN-NAME";
	public static final int MANDATE_NAT_PER_GIVEN_NAME_MAX_LENGTH = 128;
	
	public static final String MANDATE_NAT_PER_FAMILY_NAME_OID = "1.2.40.0.10.2.1.1.261.80";
	public static final String MANDATE_NAT_PER_FAMILY_NAME_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_FAMILY_NAME_OID;
	public static final String MANDATE_NAT_PER_FAMILY_NAME_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-FAMILY-NAME";
	public static final int MANDATE_NAT_PER_FAMILY_NAME_MAX_LENGTH = 128;
	
	public static final String MANDATE_NAT_PER_BIRTHDATE_OID = "1.2.40.0.10.2.1.1.261.82";
	public static final String MANDATE_NAT_PER_BIRTHDATE_NAME = URN_OID_PREFIX + MANDATE_NAT_PER_BIRTHDATE_OID;
	public static final String MANDATE_NAT_PER_BIRTHDATE_FRIENDLY_NAME = "MANDATOR-NATURAL-PERSON-BIRTHDATE";
	public static final String MANDATE_NAT_PER_BIRTHDATE_FORMAT_PATTERN = BIRTHDATE_FORMAT_PATTERN;
	
	public static final String MANDATE_LEG_PER_FULL_NAME_OID = "1.2.40.0.10.2.1.1.261.84";
	public static final String MANDATE_LEG_PER_FULL_NAME_NAME = URN_OID_PREFIX + MANDATE_LEG_PER_FULL_NAME_OID;
	public static final String MANDATE_LEG_PER_FULL_NAME_FRIENDLY_NAME = "MANDATOR-LEGAL-PERSON-FULL-NAME";
	public static final int MANDATE_LEG_PER_FULL_NAME_MAX_LENGTH = 256;
	
	public static final String MANDATE_PROF_REP_OID_OID = "1.2.40.0.10.2.1.1.261.86";
	public static final String MANDATE_PROF_REP_OID_NAME = URN_OID_PREFIX + MANDATE_PROF_REP_OID_OID;
	public static final String MANDATE_PROF_REP_OID_FRIENDLY_NAME = "MANDATOR-PROF-REP-OID";
	public static final int MANDATE_PROF_REP_OID_MAX_LENGTH = 256;
	
	public static final String MANDATE_PROF_REP_DESC_OID = "1.2.40.0.10.2.1.1.261.88";
	public static final String MANDATE_PROF_REP_DESC_NAME = URN_OID_PREFIX + MANDATE_PROF_REP_DESC_OID;
	public static final String MANDATE_PROF_REP_DESC_FRIENDLY_NAME = "MANDATOR-PROF-REP-DESCRIPTION";
	public static final int MANDATE_PROF_REP_DESC_MAX_LENGTH = 1024;
	
	public static final String MANDATE_REFERENCE_VALUE_OID = "1.2.40.0.10.2.1.1.261.90";
	public static final String MANDATE_REFERENCE_VALUE_NAME = URN_OID_PREFIX + MANDATE_REFERENCE_VALUE_OID;
	public static final String MANDATE_REFERENCE_VALUE_FRIENDLY_NAME = "MANDATE-REFERENCE-VALUE";
	public static final int MANDATE_REFERENCE_VALUE_MAX_LENGTH = 100;
	
	public static final String MANDATE_FULL_MANDATE_OID = "1.2.40.0.10.2.1.1.261.92";
	public static final String MANDATE_FULL_MANDATE_NAME = URN_OID_PREFIX + MANDATE_FULL_MANDATE_OID;
	public static final String MANDATE_FULL_MANDATE_FRIENDLY_NAME = "MANDATE-FULL-MANDATE";
	public static final int MANDATE_FULL_MANDATE_MAX_LENGTH = 32767;
	
	public static final String INVOICE_RECPT_ID_OID = "1.2.40.0.10.2.1.1.261.40";
	public static final String INVOICE_RECPT_ID_NAME = URN_OID_PREFIX + INVOICE_RECPT_ID_OID;
	public static final String INVOICE_RECPT_ID_FRIENDLY_NAME = "INVOICE-RECPT-ID";
	public static final int INVOICE_RECPT_ID_MAX_LENGTH = 64;
	
	public static final String COST_CENTER_ID_OID = "1.2.40.0.10.2.1.1.261.50";
	public static final String COST_CENTER_ID_NAME = URN_OID_PREFIX + COST_CENTER_ID_OID;
	public static final String COST_CENTER_ID_FRIENDLY_NAME = "COST-CENTER-ID";
	public static final int COST_CENTER_ID_MAX_LENGTH = 32767;
	
	public static final String CHARGE_CODE_OID = "1.2.40.0.10.2.1.1.261.60";
	public static final String CHARGE_CODE_NAME = URN_OID_PREFIX + CHARGE_CODE_OID;
	public static final String CHARGE_CODE_FRIENDLY_NAME = "CHARGE-CODE";
	public static final int CHARGE_CODE_MAX_LENGTH = 32767;
}
