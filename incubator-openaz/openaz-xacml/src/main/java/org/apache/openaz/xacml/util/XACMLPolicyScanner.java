/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

/*
 *                        AT&T - PROPRIETARY
 *          THIS FILE CONTAINS PROPRIETARY INFORMATION OF
 *        AT&T AND IS NOT TO BE DISCLOSED OR USED EXCEPT IN
 *             ACCORDANCE WITH APPLICABLE AGREEMENTS.
 *
 *          Copyright (c) 2014 AT&T Knowledge Ventures
 *              Unpublished and Not for Publication
 *                     All Rights Reserved
 */
package org.apache.openaz.xacml.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openaz.xacml.api.Advice;
import org.apache.openaz.xacml.api.Attribute;
import org.apache.openaz.xacml.api.AttributeAssignment;
import org.apache.openaz.xacml.api.Obligation;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.StdAttribute;
import org.apache.openaz.xacml.std.StdAttributeAssignment;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.StdMutableAdvice;
import org.apache.openaz.xacml.std.StdMutableObligation;
import org.apache.openaz.xacml.std.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AllOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AnyOfType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeAssignmentExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeDesignatorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeSelectorType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ConditionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.MatchType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressionsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RuleType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.TargetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.VariableDefinitionType;

/**
 * class XACMLPolicyScanner This class traverses the hierarchy of a XACML 3.0
 * policy. You can optionally pass
 * a Callback class and override any desired methods to retrieve information
 * from a policy.
 */
public class XACMLPolicyScanner {

  /**
   * Very simple enumeration used in the callback class. Return CONTINUE to
   * instruct the XACMLPolicyScanner
   * to continue scanning the policy. Otherwise, call STOP to terminate scanning
   * the policy.
   */
  public enum CallbackResult {
      //
      // Continue scanning the policy
      //
      CONTINUE,
      //
      // Terminate scanning
      //
      STOP;
  }

  /**
   * This is a simple callback interface that can be implemented and passed to
   * the XACMLPolicyScanner. When
   * the XACMLPolicyScanner encounters a relevant element in the policy, it
   * calls the appropriate method.
   */
  public interface Callback {
    /**
     * Called when the scanning begins with the root element.
     *
     * @param root
     *          - The root PolicySet/Policy object.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onBeginScan(Object root);

    /**
     * Called when the scanning finishes with the root element.
     *
     * @param root
     *          - The root PolicySet/Policy object.
     */
    void onFinishScan(Object root);

    /**
     * Called when the scanning of the policy first encounters a PolicySet
     *
     * @param parent
     *          - The parent PolicySet of the policySet. Can be null if this is
     *          the root PolicySet.
     * @param policySet
     *          - The PolicySet object.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPreVisitPolicySet(PolicySetType parent, PolicySetType policySet);

    /**
     * Called when the scanning of the PolicySet has finished.
     *
     * @param parent
     *          - The parent PolicySet of the policySet. Can be null if this is
     *          the root PolicySet.
     * @param policySet
     *          - The PolicySet object.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPostVisitPolicySet(PolicySetType parent, PolicySetType policySet);

    /**
     * Called when the scanning of the policy first encounters a Policy
     *
     * @param parent
     *          - The parent PolicySet of the policy. This can be null if this
     *          policy is the root.
     * @param policy
     *          - The policy.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPreVisitPolicy(PolicySetType parent, PolicyType policy);

    /**
     * Called when the scanning of the Policy has finished.
     *
     * @param parent
     *          - The parent PolicySet of the policy. This can be null if this
     *          policy is the root.
     * @param policy
     *          - The policy.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPostVisitPolicy(PolicySetType parent, PolicyType policy);

    /**
     * Called when the scanning of the policy first encounters a Rule
     *
     * @param parent
     *          - The parent policy of the rule
     * @param rule
     *          - The rule
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPreVisitRule(PolicyType parent, RuleType rule);

    /**
     * Called when the scanning of the Rule has finished.
     *
     * @param parent
     *          - The parent policy of the rule
     * @param rule
     *          - The rule
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPostVisitRule(PolicyType parent, RuleType rule);

    /**
     * When an attribute has been encountered.
     *
     * @param parent
     *          - The parent PolicySet/Policy/Rule for this attribute.
     * @param container
     *          - What part of the PolicySet/Policy/Rule this attribute is
     *          located. eg. Target,
     *          Condition
     * @param attribute
     *          - The attribute
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onAttribute(Object parent, Object container, Attribute attribute);

    /**
     * When an obligation has been encountered.
     *
     * @param parent
     *          - The parent PolicySet/Policy/Rule for the obligation.
     * @param obligation
     *          - The obligation.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onObligation(Object parent, ObligationExpressionType expression, Obligation obligation);

    /**
     * When an advice has been encountered.
     *
     * @param parent
     *          - The parent PolicySet/Policy/Rule for the obligation.
     * @param advice
     *          - The advice.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onAdvice(Object parent, AdviceExpressionType expression, Advice advice);

    /**
     * When a variable definition has been encountered.
     *
     * @param policy
     *          - The Policy the variable is located in.
     * @param variable
     *          - The variable.
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onVariable(PolicyType policy, VariableDefinitionType variable);

    /**
     * When a condition has been encountered.
     *
     * @param rule
     *          - The Rule containing the condition.
     * @param condition
     *          - The condition
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onCondition(RuleType rule, ConditionType condition);

    /**
     * When a reference to another PolicySet is encountered.
     *
     * @param reference
     *          - The Policy Set Reference ID
     * @param parent
     *          - The parent PolicySet that holds the reference
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPolicySetIdReference(IdReferenceType reference, PolicySetType parent);

    /**
     * When a reference to another PolicySet is encountered.
     *
     * @param reference
     *          - The Policy Set Reference ID
     * @param parent
     *          - The parent PolicySet that holds the reference
     * @return CallbackResult - CONTINUE or STOP scanning the policy.
     */
    CallbackResult onPolicyIdReference(IdReferenceType reference, PolicySetType parent);
  }

