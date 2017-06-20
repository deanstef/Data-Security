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
import org.apache.openaz.xacml.pdp.policy.FunctionDefinition;
import org.apache.openaz.xacml.pdp.policy.FunctionDefinitionFactory;
import org.apache.openaz.xacml.pdp.std.StdFunctions;

/**
 * StdFunctionDefinitionFactory is the default
 * {@link org.apache.openaz.xacml.pdp.policy.FunctionDefinitionFactory}
 * implementation used if no other
 * <code>FunctionDefinitionFactory</code> implementation is supplied. It
 * contains all of the standard XACML
 * 3.0 functions.
 */
public class ThreadSafeFunctionDefinitionFactory extends FunctionDefinitionFactory {

  private static enum Container {
      INSTANCE;
    private final Map<Identifier, FunctionDefinition> mapFunctionDefinitions = new HashMap<>();

    private void register(final FunctionDefinition functionDefinition) {
      mapFunctionDefinitions.put(functionDefinition.getId(), functionDefinition);
    }

    private Container() {
      final Field[] declaredFields = StdFunctions.class.getDeclaredFields();
      for (final Field field : declaredFields) {
        if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith(StdFunctions.FD_PREFIX)
            && FunctionDefinition.class.isAssignableFrom(field.getType())
            && Modifier.isPublic(field.getModifiers())) {
          try {
            register((FunctionDefinition) (field.get(null)));
          } catch (final IllegalAccessException ex) { // NOPMD
            // TODO
          }
        }
      }
    }
  }

  public ThreadSafeFunctionDefinitionFactory() {
  }

  @Override
  public FunctionDefinition getFunctionDefinition(final Identifier functionId) {
    return Container.INSTANCE.mapFunctionDefinitions.get(functionId);
  }
}
