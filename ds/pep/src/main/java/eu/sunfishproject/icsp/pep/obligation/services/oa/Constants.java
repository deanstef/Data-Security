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

public class Constants {
	public static final String FILEPREFIX = "file:";
	
	public static final String SERVLET_PVP2ASSERTION = "pvp2auth";
	
	public static final String SESSION_PVP2REQUESTID = "pvp2requestid";

	public static final String SERVLET_PVPSINGLELOGOUT = "singlelogout";

	public static final String SESSION_NAMEID = "pvp2nameID";

	public static final String SESSION_NAMEIDFORMAT = "pvp2nameIDFormat";

	/** Root location of the schema files. */
	public static final String SCHEMA_ROOT = "/resources/schemas/";

	/** URI of the Widerrufregister XML namespace. */
	public static final String WRR_NS_URI =
			"http://reference.e-government.gv.at/namespace/moavv/20041223";

	/** Prefix used for the Widerrufregister XML namespace */
	public static final String WRR_PREFIX = "wrr";

	/** URI of the StandardTextBlock XML namespace. */
	public static final String STB_NS_URI =
			"http://reference.e-government.gv.at/namespace/standardtextblock/20041105#";

	/** Prefix used for the standard text block XML namespace */
	public static final String STB_PREFIX = "stb";

	/** URI of the MOA XML namespace. */
	public static final String MOA_NS_URI =
			"http://reference.e-government.gv.at/namespace/moa/20020822#";

	/** Name of the mandates infobox */
	public static final String INFOBOXIDENTIFIER_MANDATES = "Mandates";

	/** Prefix used for the Mandate XML namespace */
	public static final String MD_PREFIX = "md";

	/** URI of the Mandate XML namespace. */
	public static final String MD_NS_URI =
			"http://reference.e-government.gv.at/namespace/mandates/20040701#";

	/** Prefix used for the Mandate XML namespace */
	public static final String MVV_PREFIX = "mvv";

	/** URI of the Mandate XML namespace. */
	public static final String MVV_NS_URI =
			"http://reference.e-government.gv.at/namespace/moavv/app2mvv/20041125";

	/** Prefix used for the MandateCheckProfile XML namespace */
	public static final String MDP_PREFIX = "mdp";

	/** URI of the Mandate XML namespace. */
	public static final String MDP_NS_URI =
			"http://reference.e-government.gv.at/namespace/mandateprofile/20041105#";

	/** Prefix used for the MOA XML namespace */
	public static final String MOA_PREFIX = "moa";

	/** Local location of the MOA XML schema definition. */
	public static final String MOA_SCHEMA_LOCATION =
			SCHEMA_ROOT + "MOA-SPSS-2.0.0.xsd";

	/** URI of the MOA configuration XML namespace. */
	public static final String MOA_CONFIG_NS_URI =
			"http://reference.e-government.gv.at/namespace/moaconfig/20021122#";

	/** URI of the MOA ID configuration XML namespace. */
	public static final String MOA_ID_CONFIG_NS_URI =
			"http://www.buergerkarte.at/namespaces/moaconfig#";

	/** Prefix used for the MOA configuration XML namespace */
	public static final String MOA_CONFIG_PREFIX = "conf";

	/** Prefix used for the MOA configuration XML namespace */
	public static final String MOA_ID_CONFIG_PREFIX = "confID";

	/** Local location of the MOA configuration XML schema definition. */
	public static final String MOA_CONFIG_SCHEMA_LOCATION =
			SCHEMA_ROOT + "MOA-SPSS-config-2.0.0.xsd";

	/** Local location of the MOA ID configuration XML schema definition. */
	public static final String MOA_ID_CONFIG_SCHEMA_LOCATION =
			SCHEMA_ROOT + "MOA-ID-Configuration-1.5.2.xsd";

	/** URI of the Security Layer 1.0 namespace. */
	public static final String SL10_NS_URI =
			"http://www.buergerkarte.at/namespaces/securitylayer/20020225#";