  /**
   * This is a simple implementation of the Callback. Extend this if you don't
   * wish to implement all the
   * callback functions available. Each method simply returns
   * CallbackResult.CONTINUE.
   */
  public static class SimpleCallback implements Callback {

    @Override
    public CallbackResult onBeginScan(final Object root) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public void onFinishScan(final Object root) {
    }

    @Override
    public CallbackResult onPreVisitPolicySet(final PolicySetType parent, final PolicySetType policySet) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPostVisitPolicySet(final PolicySetType parent, final PolicySetType policySet) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPreVisitPolicy(final PolicySetType parent, final PolicyType policy) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPostVisitPolicy(final PolicySetType parent, final PolicyType policy) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPreVisitRule(final PolicyType parent, final RuleType rule) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPostVisitRule(final PolicyType parent, final RuleType rule) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onAttribute(final Object parent, final Object container,
        final Attribute attribute) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onObligation(final Object parent, final ObligationExpressionType expression,
        final Obligation obligation) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onAdvice(final Object parent, final AdviceExpressionType expression,
        final Advice advice) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onVariable(final PolicyType policy, final VariableDefinitionType o) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onCondition(final RuleType rule, final ConditionType condition) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPolicySetIdReference(final IdReferenceType reference,
        final PolicySetType parent) {
      return CallbackResult.CONTINUE;
    }

    @Override
    public CallbackResult onPolicyIdReference(final IdReferenceType reference, final PolicySetType parent) {
      return CallbackResult.CONTINUE;
    }

  }

  private static final Log    logger       = LogFactory.getLog(XACMLPolicyScanner.class);
  private Object              policyObject = null;
  private Callback            callback     = null;

  private static JAXBContext  policyContext;
  private static Unmarshaller policyUnmarshaller;
  private static JAXBContext  setContext;
  private static Unmarshaller setUnmarshaller;
  private static JAXBException initException =null;

  static {
    try {
      policyContext = JAXBContext.newInstance(PolicyType.class);
      policyUnmarshaller = policyContext.createUnmarshaller();
      setContext = JAXBContext.newInstance(PolicySetType.class);
      setUnmarshaller = setContext.createUnmarshaller();
    } catch (final JAXBException e) {
     initException=e;
     logger.error("Unmarshallers could not be instantiated! Policies and PolicySets will not be loadable", e);
    }

  }

