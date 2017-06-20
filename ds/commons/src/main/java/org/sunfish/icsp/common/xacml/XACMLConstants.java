package org.sunfish.icsp.common.xacml;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class XACMLConstants {


    // Categories
    public static final String CATEGORY_ISSUER                        = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
    public static final String CATEGORY_ENDPOINT                      = "urn:oasis:names:tc:xacml:3.0:attribute-category:resource";
    public static final String CATEGORY_AUDIENCE                      = "urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject";
    public static final String CATEGORY_REQUEST_PARAMETERS            = "urn:sunfish:attribute-category:data";
    public static final String CATEGORY_REQUEST_DATA                  = "urn:sunfish:attribute-category:request-data";
    public static final String CATEGORY_REQUEST                       = "urn:sunfish:attribute-category:request";
    public static final String CATEGORY_RESPONSE                      = "urn:sunfish:attribute-category:response";
    public static final String CATEGORY_SERVICE                       = "urn:sunfish:attribute-category:service";
    public static final String CATEGORY_APPLICATION                   = "urn:sunfish:attribute-category:application";
    public static final String CATEGORY_TARGET                        = "urn:sunfish:attribute-category:target";
    public static final String CATEGORY_PEP                           = "urn:sunfish:attribute-category:pep";
    public static final String CATEGORY_IDENTITY                      = "urn:sunfish:attribute-category:identity";


    // Attributes
    public static final String ATTRIBUTE_ID                           = "urn:sunfish:attribute:id";

    public static final String ATTRIBUTE_ISSUER_ID                    = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    public static final String ATTRIBUTE_ISSUER_HOST                  = "urn:sunfish:attribute:subject:host";
    public static final String ATTRIBUTE_ISSUER_SOURCE_ZONE           = "urn:sunfish:attribute:subject:source-zone";
    public static final String ATTRIBUTE_ISSUER_PEP_ID                = "urn:sunfish:attribute:subject:pep-id";
    public static final String ATTRIBUTE_ISSUER_TOKEN                 = "urn:sunfish:attribute:subject:token";

    public static final String ATTRIBUTE_REQUEST_METHOD               = "urn:sunfish:attribute:request:method";
    public static final String ATTRIBUTE_REQUEST_HOST                 = "urn:sunfish:attribute:request:host";
    public static final String ATTRIBUTE_REQUEST_PATH                 = "urn:sunfish:attribute:request:path";
    public static final String ATTRIBUTE_REQUEST_PORT                 = "urn:sunfish:attribute:request:port";
    public static final String ATTRIBUTE_REQUEST_PROTOCOL             = "urn:sunfish:attribute:request:protocol";
    public static final String ATTRIBUTE_REQUEST_CONTENT_TYPE         = "urn:sunfish:attribute:request:content-type";
    public static final String ATTRIBUTE_REQUEST_HEADER_PARAMETER     = "urn:sunfish:attribute:request:header-parameter";
    public static final String ATTRIBUTE_REQUEST_URL_PARAMETERS       = "urn:sunfish:attribute:request:url-parameters";
    public static final String ATTRIBUTE_REQUEST_BODY_DATA            = "urn:sunfish:attribute:request:body-data";
    public static final String ATTRIBUTE_REQUEST_NOT_VALID_BEFORE     = "urn:sunfish:attribute:request:not-valid-before";
    public static final String ATTRIBUTE_REQUEST_NOT_VALID_AFTER      = "urn:sunfish:attribute:request:not-valid-after";

    public static final String ATTRIBUTE_RESPONSE_CONTENT_TYPE        = "urn:sunfish:attribute:response:content-type";
    public static final String ATTRIBUTE_RESPONSE_CODE                = "urn:sunfish:attribute:response:response-code";
    public static final String ATTRIBUTE_RESPONSE_HEADER_PARAMETER    = "urn:sunfish:attribute:response:header-parameter";
    public static final String ATTRIBUTE_RESPONSE_ENFORCED_ACTION     = "urn:sunfish:attribute:response:enforced-action";
    public static final String ATTRIBUTE_RESPONSE_BODY_DATA           = "urn:sunfish:attribute:response:body-data";
    public static final String ATTRIBUTE_RESPONSE_NOT_VALID_BEFORE     = "urn:sunfish:attribute:response:not-valid-before";
    public static final String ATTRIBUTE_RESPONSE_NOT_VALID_AFTER      = "urn:sunfish:attribute:response:not-valid-after";

    public static final String ATTRIBUTE_SERVICE_HOST                 = "urn:sunfish:attribute:service:host";
    public static final String ATTRIBUTE_SERVICE_ZONE                 = "urn:sunfish:attribute:service:zone";

    public static final String ATTRIBUTE_APPLICATION_HOST             = "urn:sunfish:attribute:application:host";
    public static final String ATTRIBUTE_APPLICATION_ZONE             = "urn:sunfish:attribute:application:zone";

    public static final String ATTRIBUTE_TARGET_BODY                  = "urn:sunfish:attribute:target:body";
    public static final String ATTRIBUTE_TARGET_HEADER                = "urn:sunfish:attribute:target:header";
    public static final String ATTRIBUTE_TARGET_QUERY_STRING          = "urn:sunfish:attribute:target:query-string";
    public static final String ATTRIBUTE_TARGET_HOST                  = "urn:sunfish:attribute:target:host";
    public static final String ATTRIBUTE_TARGET_ZONE                  = "urn:sunfish:attribute:target:zone";

    public static final String ATTRIBUTE_TENANT_ROLE                  = "urn:sunfish:attribute:tenant-role";


    public static final String ATTRIBUTE_SERVICE_PEP                  = "urn:sunfish:attribute:service:pep";

    public static final String ATTRIBUTE_AUDIENCE_TARGET_ZONE         = "urn:sunfish:attribute:subject:target-zone";
    public static final String ATTRIBUTE_AUDIENCE_TARGET_SERVICE      = "urn:sunfish:attribute:subject:target-service";

    public static final String ATTRIBUTE_REQUEST_PARAMETERS_DATA_TAGS = "urn:sunfish:attribute:data:tag";
    public static final String ATTRIBUTE_REQUEST_DATA_HEADER          = "urn:sunfish:attribute:request-data";

    public static final String OBLIGATION_ID_MASK                     = "urn:sunfish:obligation:id:mask";
    public static final String OBLIGATION_MASKING_POLICY_ID           = "urn:sunfish:obligation:masking-policy-id";
    public static final String OBLIGATION_MASKING_CONTEXT_USER        = "urn:sunfish:obligation:masking-context:user";

    public static final String OBLIGATION_ID_MASK_JSON                = "urn:sunfish:obligation:id:MASK-JSON";
    public static final String OBLIGATION_ID_MASK_CSV                 = "urn:sunfish:obligation:id:MASK-CSV";
    public static final String OBLIGATION_ID_MASK_XML                 = "urn:sunfish:obligation:id:MASK-XML";
    public static final String OBLIGATION_ID_MASK_TEXT                = "urn:sunfish:obligation:id:MASK-TEXT";

    public static final String OBLIGATION_ID_AUTHENTICATE             = "urn:sunfish:attribute:obligation:authenticate";



}
