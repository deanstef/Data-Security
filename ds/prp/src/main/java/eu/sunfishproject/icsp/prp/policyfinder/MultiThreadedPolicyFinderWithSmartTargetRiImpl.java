package eu.sunfishproject.icsp.prp.policyfinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBElement;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Attribute;
import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.IdReferenceMatch;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.RequestAttributes;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.api.pip.PIPEngine;
import org.apache.openaz.xacml.api.pip.PIPException;
import org.apache.openaz.xacml.api.pip.PIPFinder;
import org.apache.openaz.xacml.api.pip.PIPRequest;
import org.apache.openaz.xacml.api.pip.PIPResponse;
import org.apache.openaz.xacml.api.trace.TraceEvent;
import org.apache.openaz.xacml.pdp.eval.EvaluationContext;
import org.apache.openaz.xacml.pdp.eval.EvaluationException;
import org.apache.openaz.xacml.pdp.eval.MatchResult.MatchCode;
import org.apache.openaz.xacml.pdp.policy.Policy;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.PolicyFinderResult;
import org.apache.openaz.xacml.pdp.policy.PolicySet;
import org.apache.openaz.xacml.std.StdMutableAttribute;
import org.apache.openaz.xacml.std.pip.StdMutablePIPResponse;
import org.apache.openaz.xacml.std.pip.StdPIPResponse;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicyDefReader;
import org.sunfish.icsp.common.util.CommonUtil;

import eu.sunfishproject.icsp.prp.config.PRPConfig;
import eu.sunfishproject.icsp.prp.util.Utils;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;

public class MultiThreadedPolicyFinderWithSmartTargetRiImpl {

	Logger          log = LogManager.getLogger(MultiThreadedPolicyFinderWithSmartTargetRiImpl.class);
	private static final ObjectFactory OBJ_FACT = new ObjectFactory();
	private final ExecutorService srv ;
	final XACMLPolicyDefReader r = new XACMLPolicyDefReader();
	

	public MultiThreadedPolicyFinderWithSmartTargetRiImpl() throws IOException
	{
		srv = new ForkJoinPool(PRPConfig.getInstance().getMaxThreads());
	}

