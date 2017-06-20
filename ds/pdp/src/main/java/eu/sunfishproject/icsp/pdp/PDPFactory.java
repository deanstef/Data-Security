package eu.sunfishproject.icsp.pdp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.pdp.PDPEngine;
import org.apache.openaz.xacml.api.pdp.PDPEngineFactory;
import org.apache.openaz.xacml.pdp.OpenAZPDPEngineFactory;
import org.apache.openaz.xacml.pdp.policy.Policy;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyFinder;
import org.apache.openaz.xacml.pdp.policy.PolicyFinderFactory;
import org.apache.openaz.xacml.pdp.policy.PolicyIdReferenceBase;
import org.apache.openaz.xacml.pdp.policy.PolicySet;
import org.apache.openaz.xacml.pdp.policy.PolicySetChild;
import org.apache.openaz.xacml.pdp.std.StdPolicyFinder;
import org.apache.openaz.xacml.pdp.util.OpenAZPDPProperties;
import org.apache.openaz.xacml.std.StdStatusCode;
import org.apache.openaz.xacml.util.FactoryException;
import org.sunfish.icsp.common.exceptions.ICSPException;

import eu.sunfishproject.icsp.pdp.PolicyCache.CachedPolicy;
import eu.sunfishproject.icsp.pdp.exceptions.PDPException;
import eu.sunfishproject.icsp.pdp.pip.SunfishEvaluationContextFactory;

public class PDPFactory extends PolicyFinderFactory {
  private static final Logger     log        = LogManager.getLogger(PDPFactory.class);

  private static final String     KEY_POLICY = "policy";

  private static PDPEngineFactory factory    = new OpenAZPDPEngineFactory();

  public static PDPEngine loadEngine(final CachedPolicy policy) throws IOException, FactoryException {

    PolicyCache.cachePolicy(policy);
    log.debug("Configuring Properties");
    final Properties props = new Properties();
    props.put(OpenAZPDPProperties.PROP_POLICYFINDERFACTORY, PDPFactory.class.getCanonicalName());
    props.put(KEY_POLICY, policy.getPolicyID());
    props.put(OpenAZPDPProperties.PROP_EVALUATIONCONTEXTFACTORY,
        SunfishEvaluationContextFactory.class.getCanonicalName());
    fetchReferences(policy);
    log.debug("Creating Engine");

    return factory.newEngine(props);

  }

  private static void fetchReferences(final CachedPolicy policy) throws FactoryException {
    final PolicyDef def = policy.getPolicy();
    if (def instanceof Policy) {
      return;
    }

    final PolicySet set = (PolicySet) def;
    final Map<PolicySet, Set<PolicyIdReferenceBase<PolicyDef>>> referencedPolicies = new HashMap<>();
    try {
      collectReferences(set, referencedPolicies);
      if (referencedPolicies.isEmpty()) {
        return;
      }
      final Set<PolicySet> keySet = referencedPolicies.keySet();
      final LinkedBlockingQueue<PolicySet> toBeResolved = new LinkedBlockingQueue<>(keySet);

     while(!toBeResolved.isEmpty()){
       final PolicySet pol = toBeResolved.take();
        final Set<PolicyIdReferenceBase<PolicyDef>> refs = referencedPolicies.get(pol);
        final Set<PolicyIdReferenceBase<PolicyDef>> iter = new HashSet<>(refs);
        for (final PolicyIdReferenceBase<PolicyDef> ref : iter) {
          final PolicyDef resolvedReference = PolicyCache.get(ref);
          if (resolvedReference != null) {
            refs.remove(ref);
            pol.replaceReference(ref, resolvedReference);
            //add new TODOs at the end of the queue
            if (resolvedReference instanceof PolicySet) {
              final PolicySet setToResolve = (PolicySet)resolvedReference;
              toBeResolved.add(setToResolve);
              collectReferences(setToResolve, referencedPolicies);
            }
          }
        }
      }

    } catch (final PDPException | InterruptedException e) {
      throw new FactoryException(e);
    }
    // TODO fetch from prp
    try {
      final Set<PolicyIdReferenceBase<PolicyDef>> flattened = new HashSet<>();
      final Collection<Set<PolicyIdReferenceBase<PolicyDef>>> refs = referencedPolicies.values();
      for (final Set<PolicyIdReferenceBase<PolicyDef>> ref : refs) {
        flattened.addAll(ref);
      }
      final Collection<CachedPolicy> fetched = policy.getSource().get(flattened);
      for (final CachedPolicy pol : fetched) {
        PolicyCache.cacheReferencedItem(pol);
      }
      //try again from the top:
      //in case no new policies were introduced containing references this will terminate
      fetchReferences(policy);
    } catch (final ICSPException e) {
      log.warn("Could not fetch referenced policies, expect indeterminate decisions");
      return;
    }

  }

  private static void collectReferences(final PolicySet set,
      final Map<PolicySet, Set<PolicyIdReferenceBase<PolicyDef>>> referencedPolicies) throws PDPException {
    final Iterator<PolicySetChild> children = set.getChildren();
    if(children == null) {
      return;
    }
    while (children.hasNext()) {
      final PolicySetChild child = children.next();
      if (child instanceof Policy) {
        continue;
      } else if (child instanceof PolicySet) {
        collectReferences((PolicySet) child, referencedPolicies);
      } else {
        referencedPolicies.put(set, (Set<PolicyIdReferenceBase<PolicyDef>>) child);
      }
    }
   // throw new PDPException(Status.INTERNAL_SERVER_ERROR);

  }

  @Override
  public PolicyFinder getPolicyFinder() throws FactoryException {
    throw new FactoryException("this constructor must not be used!");
  }

  @Override
  public PolicyFinder getPolicyFinder(final Properties properties) throws FactoryException {

    final String identifier = properties.getProperty(KEY_POLICY);
    if (identifier == null) {
      throw new FactoryException("policy is null");
    }
    return new StdPolicyFinder(loadPolicyDef(identifier), new ArrayList<PolicyDef>());
  }

  protected PolicyDef loadPolicyDef(final String identifier) {
    PolicyDef policyDef = null;

    log.info("Loading policy from memory");
    policyDef = PolicyCache.getAndRemoveByID(identifier);

    if (policyDef == null) {
      log.error("Error loading policy. Policy is null");
      return new Policy(StdStatusCode.STATUS_CODE_SYNTAX_ERROR);
    }
    return policyDef;
  }

}