  public XACMLPolicyScanner(final Path filename, final Callback callback) {
    try (InputStream is = Files.newInputStream(filename)) {
      this.policyObject = XACMLPolicyScanner.readPolicy(is);
    } catch (final IOException e) {
      logger.error("Failed to read policy", e);
    }
    this.callback = callback;
  }

  public XACMLPolicyScanner(final PolicySetType policySet, final Callback callback) {
    this.policyObject = policySet;
    this.callback = callback;
  }

  public XACMLPolicyScanner(final PolicySetType policySet) {
    this(policySet, null);
  }

  public XACMLPolicyScanner(final PolicyType policy, final Callback callback) {
    this.policyObject = policy;
    this.callback = callback;
  }

  public XACMLPolicyScanner(final PolicyType policy) {
    this(policy, null);
  }

  /**
   * Sets the callback interface to be used.
   *
   * @param cb
   */
  public void setCallback(final Callback cb) {
    this.callback = cb;
  }

  /**
   * Saves the given callback object then calls the scan() method.
   *
   * @param cb
   * @return
   */
  public Object scan(final Callback cb) {
    this.callback = cb;
    return this.scan();
  }

  /**
   * This begins the scanning of the contained object.
   *
   * @return - The PolicySet/Policy that was scanned.
   */
  public Object scan() {
    if (this.policyObject == null) {
      return null;
    }
    if (this.callback != null && this.callback.onBeginScan(this.policyObject) == CallbackResult.STOP) {
      return this.policyObject;
    }
    if (this.policyObject instanceof PolicyType) {
      this.scanPolicy(null, (PolicyType) this.policyObject);
    } else if (this.policyObject instanceof PolicySetType) {
      this.scanPolicySet(null, (PolicySetType) this.policyObject);
    } else {
      logger.error("Unknown class type: " + this.policyObject.getClass().getCanonicalName());
    }
    if (this.callback != null) {
      this.callback.onFinishScan(this.policyObject);
    }
    return this.policyObject;
  }

