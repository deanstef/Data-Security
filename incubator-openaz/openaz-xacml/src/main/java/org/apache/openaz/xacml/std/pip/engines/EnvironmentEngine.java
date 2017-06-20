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
package org.apache.openaz.xacml.std.pip.engines;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.DataTypeException;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.SUNFISH;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.api.pip.PIPEngine;
import org.apache.openaz.xacml.api.pip.PIPException;
import org.apache.openaz.xacml.api.pip.PIPFinder;
import org.apache.openaz.xacml.api.pip.PIPRequest;
import org.apache.openaz.xacml.api.pip.PIPResponse;
import org.apache.openaz.xacml.std.StdMutableAttribute;
import org.apache.openaz.xacml.std.datatypes.DataTypes;
import org.apache.openaz.xacml.std.pip.StdPIPRequest;
import org.apache.openaz.xacml.std.pip.StdPIPResponse;
import org.apache.openaz.xacml.std.pip.StdSinglePIPResponse;

/**
 * EnvironmentEngine implements the {@link org.apache.openaz.xacml.api.pip.PipEngine} interface to provide
 * values for standard environment attributes.
 */
public class EnvironmentEngine implements PIPEngine {
    private final Date contextTime;

    private StdSinglePIPResponse responseTime;
    private StdSinglePIPResponse responseDate;
    private StdSinglePIPResponse responseDay;
    private StdSinglePIPResponse responseMonth;
    private StdSinglePIPResponse responseYear;
    private StdSinglePIPResponse responseDateTime;

    protected StdSinglePIPResponse getResponseTime() throws DataTypeException {
        if (this.responseTime == null) {
            this.responseTime = new StdSinglePIPResponse(
                                                         new StdMutableAttribute(
                                                                                 XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                                                                                 XACML3.ID_ENVIRONMENT_CURRENT_TIME,
                                                                                 DataTypes.DT_TIME
                                                                                     .createAttributeValue(this.contextTime)));
        }
        return this.responseTime;
    }

    protected StdSinglePIPResponse getResponseDate() throws DataTypeException {
        if (this.responseDate == null) {
            this.responseDate = new StdSinglePIPResponse(
                                                         new StdMutableAttribute(
                                                                                 XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                                                                                 XACML3.ID_ENVIRONMENT_CURRENT_DATE,
                                                                                 DataTypes.DT_DATE
                                                                                     .createAttributeValue(this.contextTime)));
        }
        return this.responseDate;
    }

    protected StdSinglePIPResponse getResponseDateTime() throws DataTypeException {
        if (this.responseDateTime == null) {
            this.responseDateTime = new StdSinglePIPResponse(
                                                             new StdMutableAttribute(
                                                                                     XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                                                                                     XACML3.ID_ENVIRONMENT_CURRENT_DATETIME,
                                                                                     DataTypes.DT_DATETIME
                                                                                         .createAttributeValue(this.contextTime)));
        }
        return this.responseDateTime;
    }


