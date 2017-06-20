/**
 *
 */
package eu.sunfishproject.icsp.pdp.net.api.v1;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.Response;
import org.apache.openaz.xacml.api.pdp.PDPEngine;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.util.FactoryException;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorComparator;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.pip.PIPAdapter;
import org.sunfish.icsp.common.swagger.Documentation;

import eu.sunfishproject.icsp.pdp.PDPConfig;
import eu.sunfishproject.icsp.pdp.PDPFactory;
import eu.sunfishproject.icsp.pdp.exceptions.PDPException;
import eu.sunfishproject.icsp.pdp.model.VerifyPolicyResult;
import eu.sunfishproject.icsp.pdp.net.PRPAdapter;
import eu.sunfishproject.icsp.pdp.pip.MissingAttributeCollector;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@Api
@Path(SunfishServices.PDP_VERSION)
public class PDPResource {

  private static final Logger log = LogManager.getLogger(PDPResource.class);

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Path("helo")
  @ApiOperation(hidden = true, value = "hello")
  public String helo() {
    return "HELO";
  }

  @POST
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path("/echo")
  @ApiOperation(hidden = true, value = "echo")
  public Request echo(final Request request) {
    return request;

  }

  @GET
  @Produces(ExtendedMediaType.APPLICATION_HOME_XML)
  @ApiOperation(value = Documentation.PDP_ENTRY, nickname = "pdp-entry")
  @ApiResponses(value = @ApiResponse(code = 200, message = Documentation.PDP_ENTRY_RESPONSE, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_SIGNATURE, description = Documentation.PDP_HEADER_SIGNATURE, response = String.class)))
  public String get() {
    return "<?xml version=\"1.0\"?>\n<resources xmlns=\"http://ietf.org/ns/home-documents\" >\n"
        + "<resource rel=\"http://docs.oasis-open.org/ns/xacml/relation/pdp\">\n<link href=\""
        + SunfishServices.PDP_PATH_AUTHORIZATION + "\"/>\n" + "</resource>\n</resources>";
  }

  @POST
  @Produces(ExtendedMediaType.APPLICATION_JSON)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path(SunfishServices.PDP_PATH_VERIFY_SERVICE_POLICY)
  @ApiOperation(value = Documentation.PDP_VERIFY_SERVICE_POLICY, nickname = "verify-service-policy")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Documentation.PDP_VERIFY_POLICY_200, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_SIGNATURE, description = Documentation.PDP_HEADER_SIGNATURE, response = String.class), response = VerifyPolicyResult.class),
      @ApiResponse(code = 400, message = Documentation.PDP_VERIFY_POLICY_400),
      @ApiResponse(code = 404, message = Documentation.PDP_VERIFY_POLICY_403)

  })
  public VerifyPolicyResult verifyServicePolicy(
      @ApiParam(value = Documentation.PDP_HEADER_SIGNATURE, required = false) @HeaderParam(SunfishServices.HEADER_SIGNATURE) final String signature,
      @ApiParam(value = Documentation.PDP_VERIFY_POLICY_BODY, required = true) final Request request)
      throws PDPException {

    return null;

  }

  @POST
  @Produces(ExtendedMediaType.APPLICATION_JSON)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path(SunfishServices.PDP_PATH_VERIFY_SERVICE_POLICY_SET)
  @ApiOperation(value = Documentation.PDP_VERIFY_SERVICE_POLICY_SET, nickname = "verify-service-policy-set")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Documentation.PDP_VERIFY_POLICY_200, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_SIGNATURE, description = Documentation.PDP_HEADER_SIGNATURE, response = String.class), response = VerifyPolicyResult.class),
      @ApiResponse(code = 400, message = Documentation.PDP_VERIFY_POLICY_400),
      @ApiResponse(code = 404, message = Documentation.PDP_VERIFY_POLICY_403)

  })
  public VerifyPolicyResult verifyServicePolicySet(
      @ApiParam(value = Documentation.PDP_HEADER_SIGNATURE, required = false) @HeaderParam(SunfishServices.HEADER_SIGNATURE) final String signature,
      @ApiParam(value = Documentation.PDP_VERIFY_POLICY_SET_BODY, required = true) final Request request)
      throws PDPException {

    return null;

  }

  @POST
  @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
  @Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
  @Path(SunfishServices.PDP_PATH_AUTHORIZATION)
  @ApiOperation(value = Documentation.AUTHORIZATION, nickname = "authorization")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = Documentation.PDP_BODY, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_SIGNATURE, description = Documentation.PDP_HEADER_SIGNATURE, response = String.class), response = Response.class),
      @ApiResponse(code = 400, message = Documentation.PDP_AUTH_400),
      @ApiResponse(code = 404, message = Documentation.PDP_AUTH_403)

  })
  public Response authorize(
      @ApiParam(value = Documentation.PDP_HEADER_SIGNATURE, required = false) @HeaderParam(SunfishServices.HEADER_SIGNATURE) final String signature,
      @ApiParam(value = Documentation.HEADER_SERVICE, required = true) @HeaderParam(SunfishServices.HEADER_SERVICE) final String srv,
      @ApiParam(value = Documentation.HEADER_POLICY_TYPE, required = true) @HeaderParam(SunfishServices.HEADER_POLICY_TYPE) final String type,
      @ApiParam(value = Documentation.PDP_REQUEST, required = true) final Request request)
      throws PDPException {

    final PDPEngine engine = getEngine(request, srv, type);
    try {
      MissingAttributeCollector.startCollecting(request);
    } catch (final IOException e) {
      e.printStackTrace();
      throw new PDPException(e, Status.INTERNAL_SERVER_ERROR);
    }
    Response response;
    SunfishPIPRequest pipRequest = null;
    try {
      response = engine.decide(request);
      pipRequest = MissingAttributeCollector.build(request);
    } catch (final org.apache.openaz.xacml.api.pdp.PDPException | IOException e) {
      // remove request from collector
      e.printStackTrace();
      throw new PDPException(e, Status.INTERNAL_SERVER_ERROR);
    }
    if (pipRequest == null) {
      // no attributes required
      return response;
    }
    Request enhancedRequest = null;
    try {
      final Map<PIPAdapter, SunfishPIPRequest> pips = getMatchingPIPs(pipRequest);
      for (final PIPAdapter pip : pips.keySet()) {
        final SunfishPIPRequest minimisedPipRequest = pips.get(pip);
        minimisedPipRequest.setRequest(request);
        enhancedRequest = pip.request(minimisedPipRequest);
      }
    } catch (final ICSPException e) {
      e.printStackTrace();
      throw new PDPException(e, Status.INTERNAL_SERVER_ERROR);
    }

    if (enhancedRequest == null) {
      return response;
    }

    try {
      final Response decision = engine.decide(enhancedRequest);
      return decision;
    } catch (final org.apache.openaz.xacml.api.pdp.PDPException e) {
      e.printStackTrace();
      throw new PDPException(e, Status.INTERNAL_SERVER_ERROR);
    }

  }

  private Map<PIPAdapter, SunfishPIPRequest> getMatchingPIPs(final SunfishPIPRequest request) {
    log.debug("trying to find PIP matching request {}", request);
    final HashMap<PIPAdapter, SunfishPIPRequest> pips = new HashMap<>();
    // final PIPAdapter matchingPIP = PDPConfig.getMatchingPIP(request);
    // if (matchingPIP != null) {
    // log.debug("found single PIP providing all requested attributes: {}",
    // matchingPIP.getName());
    // pips.put(matchingPIP, request);
    // return pips;
    // }
    log.debug("Using multiple PIPs to gather attributes");
    final SortedSet<AttributeDesignator> missingAttrs = new TreeSet<>(new AttributeDesignatorComparator());
    missingAttrs.addAll(request.getAttributeDesignators());
    for (final PIPAdapter adapter : PDPConfig.getPIPs()) {
      final SortedSet<AttributeDesignator> matchingAttributes = adapter.getMatchingAttributes(request);
      if (!matchingAttributes.isEmpty()) {
        log.debug("PIP {} provides attributes {}", adapter.getName(), matchingAttributes);
        pips.put(adapter, new SunfishPIPRequest(request.getRequest(), matchingAttributes));
        request.getAttributeDesignators().removeAll(matchingAttributes);
      }
      log.debug("PIP {} does not provide the required attributes {}", adapter.getName(),
          request.getAttributeDesignators());
    }
    return pips;
  }

  private PDPEngine getEngine(final Request request, final String serviceID, final String policyType)
      throws PDPException {
    final Collection<PRPAdapter> adapters = PDPConfig.getPRPs();
    final PRPAdapter adapter = adapters.iterator().next();

    try {
      final PDPEngine engine = PDPFactory.loadEngine(adapter.collect(request, serviceID, policyType));
      return engine;
    } catch (IOException | FactoryException | ICSPException e) {
      e.printStackTrace();
      throw new PDPException(e, Status.INTERNAL_SERVER_ERROR);
    }

  }

}