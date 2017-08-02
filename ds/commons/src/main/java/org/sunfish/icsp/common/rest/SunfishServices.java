package org.sunfish.icsp.common.rest;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface SunfishServices {

  // PEP
  public static final String PEP_VERSION						= "v1";
  public static final String PEP_ENDPOINT						= "pep/" + PEP_VERSION;
  public static final String PEP_PATH_REQUEST					= "request";
  public static final String PEP_PATH_APP_REQUEST               = "app-request";

  // PDP
  public static final String PDP_VERSION						= "v1";
  public static final String PDP_ENDPOINT						= "pdp/" + PDP_VERSION;
  public static final String PDP_PATH_AUTHORIZATION				= "authorization";
  public static final String PDP_PATH_VERIFY_SERVICE_POLICY		= "verifyServicePolicy";
  public static final String PDP_PATH_VERIFY_SERVICE_POLICY_SET	= "verifyServicePolicySet";


  public static final String PDP_CALL_AUTHORIZATION				= PDP_ENDPOINT + "/" + PDP_PATH_AUTHORIZATION;

  // PRP
  public static final String PRP_VERSION						= "v1";
  public static final String PRP_ENDPOINT						= "prp/" + PRP_VERSION;
  public static final String PRP_PATH_POLICY_BY_ID				= "policy/{id}";
  public static final String PRP_PATH_POLICY_BY_ID_VERSION		= "policy/{id}/{version}";
  public static final String PRP_PATH_POLICYSET_BY_ID			= "policyset/{id}";
  public static final String PRP_PATH_POLICYSET_BY_ID_VERSION	= "policyset/{id}/{version}";
  public static final String PRP_PATHPARAM_ID					= "id";
  public static final String PRP_PATH_COLLECT					= "collect";
  public static final String PRP_PATH_POLICIES_BY_REFERENCE		= "policyRef";

  public static final String PRP_CALL_COLLECT					= PRP_ENDPOINT + "/" + PRP_PATH_COLLECT;
  public static final String PRP_CALL_POLICY_BY_ID				= PRP_ENDPOINT + "/" + PRP_PATH_POLICY_BY_ID;
  public static final String PRP_CALL_POLICYSET_BY_ID			= PRP_ENDPOINT + "/" + PRP_PATH_POLICYSET_BY_ID;
  public static final String PRP_CALL_POLICIES_BY_REFERENCE		= PRP_ENDPOINT + "/" + PRP_PATH_POLICIES_BY_REFERENCE;

  // PAP
  public static final String PAP_VERSION						= "v1";
  public static final String PAP_PATH_POLICYSETS				= "policysets";
  public static final String PAP_PATH_POLICYSETS_BY_ID			= "policysets/{id}/";
  public static final String PAP_PATH_POLICYSETS_BY_ID_VERSION	= "policysets/{id}/{version}";
  public static final String PAP_PATH_POLICIES					= "policies";
  public static final String PAP_PATH_POLICIES_BY_ID			= "policies/{id}/";
  public static final String PAP_PATH_POLICIES_BY_ID_VERSION	= "policies/{id}/{version}";
  public static final String PAP_PATH_ID                        = "{id}";
  public static final String PAP_PATH_ID_VERSION                = "{id}/{version}";
  public static final String PAP_PATH_SERVICE_ID                = "{serviceID}";


  public static final String PAP_CALL_DELETE_POLICYSET			= PRP_ENDPOINT + "/" + PAP_PATH_POLICYSETS_BY_ID_VERSION;
  public static final String PAP_CALL_DELETE_POLICY				= PRP_ENDPOINT + "/" + PAP_PATH_POLICIES_BY_ID_VERSION;

  // PIP
  public static final String PIP_VERSION						= "v1";
  public static final String PIP_PATH_REQUEST                   = "request";
  public static final String PIP_PATH_COLLECT                   = "collect";

  // Header Fields
  public static final String HEADER_EXPIRATION_TIME				= "expirationTime";
  public static final String HEADER_LINK						= "Link";
  public static final String HEADER_ENDPOINT					= "SUNFISH-endpoint";
  public static final String HEADER_ISSUER						= "SUNFISH-issuer";
  public static final String HEADER_AUDIENCE					= "SUNFISH-audience";
  public static final String HEADER_VALIDITY                    = "SUNFISH-validity";
  public static final String HEADER_REQUEST_PARAMETERS			= "SUNFISH-request-parameters";
  public static final String HEADER_SIGNATURE					= "SUNFISH-signature";
  public static final String HEADER_REQUEST_DATA				= "SUNFISH-request-data";
  public static final String HEADER_ACTIVITY_CONTEXT            = "SUNFISH-activity-context";
  public static final String HEADER_REQUEST_REFERENCE           = "SUNFISH-request-reference";
  public static final String HEADER_SERVICE                     = "SUNFISH-service";
  public static final String HEADER_REQUEST                     = "SUNFISH-request";
  public static final String HEADER_RESPONSE_DATA               = "SUNFISH-response-data";
  public static final String HEADER_POLICY_TYPE                 = "SUNFISH-policy-type";

  public static final String COOKIE_NAME_SESSION                = "SUNFISH-user-session";


  // RI
  public static final String RI_PATH_STORE                      = "store";
//  public static final String RI_PATH_UPDATE                     = "update";
  public static final String RI_PATH_DELETE                     = "delete";
  public static final String RI_PATH_SERVICE                    = "polService";
  public static final String RI_PATH_READ                       = "read";


  public static final String HEADER_USER_SESSION                = "SUNFISH-user-session";



  //Query Parameters
  public static final String QUERY_PARAMETER_ROOT_POLICY		= "rootPolicy";
  public static final String QUERY_PARAMETER_ROOT_POLICY_SET    = "rootPolicySet";


  // ....
  public static final String ID_SEPARATOR						= "|";
  public static final String POLICYTYPE_DS						= "DS";

}
