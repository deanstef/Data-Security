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

package org.apache.openaz.xacml.admin.view.fields;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.openaz.xacml.admin.jpa.Attribute;
import org.apache.openaz.xacml.admin.jpa.ConstraintType;
import org.apache.openaz.xacml.admin.view.components.EnumerationEditorComponent;
import org.apache.openaz.xacml.admin.view.components.RangeEditorComponent;
import org.apache.openaz.xacml.admin.view.components.RegexpEditorComponent;
import org.apache.openaz.xacml.api.Identifier;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

public class ConstraintField extends CustomField<Object> {
	private static final long serialVersionUID = 1L;
	private static final Log logger	= LogFactory.getLog(ConstraintField.class);
	
	private VerticalLayout mainLayout = new VerticalLayout();
	private EntityItem<Attribute>	attributeEntity;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ConstraintField(EntityItem<Attribute> entityItem) {
		//
		// Save our Attribute that we are editing
		//
		this.attributeEntity = entityItem;
	}
	
	public void	resetDatatype(Identifier id) {
		if (logger.isDebugEnabled()) {
			logger.debug("resetDatatype " + id);
		}
		if (this.mainLayout.getComponentCount() == 0) {
			return;
		}
		Component component = this.mainLayout.getComponent(0);
		if (component != null && component instanceof RangeEditorComponent) {
			((RangeEditorComponent) component).setupDatatype(id);
		}
		if (component != null && component instanceof EnumerationEditorComponent) {
			((EnumerationEditorComponent) component).setupDatatype(id);
		}
	}
	
	public Component resetContent(ConstraintType type, Identifier datatype) {
		//
		// Remove all existing components
		//
		this.mainLayout.removeAllComponents();
		//
		// What are we constrained to?
		//
		if (type == null || type.getConstraintType() == null) {
			//
			// No constraint defined
			//
			this.attributeEntity.getEntity().removeAllConstraintValues();
			return mainLayout;
		}
		if (type.getConstraintType().equals("Enumeration")) {
			mainLayout.addComponent(new EnumerationEditorComponent(this.attributeEntity.getEntity(), datatype));
		} else if (type.getConstraintType().equals("Range")) {
			mainLayout.addComponent(new RangeEditorComponent(this.attributeEntity.getEntity(), datatype));
		} else if (type.getConstraintType().equals("Regular Expression")) {
			mainLayout.addComponent(new RegexpEditorComponent(this.attributeEntity.getEntity()));
		}
		return mainLayout;
	}

	@Override
	protected Component initContent() {
		return this.resetContent(this.attributeEntity.getEntity().getConstraintType(), this.attributeEntity.getEntity().getDatatypeBean().getIdentifer());
	}

	@Override
	public Class<?> getType() {
		return Collection.class;
	}

}