	/** Prefix used for the Security Layer 1.0 XML namespace */
	public static final String SL10_PREFIX = "sl10";

	/** Local location of the Security Layer 1.0 XML schema definition */
	public static final String SL10_SCHEMA_LOCATION =
			SCHEMA_ROOT + "Core.20020225.xsd";

	/** URI of the Security Layer 1.1 XML namespace */
	public static final String SL11_NS_URI =
			"http://www.buergerkarte.at/namespaces/securitylayer/20020831#";

	/** Prefix used for the Security Layer 1.1 XML namespace */
	public static final String SL11_PREFIX = "sl11";

	/** Local location of the Security Layer 1.1 XML schema definition */
	public static final String SL11_SCHEMA_LOCATION =
			SCHEMA_ROOT + "Core.20020831.xsd";

	/** URI of the Security Layer 1.2 XML namespace */
	public static final String SL12_NS_URI =
			"http://www.buergerkarte.at/namespaces/securitylayer/1.2#";

	/** Prefix used for the Security Layer 1.2 XML namespace */
	public static final String SL12_PREFIX = "sl";

	/** Local location of the Security Layer 1.2 XML schema definition */
	public static final String SL12_SCHEMA_LOCATION =
			SCHEMA_ROOT + "Core-1.2.xsd";

	/** URI of the ECDSA XML namespace */
	public static final String ECDSA_NS_URI =
			"http://www.w3.org/2001/04/xmldsig-more#";

	/** Prefix used for ECDSA namespace */
	public static final String ECDSA_PREFIX = "ecdsa";

	/** Local location of ECDSA XML schema definition */
	public static final String ECDSA_SCHEMA_LOCATION =
			SCHEMA_ROOT + "ECDSAKeyValue.xsd";

	/** URI of the PersonData XML namespace. */
	public static final String PD_NS_URI =
			"http://reference.e-government.gv.at/namespace/persondata/20020228#";

	/** Prefix used for the PersonData XML namespace */
	public static final String PD_PREFIX = "pr";

//  /** Local location of the PersonData XML schema definition */
//  public static final String PD_SCHEMA_LOCATION =
//    SCHEMA_ROOT + "PersonData.xsd";

	/** Local location of the PersonData XML schema definition */
	public static final String PD_SCHEMA_LOCATION =
			SCHEMA_ROOT + "PersonData_20_en_moaWID.xsd";

	/** URI of the SAML namespace. */
	public static final String SAML_NS_URI =
			"urn:oasis:names:tc:SAML:1.0:assertion";

	/** Prefix used for the SAML XML namespace */
	public static final String SAML_PREFIX = "saml";

	/** Local location of the SAML XML schema definition. */
	public static final String SAML_SCHEMA_LOCATION =
			SCHEMA_ROOT + "cs-sstc-schema-assertion-01.xsd";

	/** URI of the SAML request-response protocol namespace. */
	public static final String SAMLP_NS_URI =
			"urn:oasis:names:tc:SAML:1.0:protocol";

	/** Prefix used for the SAML request-response protocol namespace */
	public static final String SAMLP_PREFIX = "samlp";

	/** Local location of the SAML request-response protocol schema definition. */
	public static final String SAMLP_SCHEMA_LOCATION =
			SCHEMA_ROOT + "cs-sstc-schema-protocol-01.xsd";

	/** URI of the XML namespace. */
	public static final String XML_NS_URI =
			"http://www.w3.org/XML/1998/namespace";

	/** Prefix used for the XML namespace */
	public static final String XML_PREFIX = "xml";

	/** Local location of the XML schema definition. */
	public static final String XML_SCHEMA_LOCATION = SCHEMA_ROOT + "xml.xsd";

	/** URI of the XMLNS namespace */
	public static final String XMLNS_NS_URI = "http://www.w3.org/2000/xmlns/";

	/** Prefix used for the XSI namespace */
	public static final String XSI_PREFIX = "xsi";

