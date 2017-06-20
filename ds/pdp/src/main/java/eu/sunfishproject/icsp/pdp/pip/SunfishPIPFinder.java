package eu.sunfishproject.icsp.pdp.pip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.Status;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.api.pip.PIPEngine;
import org.apache.openaz.xacml.api.pip.PIPException;
import org.apache.openaz.xacml.api.pip.PIPFinder;
import org.apache.openaz.xacml.api.pip.PIPRequest;
import org.apache.openaz.xacml.api.pip.PIPResponse;
import org.apache.openaz.xacml.std.pip.StdPIPResponse;
import org.apache.openaz.xacml.std.pip.engines.EnvironmentEngine;
import org.apache.openaz.xacml.std.pip.engines.RequestEngine;
import org.apache.openaz.xacml.std.pip.finders.WrappingFinder;

public class SunfishPIPFinder extends WrappingFinder {
  private final RequestEngine                requestEngine;
  private final Request                      request;
  private final EnvironmentEngine            environmentEngine;
  private final Map<PIPRequest, PIPResponse> mapCache = new HashMap<PIPRequest, PIPResponse>();

  protected RequestEngine getRequestEngine() {
    return requestEngine;
  }

  protected EnvironmentEngine getEnvironmentEngine() {
    return this.environmentEngine;
  }

  public SunfishPIPFinder(PIPFinder pipFinder, RequestEngine requestEngine, Request request) {
    super(pipFinder);
    this.request = request;
    this.requestEngine = requestEngine;
    this.environmentEngine = new EnvironmentEngine(new Date());
  }

  @Override
  protected PIPResponse getAttributesInternal(PIPRequest pipRequest, PIPEngine exclude,
      PIPFinder pipFinderRoot) throws PIPException {

    /*
     * First try the RequestEngine
     */
    PIPResponse pipResponse = null;
    RequestEngine thisRequestEngine = this.getRequestEngine();
    Status status = null;
    if (thisRequestEngine != null && thisRequestEngine != exclude) {
      // tStart = System.nanoTime();
      pipResponse = thisRequestEngine.getAttributes(pipRequest,
          (pipFinderRoot == null ? this : pipFinderRoot));
      // tEnd = System.nanoTime();
      if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
        /*
         * We know how the RequestEngine works. It does not return multiple
         * results and all of the
         * results should match the request.
         */
        if (pipResponse.getAttributes().size() > 0) {
          return pipResponse;
        }
      } else {
        status = pipResponse.getStatus();
      }
    }

    /*
     * Next try the EnvironmentEngine if no issuer has been specified
     */
    if (XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT.equals(pipRequest.getCategory())
        && (pipRequest.getIssuer() == null || pipRequest.getIssuer().length() == 0)) {
      EnvironmentEngine thisEnvironmentEngine = this.getEnvironmentEngine();
      pipResponse = thisEnvironmentEngine.getAttributes(pipRequest, this);
      if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
        /*
         * We know how the EnvironmentEngine works. It does not return multiple
         * results and all of
         * the results should match the request.
         */
        if (pipResponse.getAttributes().size() > 0) {
          return pipResponse;
        }
      } else {
        if (status == null) {
          status = pipResponse.getStatus();
        }
      }
    }

    /*
     * Try the cache
     */
    if (this.mapCache.containsKey(pipRequest)) {
      return this.mapCache.get(pipRequest);
    }

    /*
     * Delegate to the wrapped Finder
     */
    PIPFinder thisWrappedFinder = this.getWrappedFinder();
    if (thisWrappedFinder != null) {
      pipResponse = thisWrappedFinder.getAttributes(pipRequest, exclude,
          (pipFinderRoot == null ? this : pipFinderRoot));
      if (pipResponse != null) {
        if (pipResponse.getStatus() == null || pipResponse.getStatus().isOk()) {
          if (pipResponse.getAttributes().size() > 0) {
            /*
             * Cache all of the returned attributes
             */
            Map<PIPRequest, PIPResponse> mapResponses = StdPIPResponse.splitResponse(pipResponse);
            if (mapResponses != null && mapResponses.size() > 0) {
              for (PIPRequest pipRequestSplit : mapResponses.keySet()) {
                this.mapCache.put(pipRequestSplit, mapResponses.get(pipRequestSplit));
              }
            }
            return pipResponse;
          }
        } else if (status == null || status.isOk()) {
          status = pipResponse.getStatus();
        }
      }
    }

    /*
     * TODO COLLECT MISSING ATTRS HERE
     */
    try {
      MissingAttributeCollector.collect(request, pipRequest);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    /*
     * We did not get a valid, non-empty response back from either the Request
     * or the wrapped
     * PIPFinder. If there was an error using the RequestEngine, use that as the
     * status of the
     * response, otherwise return an empty response.
     */
    if (status != null && !status.isOk()) {
      return new StdPIPResponse(status);
    } else {
      return StdPIPResponse.PIP_RESPONSE_EMPTY;
    }
    // System.out.println("RequestFinder.getAttributesInternal() = " + (tEnd -
    // tStart));
  }

  @Override
  public Collection<PIPEngine> getPIPEngines() {
    List<PIPEngine> engines = new ArrayList<PIPEngine>();

    if (this.requestEngine != null) {
      engines.add(this.requestEngine);
    }
    if (this.environmentEngine != null) {
      engines.add(this.environmentEngine);
    }
    PIPFinder wrappedFinder = this.getWrappedFinder();
    if (wrappedFinder != null) {
      engines.addAll(wrappedFinder.getPIPEngines());
    }
    return Collections.unmodifiableList(engines);
  }
}
