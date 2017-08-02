package org.sunfish.icsp.common.swagger;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class Documentation {


    public static final String PEP_DESCRIPTION                  = "The interactions executed inside one zone are checked by and enforced in the scope of a PEP assigned for that zone. The approach is similar for the zones that consist of geographically dispersed locations: each PEP (or sub-PEP) is responsible for its geographical unit or layer. Being the single point of contact of a zone, the PEP is primarily responsible for checking incoming and outgoing requests. In the second instance, depending on security settings and application requirements, PEP might serve as an inter-zone communication gateway, as well.";
    public static final String PDP_DESCRIPTION                  = "This API is primarily used by adjacent PEPs to issue authorization requests for intra-zone and cross-zone interactions. In this specification we partially rely on the REST profile suggested by the OASIS XACML Standard";
    public static final String PRP_DESCRIPTION                  = "The PRP is not included in the OASIS XACML standard, but provides another abstraction level of the PAP";
    public static final String PIP_DESCRIPTION                  = "The PIP is generally defined as “the system entity that acts as a source of attribute values";
    public static final String PAP_DESCRIPTION                  = "The PAP interface follows a straight forward REST interface, as it requires bare access to the policy storage.";

//  PEP
    public static final String APP_REQUEST                      = "Applications can POST new requests to this endpoint. Inputs to this endpoint are contextual parameters that establish the request, application and target specific settings. For this specification, the applications rely on common SUNFISH functionalities and components. The response of this action is the original response of the target service (synchronous use case).";
    public static final String INTER_REQUEST                    = "This endpoint is used by PEPs to POST new requests to other PEPs. Inputs to this endpoint are contextual parameters that establish the request, application and target specific settings. The response of this action is the data record that contains request id and data structure describing status parameters or other PEP requirements.";
    public static final String HEADER_ISSUER                    = "References the application that issued the request. This field may include the data required to perform application authentication, in the form of authentication token.";
    public static final String HEADER_SERVICE                   = "Machine-readable description of endpoint including at least an identifier of the service. With the service id, the PEP can resolve other required attributes.";
    public static final String HEADER_POLICY_TYPE               = "The type of the policy";
    public static final String HEADER_REQUEST                   = "Machine-readable description of the target endpoint and request data. The PEP at least requires the parameters method, port, path and protocol. If additional attributes are registered in the SUNFISH federation, the PEP can retrieve these attributes from a corresponding PIP. Furthermore, this field may include validity constraints on a request (not-valid-before, not valid-after).";
    public static final String HEADER_REQUEST_PARAMETERS        = "The parameters related to the request, including its priority, SLA requirements, call-back URI. This field includes other request meta-data that may extend or override the definitions provided in centralized administrative console. These include request type, application-specific policies or obligations to be applied beyond the ones defined in the central console, or parameters related to data-masking policies. The scope of applicable and allowed definitions provided in this variable depends on an extent of delegation policies, as determined in centralized console.";
    public static final String HEADER_REQUEST_DATA              = "This field encapsulates the original header data and the original query string as issued by the application.";
    public static final String HEADER_SIGNATURE                 = "This parameter is used to ensure integrity and authenticity of the source message for applications which require a higher degree of security. It contains signed request and fields, according to predefined schema";
    public static final String HEADER_ACTIVITY_CONTEXT          = "Includes the signed PDP decision aimed at related audience (both PEPs taking part in transaction) in the case where activity context is applied to allow access decisions to span across several interactions and entities.";
    public static final String HEADER_RESPONSE_DATA             = "This field encapsulates the original header data and the original status code as returned by the service.";
    public static final String BODY                             = "Body of the original request";
    public static final String BODY_TARGET                      = "The same response as provided by the target service";

    public static final String EXAMPLE_REQUEST_DATA             = "{\\n  “QueryString\" : “<original query string, starting with ‘?’>\",\\n  “Headers\" : {\\n    “<Original header name>\" : “<Original header value>\",\\n    …\\n\\n}";


//  PDP
    public static final String PDP_ENTRY                        = "API entry point. This point is used to identify functionality and endpoints provided by PDP.";
    public static final String AUTHORIZATION                    = "This endpoint is used by PEPs to issue authorization decision requests to PDP. These requests are sent using POST method. Inputs to this endpoint are parameters that describe access requests initiated by entities interacting through the calling PEP. Additionally, this request contains other contextual parameters that can be used by PDP to evaluate request.";
    public static final String PDP_ENTRY_RESPONSE               = "The response contains a resource with link relation http://docs.oasis-open.org/ns/xacml/relation/pdp and a valid URL.";
    public static final String PDP_REQUEST                      = "Contains XACML-formatted (or other) request with all relevant data and attributes necessary for PDP to perform authorization decision.";
    public static final String PDP_HEADER_SIGNATURE             = "This field is used to provide integrity and authenticity of messages.";
    public static final String PDP_AUTH_400                     = "Invalid XACML request";
    public static final String PDP_AUTH_403                     = "Requestor is not allowed to perform the request";
    public static final String PDP_BODY                         = "Contains complete XACML-formatted answer. Body can include additional answer that deals with activity context, if requested.";
    public static final String PDP_VERIFY_SERVICE_POLICY        = "Verify a service policy";
    public static final String PDP_VERIFY_SERVICE_POLICY_SET    = "Verify a service policy set";
    public static final String PDP_VERIFY_POLICY_RESULT_STATUS  = "Indicates the status of the verification operation.";
    public static final String PDP_VERIFY_POLICY_RESULT_DESCRIPTION  = "Description, containing detailed information about the requested operation.";
    public static final String PDP_VERIFY_POLICY_RESULT_STATUS_CODE  = "Status code of the operation.";
    public static final String PDP_VERIFY_POLICY_BODY           = "Contains XACML-formatted policy for PDP to perform verification.";
    public static final String PDP_VERIFY_POLICY_200            = "Contains information about the verification result.";
    public static final String PDP_VERIFY_POLICY_400            = "Invalid request";
    public static final String PDP_VERIFY_POLICY_403            = "The requestor is not allowed";
    public static final String PDP_VERIFY_POLICY_SET_BODY       = "Contains XACML-formatted policy set for PDP to perform verification.";






    //  PRP
    public static final String PRP_HEADER_EXPIRATION            = "Specifies the expiration time of the policy set in milliseconds starting from midnight, January 1, 1970 UTC. After this time the policy set must not be used.";
    public static final String PRP_COLLECT                      = "This endpoint is used by PDPs to retrieve a collection of policies for a specified decision request.";
    public static final String PRP_COLLECT_200                  = "Contains a single policy set where all policies are contained or references according to the XACML policy set schema.";
    public static final String PRP_COLLECT_400                  = "Invalid request";
    public static final String PRP_COLLECT_404                  = "No policies matching the specified request were found";
    public static final String PRP_POLICYSET_ID_VERSION         = "This endpoint is used to retrieve a policy by id. Optionally a version can be specified.";
    public static final String PRP_POLICYSET_ID_VERSION_200     = "Contains the requested policy set";
    public static final String PRP_POLICYSET_ID_VERSION_400     = "Invalid request";
    public static final String PRP_POLICYSET_ID_VERSION_403     = "The requestor is not allowed to retrieve this policy";
    public static final String PRP_POLICYSET_ID_VERSION_404     = "The policy set with the specified id was not found";
    public static final String PRP_POLICY_ID_VERSION_200        = "Contains the requested policy set";
    public static final String PRP_POLICY_ID_VERSION_400        = "Invalid request";
    public static final String PRP_POLICY_ID_VERSION_403        = "The requestor is not allowed to retrieve this policy";
    public static final String PRP_POLICY_ID_VERSION_404        = "The policy set with the specified id was not found";
    public static final String PRP_POLICY_ID_VERSION            = "This endpoint is used to retrieve a policy by id. Optionally a version can be specified.";
    public static final String PRP_COLLECT_PARM_BODY            = "Contains the request formatted according to the XACML decision request language with all relevant data and attributes necessary for the PRP to identify the relevant policies.";
    public static final String PRP_PATH_PARM_POLICYSET_ID       = "Specifies the id of the policy set to be returned in the response.";
    public static final String PRP_PATH_PARM_POLICYSET_VERSION  = "Specifies the version of the policy set to be returned in the response. If no version is specified the newest policy set will be returned.";
    public static final String PRP_PATH_PARM_POLICY_ID          = "Specifies the id of the policy to be returned in the response.";
    public static final String PRP_PATH_PARM_POLICY_VERSION     = "Specifies the version of the policy to be returned in the response. If no version is specified the newest policy will be returned.";
    public static final String PRP_QUERY_PARAM_ROOT_POLICY_SET  = "true|false Defines if a root policy-set or a re-usable policies-set should be returned.";
    public static final String PRP_QUERY_PARAM_ROOT_POLICY      = "true|false Defines if a root policy or a re-usable policy should be returned.";

//  PIP
    public static final String PIP_REQUEST                      = "This endpoint is used to retrieve additional attributes";
    public static final String PIP_HEADER_ISSUER                = "References the entity that issued the request. This field includes the data that confirms the authentication of source entity and its authentication level.";
    public static final String PIP_REQUEST_BODY                 = "Contains the requested attributes and the request context as issued by the PEP. If multiple PIPs are involved, the PIP always receive the most recent request context.";
    public static final String PIP_REQUEST_200                  = "The request context was enhanced with all or some of the requested attributes.";
    public static final String PIP_REQUEST_400                  = "Invalid request";
    public static final String PIP_REQUEST_403                  = "The requestor is not allowed";
    public static final String PIP_REQUEST_404                  = "This PIP does not provide any of the requested attributes.";
    public static final String PIP_COLLECT                      = "This endpoint is used to retrieve a collection of all available attribute ids";
    public static final String PIP_COLLECT_200                  = "Contains a collection of attribute designators ids according to the attribute designator set schema.";
    public static final String PIP_COLLECT_400                  = "Invalid request";
    public static final String PIP_COLLECT_403                  = "The requestor is not allowed";


//  PAP
    public static final String PAP_HEADER_ISSUER                = "References the entity that issued the request. This field may include the data that confirms the authentication of source entity and its authentication level.";
    public static final String PAP_GET_POLICIES                 = "This endpoint is used by entities interfacing with the PAP to retrieve policies";
    public static final String PAP_GET_POLICIES_200             = "The body of the response contains the requested policies according to the schema defined in Listing 3.";    public static final String PAP_GET_POLICIES_400             = "Invalid request";
    public static final String PAP_GET_POLICIES_403             = "The requestor is not allowed to perform this operation";
    public static final String PAP_GET_POLICIES_404             = "No policies matching the specified request were found";
    public static final String PAP_GET_POLICIES_SERVICE_ID      = "Only policies for the referenced service will be returned.";
    public static final String PAP_GET_POLICIES_POLICY_TYPE     = "The requested policy type";
    public static final String PAP_ADD_POLICIES                 = "This endpoint is used by entities interfacing with the PAP to add a policy";
    public static final String PAP_ADD_POLICIES_BODY            = "The body of the request contains a to be added policy according to the schema in Listing 1.";
    public static final String PAP_ADD_POLICIES_200             = "Created successful";
    public static final String PAP_ADD_POLICIES_400             = "Invalid request";
    public static final String PAP_ADD_POLICIES_403             = "The requestor is not allowed to perform this operation";
    public static final String PAP_ADD_POLICIES_409             = "The policy exists already";
    public static final String PAP_ADD_POLICIES_SERVICE_ID      = "Policies will be added to the references service.";
    public static final String PAP_ADD_POLICIES_POLICY_TYPE     = "The policy type of the policy to be added";
    public static final String PAP_ADD_POLICIES_EXPIRATION_TIME = "The expiration time of the policy";
//    public static final String PAP_UPDATE_POLICIES              = "This endpoint is used by entities interfacing with the PAP to update a policy";
//    public static final String PAP_UPDATE_POLICIES_200          = "Updated successful";
//    public static final String PAP_UPDATE_POLICIES_400          = "Invalid request";
//    public static final String PAP_UPDATE_POLICIES_403          = "The requestor is not allowed to perform this operation";
//    public static final String PAP_UPDATE_POLICIES_404          = "Policy to update not found";
    public static final String PAP_DELETE_POLICIES              = "This endpoint is used by entities to remove policies";
    public static final String PAP_DELETE_POLICIES_200          = "Deleted successful";
    public static final String PAP_DELETE_POLICIES_400          = "Invalid request";
    public static final String PAP_DELETE_POLICIES_403          = "The requestor is not allowed to perform this operation";
    public static final String PAP_DELETE_POLICIES_404          = "Policy not found";
    public static final String PAP_DELETE_POLICIES_ID           = "Id of the policy to delete";
    public static final String PAP_DELETE_POLICIES_VERSION      = "Specifies the version of the policy to be deleted";




}