    protected StdSinglePIPResponse getResponseDay() throws DataTypeException {
      if (this.responseDay == null) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(contextTime);
        this.responseDay = new StdSinglePIPResponse(
            new StdMutableAttribute(
                XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                SUNFISH.ID_ENVIRONMENT_CURRENT_DAY,
                DataTypes.DT_INTEGER
                .createAttributeValue(
                    instance.get(Calendar.DAY_OF_MONTH)
                   )));
      }
      return this.responseDay;
    }
    protected StdSinglePIPResponse getResponseMonth() throws DataTypeException {
      if (this.responseMonth == null) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(contextTime);
        this.responseMonth = new StdSinglePIPResponse(
            new StdMutableAttribute(
                XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                SUNFISH.ID_ENVIRONMENT_CURRENT_MONTH,
                DataTypes.DT_INTEGER
                .createAttributeValue(
                    instance.get(Calendar.MONTH)
                    )));
      }
      return this.responseMonth;
    }
    protected StdSinglePIPResponse getResponseYear() throws DataTypeException {
      if (this.responseYear == null) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(contextTime);
        this.responseYear = new StdSinglePIPResponse(
            new StdMutableAttribute(
                XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                SUNFISH.ID_ENVIRONMENT_CURRENT_YEAR,
                DataTypes.DT_INTEGER
                .createAttributeValue(
                    instance.get(Calendar.YEAR)
                    )));
      }
      return this.responseYear;
    }



    public EnvironmentEngine(final Date dateContextTimeIn) {
        this.contextTime = dateContextTimeIn;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String getDescription() {
        return "Environment attribute PIP";
    }

    @Override
    public Collection<PIPRequest> attributesRequired() {
        return Collections.emptyList();
    }

    @Override
    public Collection<PIPRequest> attributesProvided() {
        final List<PIPRequest> attributes = new ArrayList<>();
        attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                                         XACML3.ID_ENVIRONMENT_CURRENT_DATE, XACML3.ID_DATATYPE_DATE, null));
        attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                                         XACML3.ID_ENVIRONMENT_CURRENT_TIME, XACML3.ID_DATATYPE_TIME, null));
        attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
                                         XACML3.ID_ENVIRONMENT_CURRENT_DATETIME, XACML3.ID_DATATYPE_DATETIME,
                                         null));
        attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
            SUNFISH.ID_ENVIRONMENT_CURRENT_DAY, XACML3.ID_DATATYPE_INTEGER,
            null));
        attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
            SUNFISH.ID_ENVIRONMENT_CURRENT_MONTH, XACML3.ID_DATATYPE_INTEGER,
            null));
        attributes.add(new StdPIPRequest(XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT,
            SUNFISH.ID_ENVIRONMENT_CURRENT_YEAR, XACML3.ID_DATATYPE_INTEGER,
            null));

        return attributes;
    }

    @Override
    public PIPResponse getAttributes(final PIPRequest pipRequest, final PIPFinder pipFinder) throws PIPException {
        /*
         * Make sure this is a request for an environment attribute and no issuer has been set
         */
        if (!XACML3.ID_ATTRIBUTE_CATEGORY_ENVIRONMENT.equals(pipRequest.getCategory())
            || pipRequest.getIssuer() != null && pipRequest.getIssuer().length() > 0) {
            return StdPIPResponse.PIP_RESPONSE_EMPTY;
        }

        /*
         * See which environment attribute is requested
         */
        final Identifier attributeIdRequest = pipRequest.getAttributeId();
        StdSinglePIPResponse pipResponse = null;
        try {
            if (XACML3.ID_ENVIRONMENT_CURRENT_DATE.equals(attributeIdRequest)) {
                pipResponse = this.getResponseDate();
            } else if (XACML3.ID_ENVIRONMENT_CURRENT_TIME.equals(attributeIdRequest)) {
                pipResponse = this.getResponseTime();
            }
            else if (XACML3.ID_ENVIRONMENT_CURRENT_DATETIME.equals(attributeIdRequest)) {
                pipResponse = this.getResponseDateTime();
            }
            else if (SUNFISH.ID_ENVIRONMENT_CURRENT_DAY.equals(attributeIdRequest)) {
              pipResponse = this.getResponseDay();
            }
            else if (SUNFISH.ID_ENVIRONMENT_CURRENT_MONTH.equals(attributeIdRequest)) {
              pipResponse = this.getResponseMonth();
            }
            else if (SUNFISH.ID_ENVIRONMENT_CURRENT_YEAR.equals(attributeIdRequest)) {
              pipResponse = this.getResponseYear();
            }
        } catch (final DataTypeException ex) {
            throw new PIPException("DataTypeException getting \"" + attributeIdRequest.stringValue() + "\"",
                                   ex);
        }

        if (pipResponse == null) {
            return StdPIPResponse.PIP_RESPONSE_EMPTY;
        }

        /*
         * Ensure the data types match
         */
        final AttributeValue<?> attributeValuePipResponse = pipResponse.getSingleAttribute().getValues().iterator()
            .next();
        if (attributeValuePipResponse.getDataTypeId().equals(pipRequest.getDataTypeId())) {
            return pipResponse;
        } else {
            return StdPIPResponse.PIP_RESPONSE_EMPTY;
        }
    }

}