	public PolicySetType getPoliciesForDecisionRequest(final Request decisionRequest, List<PolicyType> rootPolicyMap) {

		final PolicySetType policySet = new PolicySetType();
		policySet.setDescription("Auto generated " + new Date());
		policySet.setTarget(new TargetType());
		policySet.setVersion("1.0");// TODO use hash of all concatenated ids and
		// versions??
		policySet.setPolicySetId(UUID.randomUUID().toString());
		policySet.setPolicyCombiningAlgId(XACML3.ID_POLICY_DENY_OVERRIDES.stringValue());
		final List<JAXBElement<?>> set = policySet.getPolicySetOrPolicyOrPolicySetIdReference();
		final BlockingQueue<JAXBElement<?>> policyQ= new LinkedBlockingQueue<>();
		//    final BlockingQueue<JAXBElement<?>> setQ= new LinkedBlockingQueue<>();
		final AtomicInteger matchingPolicies = new AtomicInteger(0);
		final AtomicInteger indeterminatePolicies = new AtomicInteger(0);

		for (final PolicyType policy : rootPolicyMap) {
			srv.execute(new PolicyMatcher(decisionRequest, policyQ,policy, matchingPolicies, indeterminatePolicies));
		}
		//    for (final TreeMap<String, PolicySetType> treeMap : rootPolicySetMap.values()) {
		//      srv.execute(new SetMatcher(decisionRequest, rootPolicyDefMap, setQ, treeMap, matchingPolicies, indeterminatePolicies));
		//    }
		srv.shutdown();
		try {
			srv.awaitTermination(1, TimeUnit.HOURS);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//      set.addAll(setQ);
		set.addAll(policyQ);

		log.info("Found " + matchingPolicies.get() + " matching and "+ indeterminatePolicies.get()+" indeterminate policies.");

		return policySet;
	}

	private class PolicyMatcher implements Runnable {
//		private final TreeMap<String, PolicyType> treeMap;
		//private final Map<String, PolicyDef>      rootPolicyDefMap;
		private final Request                     decisionRequest;
		private final Collection<JAXBElement<?>>        set;
		private final AtomicInteger matchingPolicies;
		private final AtomicInteger indeterminatePolicies;
		private final PolicyType policy;


		public PolicyMatcher(final Request decisionRequest, final Collection<JAXBElement<?>> set,
				final PolicyType policy, final AtomicInteger matchingPolicies, final AtomicInteger indeterminatePolicies) {
			this.decisionRequest = decisionRequest;
			//      this.rootPolicyDefMap = rootPolicyDefMap;
			//      this.treeMap = treeMap;
			this.policy = policy;
			this.set = set;
			this.matchingPolicies = matchingPolicies;
			this.indeterminatePolicies = indeterminatePolicies;
		}

		@Override
		public void run() {
			PolicyDef policyDef;
			//      final PolicyType policy = treeMap.firstEntry().getValue();

			policyDef = convertToPolicyDef(policy);
			try {
				final MatchCode res = match(policyDef, decisionRequest);
				if(res.compareTo(MatchCode.MATCH) == 0)
				{
					set.add(OBJ_FACT.createPolicy(policy));
					matchingPolicies.incrementAndGet();
				}
				else if(res.compareTo(MatchCode.INDETERMINATE) == 0)
				{
					set.add(OBJ_FACT.createPolicy(policy));
					indeterminatePolicies.incrementAndGet();
				}

			} catch (final EvaluationException e) {
				e.printStackTrace();
				set.add(OBJ_FACT.createPolicy(policy));

			}
		}

		private PolicyDef convertToPolicyDef(PolicyType policy) {
			try{
				String s = CommonUtil.policyTypeToString(policy);
		    final PolicyDef pol = r.readFrom(PolicyDef.class, null, null,
		        ExtendedMediaType.APPLICATION_XML_XACML_TYPE, null, IOUtils.toInputStream(s, "UTF-8"));
			return pol;
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		    return null;
		}
	}

	public MatchCode match(final PolicyDef policy, final Request request) throws EvaluationException {
		// log.trace("Matching policy {} against {}",policy, request);
		return policy.getTarget().match(new MatchingContest(request)).getMatchCode();
	}

	private class MatchingContest implements EvaluationContext {
		private final Request request;

		private MatchingContest(final Request req) {
			this.request = req;
		}

		@Override
		public void trace(final TraceEvent<?> traceEvent) {
		}

		@Override
		public boolean isTracing() {
			return false;
		}

		@Override
		public Collection<PIPEngine> getPIPEngines() {
			throw new RuntimeException();
			// return null;
		}

		@Override
		public PIPResponse getMatchingAttributes(final PIPRequest pipRequest, final PIPEngine exclude,
				final PIPFinder pipFinderParent) throws PIPException {
			throw new RuntimeException();
		}

		@Override
		public PIPResponse getMatchingAttributes(final PIPRequest pipRequest, final PIPEngine exclude)
				throws PIPException {
			throw new RuntimeException();
		}

		@Override
		public PIPResponse getAttributes(final PIPRequest pipRequest, final PIPEngine exclude,
				final PIPFinder pipFinderParent) throws PIPException {
			throw new RuntimeException();
		}

		@Override
		public PIPResponse getAttributes(final PIPRequest pipRequest, final PIPEngine exclude)
				throws PIPException {
			throw new RuntimeException();
		}

		@Override
		public PolicyFinderResult<PolicyDef> getRootPolicyDef() {
			throw new RuntimeException();
		}

		@Override
		public Request getRequest() {
			return request;
		}

		@Override
		public PolicyFinderResult<PolicySet> getPolicySet(final IdReferenceMatch idReferenceMatch) {
			throw new RuntimeException();
		}

		@Override
		public PolicyFinderResult<Policy> getPolicy(final IdReferenceMatch idReferenceMatch) {
			throw new RuntimeException();
		}

		@Override
		public PIPResponse getAttributes(final PIPRequest pipRequest) throws PIPException {
			final Request thisRequest = this.getRequest();
			if (thisRequest == null) {
				return StdPIPResponse.PIP_RESPONSE_EMPTY;
			}

			final Iterator<RequestAttributes> iterRequestAttributes = thisRequest
					.getRequestAttributes(pipRequest.getCategory());
			if (iterRequestAttributes == null || !iterRequestAttributes.hasNext()) {
				return StdPIPResponse.PIP_RESPONSE_EMPTY;
			}

			StdMutablePIPResponse pipResponse = null;

			while (iterRequestAttributes.hasNext()) {
				final RequestAttributes requestAttributes = iterRequestAttributes.next();
				final Iterator<Attribute> iterAttributes = requestAttributes
						.getAttributes(pipRequest.getAttributeId());
				while (iterAttributes.hasNext()) {
					final Attribute attribute = iterAttributes.next();
					if (attribute.getValues().size() > 0
							&& (pipRequest.getIssuer() == null || pipRequest.getIssuer().equals(attribute.getIssuer()))) {
						/*
						 * If all of the attribute values in the given Attribute match the
						 * requested data type, we
						 * can just return the whole Attribute as part of the response.
						 */
						boolean bAllMatch = true;
						for (final AttributeValue<?> attributeValue : attribute.getValues()) {
							if (!pipRequest.getDataTypeId().equals(attributeValue.getDataTypeId())) {
								bAllMatch = false;
								break;
							}
						}
						if (bAllMatch) {
							if (pipResponse == null) {
								pipResponse = new StdMutablePIPResponse(attribute);
							} else {
								pipResponse.addAttribute(attribute);
							}
						} else {
							/*
							 * Only a subset of the values match, so we have to construct a
							 * new Attribute
							 * containing only the matching values.
							 */
							List<AttributeValue<?>> listAttributeValues = null;
							for (final AttributeValue<?> attributeValue : attribute.getValues()) {
								if (pipRequest.getDataTypeId().equals(attributeValue.getDataTypeId())) {
									if (listAttributeValues == null) {
										listAttributeValues = new ArrayList<>();
									}
									listAttributeValues.add(attributeValue);
								}
							}
							if (listAttributeValues != null) {
								if (pipResponse == null) {
									pipResponse = new StdMutablePIPResponse();
								}
								pipResponse
								.addAttribute(new StdMutableAttribute(attribute.getCategory(), attribute.getAttributeId(),
										listAttributeValues, attribute.getIssuer(), attribute.getIncludeInResults()));
							}
						}
					}
				}
			}

			if (pipResponse == null) {
				return StdPIPResponse.PIP_RESPONSE_EMPTY;
			} else {
				return pipResponse;
			}
		}
	};
}
