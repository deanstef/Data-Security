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

import java.util.HashMap;
import java.util.Map;

import org.apache.openaz.xacml.api.DataType;
import org.apache.openaz.xacml.api.DataTypeFactory;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.std.datatypes.DataTypes;

/**
 * Implements the {@link org.apache.openaz.xacml.api.DataTypeFactory} interface
 * to support all of the data
 * types in the XACML 3.0 specification.
 */
public class ThreadSafeDataTypeFactory extends DataTypeFactory {

  private static enum Container {
      INSTANCE;

    private Container() {
      registerDataType(DataTypes.DT_ANYURI);
      registerDataType(DataTypes.DT_BASE64BINARY);
      registerDataType(DataTypes.DT_BOOLEAN);
      registerDataType(DataTypes.DT_DATE);
      registerDataType(DataTypes.DT_DATETIME);
      registerDataType(DataTypes.DT_DAYTIMEDURATION);
      registerDataType(DataTypes.DT_DNSNAME);
      registerDataType(DataTypes.DT_DOUBLE);
      registerDataType(DataTypes.DT_HEXBINARY);
      registerDataType(DataTypes.DT_INTEGER);
      registerDataType(DataTypes.DT_IPADDRESS);
      registerDataType(DataTypes.DT_RFC822NAME);
      registerDataType(DataTypes.DT_STRING);
      registerDataType(DataTypes.DT_TIME);
      registerDataType(DataTypes.DT_X500NAME);
      registerDataType(DataTypes.DT_XPATHEXPRESSION);
      registerDataType(DataTypes.DT_YEARMONTHDURATION);
    }

    private final Map<Identifier, DataType<?>> mapIdentifiersToDataTypes = new HashMap<>();

    private void registerDataType(final DataType<?> dataType) {
      if (dataType != null && dataType.getId() != null) {
        mapIdentifiersToDataTypes.put(dataType.getId(), dataType);
      }
    }
  }

  @Override
  public DataType<?> getDataType(final Identifier dataTypeId) {
    return Container.INSTANCE.mapIdentifiersToDataTypes.get(dataTypeId);
  }
}