	/** Local location of the XSI schema definition. */
	public static final String XSI_SCHEMA_LOCATION =
			SCHEMA_ROOT + "XMLSchema-instance.xsd";

	/** URI of the XSI XMLNS namespace */
	public static final String XSI_NS_URI =
			"http://www.w3.org/2001/XMLSchema-instance";

	/** URI of the XSLT XML namespace */
	public static final String XSLT_NS_URI =
			"http://www.w3.org/1999/XSL/Transform";

	/** Prefix used for the XSLT XML namespace */
	public static final String XSLT_PREFIX = "xsl";

	/** URI of the XMLDSig XML namespace. */
	public static final String DSIG_NS_URI = "http://www.w3.org/2000/09/xmldsig#";

	/** Prefix used for the XMLDSig XML namespace */
	public static final String DSIG_PREFIX = "dsig";

	/** Local location of the XMLDSig XML schema. */
	public static final String DSIG_SCHEMA_LOCATION =
			SCHEMA_ROOT + "xmldsig-core-schema.xsd";

	/** URI of the XMLDSig XPath Filter XML namespace. */
	public static final String DSIG_FILTER2_NS_URI =
			"http://www.w3.org/2002/06/xmldsig-filter2";

	/** Prefix used for the XMLDSig XPath Filter XML namespace */
	public static final String DSIG_FILTER2_PREFIX = "dsig-filter2";

	/** Local location of the XMLDSig XPath Filter XML schema definition. */
	public static final String DSIG_FILTER2_SCHEMA_LOCATION =
			SCHEMA_ROOT + "xmldsig-filter2.xsd";

	/** URI of the Exclusive Canonicalization XML namespace */
	public static final String DSIG_EC_NS_URI =
			"http://www.w3.org/2001/10/xml-exc-c14n#";

	/** Prefix used for the Exclusive Canonicalization XML namespace */
	public static final String DSIG_EC_PREFIX = "ec";

	/** Local location of the Exclusive Canonicalizaion XML schema definition */
	public static final String DSIG_EC_SCHEMA_LOCATION =
			SCHEMA_ROOT + "exclusive-canonicalization.xsd";

	/** URI of the XMLLoginParameterResolver Configuration XML namespace */
	public static final String XMLLPR_NS_URI="http://reference.e-government.gv.at/namespace/moa/20020822#/xmllpr20030814";

	/** Local location of the XMLLoginParameterResolver Configuration XML schema definition */
	public static final String XMLLPR_SCHEMA_LOCATION =
			SCHEMA_ROOT + "MOAIdentities.xsd";

	/** Local location of the XAdES v1.1.1 schema definition */
	public static final String XADES_1_1_1_SCHEMA_LOCATION =
			SCHEMA_ROOT + "XAdES-1.1.1.xsd";

	/** URI of the XAdES v1.1.1 namespace */
	public static final String XADES_1_1_1_NS_URI = "http://uri.etsi.org/01903/v1.1.1#";

	public static final String XADES_1_1_1_NS_PREFIX = "xades111";

	/** Local location of the XAdES v1.2.2 schema definition */
	public static final String XADES_1_2_2_SCHEMA_LOCATION =
			SCHEMA_ROOT + "XAdES-1.2.2.xsd";

	/** URI of the XAdES v1.2.2 namespace */
	public static final String XADES_1_2_2_NS_URI = "http://uri.etsi.org/01903/v1.2.2#";

	public static final String XADES_1_2_2_NS_PREFIX = "xades122";

	/** Local location of the XAdES v1.1.1 schema definition */
	public static final String XADES_1_3_2_SCHEMA_LOCATION =
			SCHEMA_ROOT + "XAdES-1.3.2.xsd";

	/** URI of the XAdES v1.3.2 namespace */
	public static final String XADES_1_3_2_NS_URI = "http://uri.etsi.org/01903/v1.3.2#";

	public static final String XADES_1_3_2_NS_PREFIX = "xades132";

	/** Local location of the XAdES v1.4.1 schema definition */
	public static final String XADES_1_4_1_SCHEMA_LOCATION =
			SCHEMA_ROOT + "XAdES-1.4.1.xsd";

