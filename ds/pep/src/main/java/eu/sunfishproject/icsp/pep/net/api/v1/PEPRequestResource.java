package eu.sunfishproject.icsp.pep.net.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sunfishproject.icsp.pep.PEPUtil;
import eu.sunfishproject.icsp.pep.config.PEPConfig;
import eu.sunfishproject.icsp.pep.exceptions.PEPException;
import eu.sunfishproject.icsp.pep.net.api.v1.rest.RestRequestClient;
import org.apache.commons.codec.binary.Base64;
import org.sunfish.icsp.common.session.UserSession;
import eu.sunfishproject.icsp.pep.obligation.ObligationService;
import eu.sunfishproject.icsp.pep.obligation.ObligationServiceStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.*;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.Response;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.NotFoundException;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.*;
import org.sunfish.icsp.common.rest.pdp.AuthorizationResponse;
import org.sunfish.icsp.common.rest.pdp.PDPAdapter;
import org.sunfish.icsp.common.rest.pep.PEPAdapter;
import org.sunfish.icsp.common.rest.pep.RestPEP;
import org.sunfish.icsp.common.rest.pip.PIPAdapter;
import org.sunfish.icsp.common.xacml.XACMLConstants;
import org.sunfish.icsp.common.xacml.XACMLRequestBuilder;
import org.sunfish.icsp.common.xacml.XACMLUtils;
import org.sunfish.icsp.common.xacml.XMLDataTypes;
import org.sunfish.icsp.common.xacml.exceptions.XACMLBuilderException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PEPRequestResource {


    protected String issuer;
    protected SunfishService service;
    protected SunfishRequest request;
    protected String requestParams;
    protected SunfishRequestData requestData;
    protected SunfishSignature signature;
    protected HttpServletResponse servletResponse;
    protected UserSession userSession;



    private static final Logger log = LogManager.getLogger(PEPRequestResource.class);


    public PEPRequestResource(final String issuer, final SunfishService service, final SunfishRequest request,
                              final String requestParams, final SunfishRequestData requestData, final SunfishSignature signature,
                              final UserSession userSession, final HttpServletResponse servletResponse) {
        this.issuer = issuer;
        this.service = service;
        this.request = request;
        this.requestParams = requestParams;
        this.requestData = requestData;
        this.signature = signature;
        this.servletResponse = servletResponse;
        this.userSession = userSession;

        this.initService();
        this.initUserSession();

    }



    protected javax.ws.rs.core.Response handleRequest(final byte[] requestBody) throws ICSPException {

        javax.ws.rs.core.Response response = null;


            try {

                if(!isInZone()) {

                    final PEPAdapter pep = getPEPForZone(this.service.getZone());

                    if(pep != null) {
                        log.info("Forwarding request to pep: " + pep.getName());
                        response = pep.request(issuer, service, request, requestParams, requestData, signature, requestBody);
                    }
                }

                else {

                    SunfishHttpObject sunfishHttpObject = executeRequest(requestBody);
                    sunfishHttpObject = prepareResponse(sunfishHttpObject);

                    final String responseSignature = "response-sig";
                    servletResponse.setHeader(SunfishServices.HEADER_SIGNATURE, responseSignature);

                    response = PEPUtil.buildHttpResponse(sunfishHttpObject);

                }

            } catch (final XACMLBuilderException e) {
                log.error("Could not build XAMLC Request!", e);
                throw new PEPException("Could not build XACML Request", javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);
            }

        return response;
    }



    protected SunfishHttpObject executeRequest(final byte[] requestBody) throws ICSPException, XACMLBuilderException {

        final MultivaluedMap<String, Object> headers = this.requestData.getMultivaluedHeaders();
        final String queryString = this.requestData.getQueryString();

        final SunfishHttpObject sunfishHttpObject = new SunfishHttpObject(-1, headers, null, queryString, requestBody);

        final Request request = PEPUtil.buildPDPAuthorization(this.request, this.service, sunfishHttpObject, this.userSession, PEPConfig.getInstance().getZone());

        //String reuqestString = CommonUtil.xacmlRequestToString(request);

        final AuthorizationResponse authResponse = performAuthorization(request, this.service.getId(), SunfishServices.POLICYTYPE_DS);

        final Response response = authResponse.getResponse();
        final Collection<Result> results = response.getResults();

        if(results.isEmpty()) {
            throw new PEPException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);
        }


        for(final Result decisionResult : results) {

            final Decision decision = decisionResult.getDecision();
            log.debug("Decision: " + decision.name(), ", Result: " + decision.toString());


            final Collection<Obligation> obligations = decisionResult.getObligations();
            handleObligations(obligations, sunfishHttpObject);


            // TODO: Just for debug!
            // TODO: What happens if multiple decisions?
            if (decision.equals(Decision.PERMIT)) {

                final RestRequestClient requestClient = new RestRequestClient(this.request, getTargetHost(), this.requestData, sunfishHttpObject.getBody());
                final javax.ws.rs.core.Response result = requestClient.request();


                return PEPUtil.buildSunfishHttpObject(result);


            } else if(decision.equals(Decision.INDETERMINATE)) {

                sunfishHttpObject.setResponseCode(javax.ws.rs.core.Response.Status.EXPECTATION_FAILED.getStatusCode());
                return sunfishHttpObject;
                //throw new PolicyException(javax.ws.rs.core.Response.Status.EXPECTATION_FAILED);
            }  else {

                sunfishHttpObject.setResponseCode(javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode());
                return sunfishHttpObject;
            }

        }

        throw new PEPException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);

    }

    protected SunfishHttpObject prepareResponse(final SunfishHttpObject responseToPrepare) throws ICSPException, XACMLBuilderException {


        final SunfishResponse sunfishResponse = new SunfishResponse();
        final MultivaluedMap<String, Object> headers = responseToPrepare.getHeaders();
        final String queryString = this.requestData.getQueryString();
        sunfishResponse.setResponseCode(responseToPrepare.getResponseCode());

        final Request request = PEPUtil.buildPDPAuthorization(sunfishResponse, this.service, this.request, responseToPrepare, this.userSession);


        final AuthorizationResponse authResponse = performAuthorization(request, this.service.getId(), SunfishServices.POLICYTYPE_DS);

        final Response response = authResponse.getResponse();
        final Collection<Result> results = response.getResults();

        if(results.isEmpty()) {
            throw new PEPException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);
        }


        for(final Result decisionResult : results) {

            final Decision decision = decisionResult.getDecision();
            log.debug("Decision: " + decision.name(), ", Result: " + decision.toString());

            final Collection<Obligation> obligations = decisionResult.getObligations();
            handleObligations(obligations, responseToPrepare);

            // TODO: Just for debug!
            // TODO: What happens if multiple decisions?
            if (decision.equals(Decision.PERMIT)) {
                return responseToPrepare;

            } else if(decision.equals(Decision.INDETERMINATE)) {
                //throw new PolicyException(javax.ws.rs.core.Response.Status.EXPECTATION_FAILED);
                responseToPrepare.setResponseCode(javax.ws.rs.core.Response.Status.EXPECTATION_FAILED.getStatusCode());
                return responseToPrepare;
            }  else {
                //throw new PolicyException(javax.ws.rs.core.Response.Status.FORBIDDEN);
                responseToPrepare.setResponseCode(javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode());
                return responseToPrepare;
            }


        }

        throw new PEPException(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);


    }





    protected AuthorizationResponse performAuthorization(final Request request, final String serviceID, final String policyType) throws ICSPException {


        final PDPAdapter pdp = PEPConfig.getInstance().getPDP();

        // TODO: Set to correct values
        final String reqReference = "";
        final String requestSignature = "signature-123";

        final AuthorizationResponse authResponse = pdp.authorization(requestSignature, reqReference, request, serviceID, policyType);

        return authResponse;

    }




    protected String getTargetHost() throws ICSPException {

        String targetHost = "";


        try {
            XACMLRequestBuilder requestBuilder = new XACMLRequestBuilder();

            requestBuilder = requestBuilder.setCategory(XACMLConstants.CATEGORY_SERVICE)
                    .addStringAttribute(XACMLConstants.ATTRIBUTE_ID, this.service.getId())
                    .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_HOST, this.service.getHost())
                    .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_ZONE, this.service.getZone());

            final Request request = requestBuilder.build();

            final AttributeDesignator requestedAttribute = XACMLUtils.createAttributeDesignator(
                    XACMLConstants.ATTRIBUTE_TARGET_HOST,
                    XACMLConstants.CATEGORY_TARGET,
                    XMLDataTypes.XML_TYPE_STRING);


            final List<AttributeDesignator> attributeDesignators = new ArrayList<>();
            attributeDesignators.add(requestedAttribute);

            final SunfishPIPRequest pipRequest = new SunfishPIPRequest(request, attributeDesignators);


            final PIPAdapter pipAdapter = getPIPForRequest(pipRequest);


            if(pipAdapter != null) {

                final Request response = pipAdapter.request(pipRequest);
                if (response != null) {
                    final Iterator<RequestAttributes> iterator =
                            response.getRequestAttributes(new IdentifierImpl(XACMLConstants.CATEGORY_TARGET));

                    if (iterator.hasNext()) {
                        final RequestAttributes category = iterator.next();
                        final Collection<Attribute> attributes = category.getAttributes();
                        for (final Attribute attribute : attributes) {
                            if (attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_TARGET_HOST)) {
                                final Collection<AttributeValue<?>> values = attribute.getValues();
                                for (final AttributeValue<?> value : values) {
                                    targetHost = (String) value.getValue();
                                }
                            }
                        }
                    }

                } else {
                    log.error("PIP did not reply with correct request.");
                }

            }


        } catch (final XACMLBuilderException e) {
            log.error("Could not generate xacml");
        }

        if(targetHost == null || targetHost.isEmpty() ) {
            throw new NotFoundException();
        }

        return targetHost;

    }


    private void initService() {

        final String pepZone = PEPConfig.getInstance().getZone();


        if(this.service.getZone() == null) {
            String zone = getZoneForServiceId(this.service.getId());
            if(zone == null || zone.isEmpty()) {
                // TODO: Just for debug?
                zone = pepZone;
                //throw new InvalidRequestException();
            }
            this.service.setZone(zone);
        }
    }

    private void initUserSession() {

        if(this.userSession != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String userSessionString = objectMapper.writeValueAsString(this.userSession);
                String base64 = Base64.encodeBase64String(userSessionString.getBytes(StandardCharsets.UTF_8));
                this.requestData.getHeaders().put(SunfishServices.HEADER_USER_SESSION, base64);
            } catch (Exception e) {
                log.error("Could not write user session header", e);
            }
        }
    }


    private boolean isInZone() throws ICSPException {
        final String pepZone = PEPConfig.getInstance().getZone();

        return pepZone.equals(this.service.getZone());
    }


    private PIPAdapter getPIPForRequest(final SunfishPIPRequest pipRequest) {

        for(final PIPAdapter pipAdapter : PEPConfig.getInstance().getPIPs()) {
            if(pipAdapter.matches(pipRequest)){
                return pipAdapter;
            }
        }

        return null;

    }


    private PEPAdapter getPEPForZone(final String zone) throws ICSPException {

        // TODO: Implement Caching

        String host = null;


        try {
            XACMLRequestBuilder requestBuilder = new XACMLRequestBuilder();

            requestBuilder = requestBuilder.setCategory(XACMLConstants.CATEGORY_SERVICE)
                    .addStringAttribute(XACMLConstants.ATTRIBUTE_SERVICE_ZONE, zone);

            final Request request = requestBuilder.build();

            final AttributeDesignator requestedAttribute = XACMLUtils.createAttributeDesignator(
                    XACMLConstants.ATTRIBUTE_SERVICE_PEP,
                    XACMLConstants.CATEGORY_PEP,
                    XMLDataTypes.XML_TYPE_STRING);


            final List<AttributeDesignator> attributeDesignators = new ArrayList<>();
            attributeDesignators.add(requestedAttribute);

            final SunfishPIPRequest pipRequest = new SunfishPIPRequest(request, attributeDesignators);


            final PIPAdapter pipAdapter = getPIPForRequest(pipRequest);


            if(pipAdapter != null) {

                final Request response = pipAdapter.request(pipRequest);
                if (response != null) {
                    final Iterator<RequestAttributes> iterator =
                            response.getRequestAttributes(new IdentifierImpl(XACMLConstants.CATEGORY_PEP));

                    if (iterator.hasNext()) {
                        final RequestAttributes category = iterator.next();
                        final Collection<Attribute> attributes = category.getAttributes();
                        for (final Attribute attribute : attributes) {
                            if (attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_SERVICE_PEP)) {
                                final Collection<AttributeValue<?>> values = attribute.getValues();
                                for (final AttributeValue<?> value : values) {
                                    host = (String) value.getValue();
                                }
                            }
                        }
                    }

                } else {
                    log.error("PIP did not reply with correct response.");
                }

            }


        } catch (final XACMLBuilderException e) {
            log.error("Could not generate xacml");
        }

        if(host == null || host.isEmpty() ) {
            throw new NotFoundException();
        }

        return new RestPEP(host);

    }



    private String getZoneForServiceId(final String id) {


        String zone = null;


        try {
            XACMLRequestBuilder requestBuilder = new XACMLRequestBuilder();

            requestBuilder = requestBuilder.setCategory(XACMLConstants.CATEGORY_SERVICE)
                    .addStringAttribute(XACMLConstants.ATTRIBUTE_ID, id);

            final Request request = requestBuilder.build();

            final AttributeDesignator requestedAttribute = XACMLUtils.createAttributeDesignator(
                    XACMLConstants.ATTRIBUTE_TARGET_ZONE,
                    XACMLConstants.CATEGORY_TARGET,
                    XMLDataTypes.XML_TYPE_STRING);


            final List<AttributeDesignator> attributeDesignators = new ArrayList<>();
            attributeDesignators.add(requestedAttribute);

            final SunfishPIPRequest pipRequest = new SunfishPIPRequest(request, attributeDesignators);


            final PIPAdapter pipAdapter = getPIPForRequest(pipRequest);


            if(pipAdapter != null) {

                final Request response = pipAdapter.request(pipRequest);
                if (response != null) {
                    final Iterator<RequestAttributes> iterator =
                            response.getRequestAttributes(new IdentifierImpl(XACMLConstants.CATEGORY_TARGET));

                    if (iterator.hasNext()) {
                        final RequestAttributes category = iterator.next();
                        final Collection<Attribute> attributes = category.getAttributes();
                        for (final Attribute attribute : attributes) {
                            if (attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_TARGET_ZONE)) {
                                final Collection<AttributeValue<?>> values = attribute.getValues();
                                for (final AttributeValue<?> value : values) {
                                    zone = (String) value.getValue();
                                }
                            }
                        }
                    }

                } else {
                    log.error("PIP did not reply with correct response.");
                }

            }


        } catch (final XACMLBuilderException e) {
            log.error("Could not generate xacml");
        } catch (final ICSPException e) {
            e.printStackTrace();
        }

        return zone;

    }




    private void handleObligations(final Collection<Obligation> obligations, final SunfishHttpObject sunfishHttpObject) throws ICSPException {

        for(final Obligation obligation : obligations) {

            final ObligationService service = ObligationServiceStore.getInstance().getObligationServiceForObligation(obligation.getId().stringValue());
            if(service == null) {
                throw new PEPException("Could not find appropriate Obligation service", javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);
            }
            service.handleObligation(obligation, sunfishHttpObject);

        }
    }



}
