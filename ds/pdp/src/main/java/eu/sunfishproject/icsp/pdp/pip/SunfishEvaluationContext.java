package eu.sunfishproject.icsp.pdp.pip;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openaz.xacml.api.IdReferenceMatch;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.pip.PIPEngine;
import org.apache.openaz.xacml.api.pip.PIPException;
import org.apache.openaz.xacml.api.pip.PIPFinder;
import org.apache.openaz.xacml.api.pip.PIPRequest;
import org.apache.openaz.xacml.api.pip.PIPResponse;
import org.apache.openaz.xacml.api.trace.TraceEngine;
import org.apache.openaz.xacml.api.trace.TraceEngineFactory;
import org.apache.openaz.xacml.api.trace.TraceEvent;
import org.apache.openaz.xacml.pdp.eval.EvaluationContext;
import org.apache.openaz.xacml.pdp.policy.Policy;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyFinder;
import org.apache.openaz.xacml.pdp.policy.PolicyFinderResult;
import org.apache.openaz.xacml.pdp.policy.PolicySet;
import org.apache.openaz.xacml.std.pip.engines.RequestEngine;
import org.apache.openaz.xacml.util.FactoryException;

public class SunfishEvaluationContext implements EvaluationContext {
  private Log          logger = LogFactory.getLog(this.getClass());
  private Request      request;
  private PIPFinder    requestFinder;
  private PolicyFinder policyFinder;
  private TraceEngine  traceEngine;

  /**
   * Creates a new <code>StdEvaluationContext</code> with the given
   * {@link org.apache.openaz.xacml.api.Request} and
   * {@link org.apache.openaz.xacml.pdp.policy.PolicyDef}.
   *
   * @param requestIn
   *          the <code>Request</code>
   * @param policyDef
   *          the <code>PolicyDef</code>
   */
  public SunfishEvaluationContext(Request requestIn, PolicyFinder policyFinderIn, PIPFinder pipFinder,
      TraceEngine traceEngineIn) {
    this.request = requestIn;
    this.policyFinder = policyFinderIn;
    if (traceEngineIn != null) {
      this.traceEngine = traceEngineIn;
    } else {
      try {
        this.traceEngine = TraceEngineFactory.newInstance().getTraceEngine();
      } catch (FactoryException ex) {
        this.logger.error("FactoryException creating TraceEngine: " + ex.toString(), ex);
      }
    }
    if (pipFinder == null) {
      this.requestFinder = new SunfishPIPFinder(null, new RequestEngine(requestIn), requestIn);
    } else {
      if (pipFinder instanceof SunfishPIPFinder) {
        this.requestFinder = (SunfishPIPFinder) pipFinder;
      } else {
        this.requestFinder = new SunfishPIPFinder(pipFinder, new RequestEngine(requestIn), requestIn);
      }
    }
  }

  public SunfishEvaluationContext(Request requestIn, PolicyFinder policyFinderIn, PIPFinder pipFinder) {
    this(requestIn, policyFinderIn, pipFinder, null);
  }

  @Override
  public Request getRequest() {
    return this.request;
  }

  @Override
  public PIPResponse getAttributes(PIPRequest pipRequest) throws PIPException {
    return this.requestFinder.getAttributes(pipRequest, null);
  }

  @Override
  public PIPResponse getAttributes(PIPRequest pipRequest, PIPEngine exclude) throws PIPException {
    return this.requestFinder.getAttributes(pipRequest, exclude);
  }

  @Override
  public PIPResponse getAttributes(PIPRequest pipRequest, PIPEngine exclude, PIPFinder pipFinderRoot)
      throws PIPException {
    return this.requestFinder.getAttributes(pipRequest, exclude, pipFinderRoot);
  }

  @Override
  public PolicyFinderResult<PolicyDef> getRootPolicyDef() {
    return this.policyFinder.getRootPolicyDef(this);
  }

  @Override
  public PolicyFinderResult<Policy> getPolicy(IdReferenceMatch idReferenceMatch) {
    return this.policyFinder.getPolicy(idReferenceMatch);
  }

  @Override
  public PolicyFinderResult<PolicySet> getPolicySet(IdReferenceMatch idReferenceMatch) {
    return this.policyFinder.getPolicySet(idReferenceMatch);
  }

  @Override
  public void trace(TraceEvent<?> traceEvent) {
    if (this.traceEngine != null) {
      this.traceEngine.trace(traceEvent);
    }
  }

  @Override
  public boolean isTracing() {
    return (this.traceEngine == null ? false : this.traceEngine.isTracing());
  }

  @Override
  public PIPResponse getMatchingAttributes(PIPRequest pipRequest, PIPEngine exclude) throws PIPException {
    return this.requestFinder.getMatchingAttributes(pipRequest, exclude);
  }

  @Override
  public PIPResponse getMatchingAttributes(PIPRequest pipRequest, PIPEngine exclude,
      PIPFinder pipFinderParent) throws PIPException {
    return this.requestFinder.getMatchingAttributes(pipRequest, exclude, pipFinderParent);
  }

  @Override
  public Collection<PIPEngine> getPIPEngines() {
    return this.requestFinder.getPIPEngines();
  }
}