	/** URI of the XAdES v1.4.1 namespace */
	public static final String XADES_1_4_1_NS_URI = "http://uri.etsi.org/01903/v1.4.1#";

	public static final String XADES_1_4_1_NS_PREFIX = "xades141";
	/** URI of the SAML 2.0 namespace. */
	public static final String SAML2_NS_URI =
			"urn:oasis:names:tc:SAML:2.0:assertion";

	/** Prefix used for the SAML 2.0 XML namespace */
	public static final String SAML2_PREFIX = "saml2";

	/** Local location of the SAML 2.0 XML schema definition. */
	public static final String SAML2_SCHEMA_LOCATION =
			SCHEMA_ROOT + "saml-schema-assertion-2.0.xsd";

	/** URI of the SAML 2.0 protocol namespace. */
	public static final String SAML2P_NS_URI =
			"urn:oasis:names:tc:SAML:2.0:protocol";

	/** Prefix used for the SAML 2.0 protocol XML namespace */
	public static final String SAML2P_PREFIX = "saml2p";

	/** Local location of the SAML 2.0 protocol XML schema definition. */
	public static final String SAML2P_SCHEMA_LOCATION =
			SCHEMA_ROOT + "saml-schema-protocol-2.0.xsd";

	/** URI of the STORK namespace. */
	public static final String STORK_NS_URI =
			"urn:eu:stork:names:tc:STORK:1.0:assertion";

	/** Prefix used for the STORK XML namespace */
	public static final String STORK_PREFIX = "stork";

	/** Local location of the STORK XML schema definition. */
	public static final String STORK_SCHEMA_LOCATION =
			SCHEMA_ROOT + "stork-schema-assertion-1.0.xsd";

	/** URI of the STORK protocol namespace. */
	public static final String STORKP_NS_URI =
			"urn:eu:stork:names:tc:STORK:1.0:protocol";

	/** Prefix used for the STORK protocol XML namespace */
	public static final String STORKP_PREFIX = "storkp";

	/** Local location of the STORK protocol XML schema definition. */
	public static final String STORKP_SCHEMA_LOCATION =
			SCHEMA_ROOT + "stork-schema-protocol-1.0.xsd";

	/** URI of the TSL namespace. */
	public static final String TSL_NS_URI =
			"http://uri.etsi.org/02231/v2#";

	/** Prefix used for the TSL namespace */
	public static final String TSL_PREFIX = "tsl1";

	/** Local location of the TSL schema definition. */
	public static final String TSL_SCHEMA_LOCATION =
			SCHEMA_ROOT + "ts_119612v010201_xsd.xsd";

	/** URI of the TSL SIE namespace. */
	public static final String TSL_SIE_NS_URI =
			"http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#";

	/** Prefix used for the TSL SIE namespace */
	public static final String TSL_SIE_PREFIX = "tslsie";

	/** Local location of the TSL SIE schema definition. */
	public static final String TSL_SIE_SCHEMA_LOCATION =
			SCHEMA_ROOT + "ts_119612v010201_sie_xsd.xsd";

	/** URI of the TSL additional types namespace. */
	public static final String TSL_ADDTYPES_NS_URI =
			"http://uri.etsi.org/02231/v2/additionaltypes#";

	/** Prefix used for the TSL additional types namespace */
	public static final String TSL_ADDTYPES_PREFIX = "tsltype";

	/** Local location of the TSL additional types schema definition. */
	public static final String TSL_ADDTYPES_SCHEMA_LOCATION =
			SCHEMA_ROOT + "ts_ts_119612v010201_additionaltypes_xsd.xsd";

	/** URI of the XML Encryption namespace. */
	public static final String XENC_NS_URI =
			"http://www.w3.org/2001/04/xmlenc#";

	/** Prefix used for the XML Encryption XML namespace */
	public static final String XENC_PREFIX = "xenc";