  /**
   * This performs the scan of a PolicySet
   *
   * @param parent
   *          - Its parent PolicySet. Can be null if this is the root.
   * @param policySet
   *          - The PolicySet object.
   * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
   */
  /**
   * @param parent
   * @param policySet
   * @return
   */
  protected CallbackResult scanPolicySet(final PolicySetType parent, final PolicySetType policySet) {
    if (logger.isTraceEnabled()) {
      logger.trace("scanning policy set: " + policySet.getPolicySetId() + " " + policySet.getDescription());
    }
    if (this.callback != null
        && this.callback.onPreVisitPolicySet(parent, policySet) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    //
    // Scan its info
    //
    if (this.scanTarget(policySet, policySet.getTarget()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    if (this.scanObligations(policySet, policySet.getObligationExpressions()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    if (this.scanAdvice(policySet, policySet.getAdviceExpressions()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    //
    // Iterate the policy sets and/or policies
    //
    final List<JAXBElement<?>> list = policySet.getPolicySetOrPolicyOrPolicySetIdReference();
    for (final JAXBElement<?> element : list) {
      if (element.getName().getLocalPart().equals("PolicySet")) {
        if (this.scanPolicySet(policySet, (PolicySetType) element.getValue()) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
      } else if (element.getName().getLocalPart().equals("Policy")) {
        if (this.scanPolicy(policySet, (PolicyType) element.getValue()) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
      } else if (element.getValue() instanceof IdReferenceType) { // NOPMD
        /*
         * TODO if
         * (element.getName().getLocalPart().equals("PolicySetIdReference")) {
         *
         * } else if
         * (element.getName().getLocalPart().equals("PolicyIdReference")) {
         *
         * }
         */
      } else {
        logger
            .warn("generating policy sets found unsupported element: " + element.getName().getNamespaceURI());
      }
    }
    if (this.callback != null
        && this.callback.onPostVisitPolicySet(parent, policySet) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    return CallbackResult.CONTINUE;
  }

  /**
   * This performs scanning of the Policy object.
   *
   * @param parent
   *          - The parent PolicySet of the policy. This can be null if this is
   *          a root Policy.
   * @param policy
   *          - The policy being scanned.
   * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
   */
  protected CallbackResult scanPolicy(final PolicySetType parent, final PolicyType policy) {
    if (logger.isTraceEnabled()) {
      logger.trace("scanning policy: " + policy.getPolicyId() + " " + policy.getDescription());
    }
    if (this.callback != null && this.callback.onPreVisitPolicy(parent, policy) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    //
    // Scan its info
    //
    if (this.scanTarget(policy, policy.getTarget()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    if (this.scanVariables(policy,
        policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    if (this.scanObligations(policy, policy.getObligationExpressions()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    if (this.scanAdvice(policy, policy.getAdviceExpressions()) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    //
    // Iterate the rules
    //
    final List<Object> list = policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();
    for (final Object o : list) {
      if (o instanceof RuleType) {
        final RuleType rule = (RuleType) o;
        if (logger.isTraceEnabled()) {
          logger.trace("scanning rule: " + rule.getRuleId() + " " + rule.getDescription());
        }
        if (this.callback != null && this.callback.onPreVisitRule(policy, rule) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
        if (this.scanTarget(rule, rule.getTarget()) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
        if (this.scanConditions(rule, rule.getCondition()) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
        if (this.scanObligations(rule, rule.getObligationExpressions()) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
        if (this.scanAdvice(rule, rule.getAdviceExpressions()) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
        if (this.callback != null && this.callback.onPostVisitRule(policy, rule) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
      } else if (o instanceof VariableDefinitionType) {
        if (this.callback != null
            && this.callback.onVariable(policy, (VariableDefinitionType) o) == CallbackResult.STOP) {
          return CallbackResult.STOP;
        }
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("scanning policy rules found unsupported object:" + o.toString());
        }
      }
    }
    if (this.callback != null && this.callback.onPostVisitPolicy(parent, policy) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    return CallbackResult.CONTINUE;
  }

  /**
   * Scans the given target for attributes. Its sole purpose is to return
   * attributes found.
   *
   * @param parent
   *          - The parent PolicySet/Policy/Rule for the target.
   * @param target
   *          - The target.
   * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
   */
  protected CallbackResult scanTarget(final Object parent, final TargetType target) {
    if (target == null) {
      return CallbackResult.CONTINUE;
    }
    final List<AnyOfType> anyOfList = target.getAnyOf();
    if (anyOfList != null) {
      final Iterator<AnyOfType> iterAnyOf = anyOfList.iterator();
      while (iterAnyOf.hasNext()) {
        final AnyOfType anyOf = iterAnyOf.next();
        final List<AllOfType> allOfList = anyOf.getAllOf();
        if (allOfList != null) {
          final Iterator<AllOfType> iterAllOf = allOfList.iterator();
          while (iterAllOf.hasNext()) {
            final AllOfType allOf = iterAllOf.next();
            final List<MatchType> matchList = allOf.getMatch();
            if (matchList != null) {
              final Iterator<MatchType> iterMatch = matchList.iterator();
              while (iterMatch.hasNext()) {
                final MatchType match = iterMatch.next();
                //
                // Finally down to the actual attribute
                //
                StdAttribute attribute = null;
                final AttributeValueType value = match.getAttributeValue();
                if (match.getAttributeDesignator() != null && value != null) {
                  final AttributeDesignatorType designator = match.getAttributeDesignator();
                  //
                  // The content may be tricky
                  //
                  attribute = new StdAttribute(new IdentifierImpl(designator.getCategory()),
                      new IdentifierImpl(designator.getAttributeId()),
                      new StdAttributeValue<List<?>>(new IdentifierImpl(value.getDataType()),
                          value.getContent()),
                      designator.getIssuer(), false);
                } else if (match.getAttributeSelector() != null && value != null) {
                  final AttributeSelectorType selector = match.getAttributeSelector();
                  attribute = new StdAttribute(new IdentifierImpl(selector.getCategory()),
                      new IdentifierImpl(selector.getContextSelectorId()), new StdAttributeValue<List<?>>(
                          new IdentifierImpl(value.getDataType()), value.getContent()),
                      null, false);
                } else {
                  logger.warn("NULL designator/selector or value for match.");
                }
                if (attribute != null && this.callback != null
                    && this.callback.onAttribute(parent, target, attribute) == CallbackResult.STOP) {
                  return CallbackResult.STOP;
                }
              }
            }
          }
        }
      }
    }
    return CallbackResult.CONTINUE;
  }

  /**
   * Scan the list of obligations.
   *
   * @param parent
   *          - The parent PolicySet/Policy/Rule for the obligation.
   * @param obligationExpressionsType
   *          - All the obligation expressions.
   * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
   */
  protected CallbackResult scanObligations(final Object parent,
      final ObligationExpressionsType obligationExpressionsType) {
    if (obligationExpressionsType == null) {
      return CallbackResult.CONTINUE;
    }
    final List<ObligationExpressionType> expressions = obligationExpressionsType.getObligationExpression();
    if (expressions == null || expressions.size() == 0) {
      return CallbackResult.CONTINUE;
    }
    for (final ObligationExpressionType expression : expressions) {
      final StdMutableObligation ob = new StdMutableObligation(
          new IdentifierImpl(expression.getObligationId()));
      final List<AttributeAssignmentExpressionType> assignments = expression
          .getAttributeAssignmentExpression();
      if (assignments != null) {
        for (final AttributeAssignmentExpressionType assignment : assignments) {
          // category is optional and may be null
          IdentifierImpl categoryId = null;
          if (assignment.getCategory() != null) {
            categoryId = new IdentifierImpl(assignment.getCategory());
          }
          final AttributeAssignment attribute = new StdAttributeAssignment(categoryId,
              new IdentifierImpl(assignment.getAttributeId()), assignment.getIssuer(),
              new StdAttributeValue<>(null, null));
          ob.addAttributeAssignment(attribute);
        }
      }
      if (this.callback != null
          && this.callback.onObligation(parent, expression, ob) == CallbackResult.STOP) {
        return CallbackResult.STOP;
      }
    }
    return CallbackResult.CONTINUE;
  }

  /**
   * Scans the list of advice expressions returning each individually.
   *
   * @param parent
   *          - The parent PolicySet/Policy/Rule for the advice.
   * @param adviceExpressionstype
   *          - The list of advice expressions.
   * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
   */
  protected CallbackResult scanAdvice(final Object parent,
      final AdviceExpressionsType adviceExpressionstype) {
    if (adviceExpressionstype == null) {
      return CallbackResult.CONTINUE;
    }
    final List<AdviceExpressionType> expressions = adviceExpressionstype.getAdviceExpression();
    if (expressions == null || expressions.size() == 0) {
      return CallbackResult.CONTINUE;
    }
    for (final AdviceExpressionType expression : expressions) {
      final StdMutableAdvice ob = new StdMutableAdvice(new IdentifierImpl(expression.getAdviceId()));
      final List<AttributeAssignmentExpressionType> assignments = expression
          .getAttributeAssignmentExpression();
      if (assignments != null) {
        for (final AttributeAssignmentExpressionType assignment : assignments) {
          IdentifierImpl categoryId = null;
          if (assignment.getCategory() != null) {
            categoryId = new IdentifierImpl(assignment.getCategory());
          }
          final AttributeAssignment attribute = new StdAttributeAssignment(categoryId,
              new IdentifierImpl(assignment.getAttributeId()), assignment.getIssuer(),
              new StdAttributeValue<>(null, null));
          ob.addAttributeAssignment(attribute);
        }
      }
      if (this.callback != null && this.callback.onAdvice(parent, expression, ob) == CallbackResult.STOP) {
        return CallbackResult.STOP;
      }
    }
    return CallbackResult.CONTINUE;
  }

  /**
   * Scans the list of variable definitions.
   *
   * @param policy
   *          - Policy object containing the variable definition.
   * @param list
   *          - List of variable definitions.
   * @return CallbackResult - CONTINUE to continue, STOP to terminate scanning.
   */
  protected CallbackResult scanVariables(final PolicyType policy, final List<Object> list) {
    if (list == null) {
      return CallbackResult.CONTINUE;
    }
    for (final Object o : list) {
      if (o instanceof VariableDefinitionType && this.callback != null
          && this.callback.onVariable(policy, (VariableDefinitionType) o) == CallbackResult.STOP) {
        return CallbackResult.STOP;
      }
    }

    return CallbackResult.CONTINUE;
  }

  /**
   * Scans the list of conditions.
   *
   * @param rule
   * @param condition
   * @return
   */
  protected CallbackResult scanConditions(final RuleType rule, final ConditionType condition) {
    if (condition != null && this.callback != null
        && this.callback.onCondition(rule, condition) == CallbackResult.STOP) {
      return CallbackResult.STOP;
    }
    return CallbackResult.CONTINUE;
  }

  /**
   * Reads the XACML XML policy file in and returns the version contained in the
   * root Policy/PolicySet
   * element.
   *
   * @param policy
   *          - The policy file.
   * @return - The version string from the file (uninterpreted)
   * @throws java.io.IOException
   */
  public static String getVersion(final Path policy) throws IOException {
    Object data = null;
    try (InputStream is = Files.newInputStream(policy)) {
      data = XACMLPolicyScanner.readPolicy(is);
    } catch (final IOException e) {
      logger.error("Failed to read policy", e);
      throw e;
    }
    if (data == null) {
      logger.warn("Version is null.");
      return null;
    }
    return getVersion(data);
  }

  /**
   * Reads the Policy/PolicySet element object and returns its current version.
   *
   * @param data
   *          - Either a PolicySet or Policy XACML type object.
   * @return - The integer version value. -1 if it doesn't exist or was
   *         un-parsable.
   */
  public static String getVersion(final Object data) {
    String version = null;
    try {
      if (data instanceof PolicySetType) {
        version = ((PolicySetType) data).getVersion();
      } else if (data instanceof PolicyType) {
        version = ((PolicyType) data).getVersion();
      } else {
        if (data != null) {
          logger
              .error("Expecting a PolicySet/Policy/Rule object. Got: " + data.getClass().getCanonicalName());
        }
        return null;
      }
      if (version != null && version.length() > 0) {
        return version;
      }
      logger.warn("No version set in policy");
    } catch (final NumberFormatException e) {
      logger.error("Invalid version contained in policy: " + version);
      return null;
    }
    return null;
  }

  /**
   * Returns the Policy or PolicySet ID.
   *
   * @param data
   *          - A XACML 3.0 Policy or PolicySet element object.
   * @return The policy/policyset's policy ID
   */
  public static String getID(final Object data) {
    if (data instanceof PolicySetType) {
      return ((PolicySetType) data).getPolicySetId();
    } else if (data instanceof PolicyType) {
      return ((PolicyType) data).getPolicyId();
    } else {
      logger.error("Expecting a PolicySet/Policy/Rule object. Got: " + data.getClass().getCanonicalName());
      return null;
    }
  }

  /**
   * readPolicy - does the work to read in policy data from a file.
   *
   * @param policy
   *          - The path to the policy file.
   * @return - The policy data object. This *should* be either a PolicySet or a
   *         Policy.
   */
  public static Object readPolicy(final InputStream is) {

    try {
      //
      // Parse the policy file
      //
      final DocumentBuilder db = DOMUtil.getDocumentBuilder();
      final Document doc = db.parse(is);
      if(initException!=null) {
        throw initException;
      }
      //
      // Because there is no root defined in xacml,
      // find the first element
      //
      final NodeList nodes = doc.getChildNodes();
      Node node = nodes.item(0);
      if (node.getNodeType() == 8) {
        node = nodes.item(1);
      }
      Element e = null;
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        e = (Element) node;
        //
        // Is it a 3.0 policy?
        //
        if (e.getNamespaceURI().equals("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
          //
          // A policyset or policy could be the root
          //
          if (e.getNodeName().endsWith("Policy")) {
            final JAXBElement<PolicyType> root = policyUnmarshaller.unmarshal(e, PolicyType.class);
            //
            // Here is our policy set class
            //
            return root.getValue();
          } else if (e.getNodeName().endsWith("PolicySet")) {
            final JAXBElement<PolicySetType> root = setUnmarshaller.unmarshal(e, PolicySetType.class);
            //
            // Here is our policy set class
            //
            return root.getValue();
          } else {
            if (logger.isDebugEnabled()) {
              logger.debug("Not supported yet: " + e.getNodeName());
            }
          }
        } else {
          logger.warn("unsupported namespace: " + e.getNamespaceURI());
        }
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("No root element contained in policy " + " Name: " + node.getNodeName() + " type: "
              + node.getNodeType() + " Value: " + node.getNodeValue());
        }
      }
    } catch (final Exception e) {
      logger.error(e.getMessage());
    }
    return null;
  }
}
