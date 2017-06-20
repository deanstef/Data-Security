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
 *          Copyright (c) 2013 AT&T Knowledge Ventures
 *              Unpublished and Not for Publication
 *                     All Rights Reserved
 */
package org.sunfish.icsp.common.factories;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.pdp.policy.CombiningAlgorithm;
import org.apache.openaz.xacml.pdp.policy.CombiningAlgorithmFactory;
import org.apache.openaz.xacml.pdp.policy.PolicySetChild;
import org.apache.openaz.xacml.pdp.policy.Rule;
import org.apache.openaz.xacml.pdp.std.StdCombiningAlgorithms;

/**
 * StdCombiningAlgorithmFactory extends
 * {@link org.apache.openaz.xacml.pdp.policy.CombiningAlgorithmFactory}
 * to implement a mapping from {@link org.apache.openaz.xacml.api.Identifier}s
 * to the standard
 * {@link org.apache.openaz.xacml.pdp.policy.CombiningAlgorithm}
 * implementations.
 */
public class ThreadSafeCombiningAlgorithmFactory extends CombiningAlgorithmFactory {

  private enum Container {
      INSTANCE;

    private Container() {
      final Field[] declaredFields = StdCombiningAlgorithms.class.getFields();
      for (final Field field : declaredFields) {
        if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())
            && field.getName().startsWith(StdCombiningAlgorithms.PREFIX_CA)
            && CombiningAlgorithm.class.isAssignableFrom(field.getType())) {
          try {
            if (field.getName().startsWith(StdCombiningAlgorithms.PREFIX_RULE)) {
              registerRuleCombiningAlgorithm((CombiningAlgorithm<Rule>) field.get(null));
            } else if (field.getName().startsWith(StdCombiningAlgorithms.PREFIX_POLICY)) {
              registerPolicyCombiningAlgorithm((CombiningAlgorithm<PolicySetChild>) field.get(null));
            }
          } catch (final IllegalAccessException ex) { // NOPMD
            // TODO
          }
        }
      }
    }

    private final Map<Identifier, CombiningAlgorithm<Rule>>           mapRuleCombiningAlgorithms   = new HashMap<>();
    private final Map<Identifier, CombiningAlgorithm<PolicySetChild>> mapPolicyCombiningAlgorithms = new HashMap<>();

    protected void registerRuleCombiningAlgorithm(final CombiningAlgorithm<Rule> ruleCombiningAlgorithm) {
      mapRuleCombiningAlgorithms.put(ruleCombiningAlgorithm.getId(), ruleCombiningAlgorithm);
    }

    protected void registerPolicyCombiningAlgorithm(
        final CombiningAlgorithm<PolicySetChild> policyCombiningAlgorithm) {
      mapPolicyCombiningAlgorithms.put(policyCombiningAlgorithm.getId(), policyCombiningAlgorithm);
    }

  }

  public ThreadSafeCombiningAlgorithmFactory() {
  }

  @Override
  public CombiningAlgorithm<Rule> getRuleCombiningAlgorithm(final Identifier combiningAlgorithmId) {
    return Container.INSTANCE.mapRuleCombiningAlgorithms.get(combiningAlgorithmId);
  }

  @Override
  public CombiningAlgorithm<PolicySetChild> getPolicyCombiningAlgorithm(final Identifier combiningAlgorithmId) {
    return Container.INSTANCE.mapPolicyCombiningAlgorithms.get(combiningAlgorithmId);
  }
}