	/** Local location of the XML Encryption XML schema definition. */
	public static final String XENC_SCHEMA_LOCATION =
			SCHEMA_ROOT + "xenc-schema.xsd";

	/** Prefix used for the XML Encryption XML namespace */
	public static final String SAML2_METADATA_PREFIX = "md";

	/** Prefix used for the XML Encryption XML namespace */
	public static final String SAML2_METADATA_URI = "urn:oasis:names:tc:SAML:2.0:metadata";

	/** Local location of the XML Encryption XML schema definition. */
	public static final String SAML2_METADATA_SCHEMA_LOCATION =
			SCHEMA_ROOT + "saml-schema-metadata-2.0.xsd";

	/**
	 * Contains all namespaces and local schema locations for XML schema
	 * definitions relevant for MOA. For use in validating XML parsers.
	 */
	public static final String ALL_SCHEMA_LOCATIONS =
			(MOA_NS_URI + " " + MOA_SCHEMA_LOCATION + " ")
					+ (MOA_CONFIG_NS_URI + " " + MOA_CONFIG_SCHEMA_LOCATION + " ")
					+ (MOA_ID_CONFIG_NS_URI + " " + MOA_ID_CONFIG_SCHEMA_LOCATION + " ")
					+ (SL10_NS_URI + " " + SL10_SCHEMA_LOCATION + " ")
					+ (SL11_NS_URI + " " + SL11_SCHEMA_LOCATION + " ")
					+ (SL12_NS_URI + " " + SL12_SCHEMA_LOCATION + " ")
					+ (ECDSA_NS_URI + " " + ECDSA_SCHEMA_LOCATION + " ")
					+ (PD_NS_URI + " " + PD_SCHEMA_LOCATION + " ")
					+ (SAML_NS_URI + " " + SAML_SCHEMA_LOCATION + " ")
					+ (SAMLP_NS_URI + " " + SAMLP_SCHEMA_LOCATION + " ")
					+ (XML_NS_URI + " " + XML_SCHEMA_LOCATION + " ")
					+ (XSI_NS_URI + " " + XSI_SCHEMA_LOCATION + " ")
					+ (DSIG_NS_URI + " " + DSIG_SCHEMA_LOCATION + " ")
					+ (DSIG_FILTER2_NS_URI + " " + DSIG_FILTER2_SCHEMA_LOCATION + " ")
					+ (DSIG_EC_NS_URI + " " + DSIG_EC_SCHEMA_LOCATION + " ")
					+ (XMLLPR_NS_URI + " " + XMLLPR_SCHEMA_LOCATION + " ")
					+ (XADES_1_1_1_NS_URI + " " + XADES_1_1_1_SCHEMA_LOCATION + " ")
					+ (XADES_1_2_2_NS_URI + " " + XADES_1_2_2_SCHEMA_LOCATION + " ")
					+ (XADES_1_3_2_NS_URI + " " + XADES_1_3_2_SCHEMA_LOCATION + " ")
					+ (XADES_1_4_1_NS_URI + " " + XADES_1_4_1_SCHEMA_LOCATION + " ")
					+ (TSL_NS_URI + " " + TSL_SCHEMA_LOCATION + " ")
					+ (TSL_SIE_NS_URI + " " + TSL_SIE_SCHEMA_LOCATION + " ")
					+ (TSL_ADDTYPES_NS_URI + " " + TSL_ADDTYPES_SCHEMA_LOCATION + " ")
					+ (SAML2_NS_URI + " " + SAML2_SCHEMA_LOCATION + " ")
					+ (SAML2P_NS_URI + " " + SAML2P_SCHEMA_LOCATION + " ")
					+ (STORK_NS_URI + " " + STORK_SCHEMA_LOCATION + " ")
					+ (STORKP_NS_URI + " " + STORKP_SCHEMA_LOCATION + " ")
					+ (SAML2_METADATA_URI + " " + SAML2_METADATA_SCHEMA_LOCATION + " ")
					+ (XENC_NS_URI + " " + XENC_SCHEMA_LOCATION);
}
