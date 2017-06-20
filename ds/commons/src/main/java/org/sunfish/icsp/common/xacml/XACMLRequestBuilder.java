package org.sunfish.icsp.common.xacml;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.*;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.apache.openaz.xacml.std.StdMutableAttribute;
import org.apache.openaz.xacml.std.StdMutableRequestAttributes;
import org.sunfish.icsp.common.xacml.exceptions.XACMLBuilderException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class XACMLRequestBuilder {


    private final SunfishMutableRequest request;
    private StdMutableRequestAttributes currentAttributes;
    private final Logger log = LogManager.getLogger(XACMLRequestBuilder.class);

    public XACMLRequestBuilder() {
        this.request = new SunfishMutableRequest();
    }

    public XACMLRequestBuilder(Request request) {
        this.request = new SunfishMutableRequest(request);
    }


    public XACMLRequestBuilder setCategory(String category) {

        return this.setCategory(new IdentifierImpl(category));
    }

    public XACMLRequestBuilder setCategory(Identifier category) {


        if(this.currentAttributes == null || !this.currentAttributes.getCategory().equals(category)) {

            closeCurrentAttributes();

            this.currentAttributes = new StdMutableRequestAttributes();
            this.currentAttributes.setCategory(category);


            // Check if category already exists, and remove, but keep children
            Collection<RequestAttributes> requestAttributes = this.request.getRequestAttributes();

            for(RequestAttributes requestAttributeCategory : requestAttributes) {

                if(requestAttributeCategory.getCategory().stringValue().equals(category.stringValue())) {
                    Collection<Attribute> attributes =  requestAttributeCategory.getAttributes();
                    for(Attribute attribute : attributes) {
                        this.currentAttributes.add(attribute);
                    }
                    this.request.remove(requestAttributeCategory);
                    break;
                }
            }
        }

        return this;

    }

    public XACMLRequestBuilder removeCategory(String category) {

        if(this.currentAttributes != null && this.currentAttributes.getCategory().stringValue().equals(category)) {

            this.currentAttributes = new StdMutableRequestAttributes();
        }
        else {

            // Check if category already exists, and remove
            Collection<RequestAttributes> requestAttributes = this.request.getRequestAttributes();

            for(RequestAttributes requestAttributeCategory : requestAttributes) {

                if(requestAttributeCategory.getCategory().stringValue().equals(category)) {
                    this.request.remove(requestAttributeCategory);
                    break;
                }
            }
        }

        return this;


    }


    public XACMLRequestBuilder addStringAttribute(String attribute, String value) throws XACMLBuilderException {

        StdAttributeValue<String> stringAttribute = XACMLUtils.createStringAttributeValue(value);
        addAttribute(attribute, stringAttribute);

        return this;

    }

    public XACMLRequestBuilder addStringAttributes(String attribute, List<String> values) throws XACMLBuilderException {

        List<AttributeValue> attributeValues = new ArrayList<>();

        for(String value : values) {
            StdAttributeValue<String> stringAttribute = XACMLUtils.createStringAttributeValue(value);
            attributeValues.add(stringAttribute);
        }

        addAttribute(attribute, attributeValues);

        return this;

    }

    public XACMLRequestBuilder addBooleanAttribute(String attribute, boolean value) throws XACMLBuilderException {

        StdAttributeValue<Boolean> stringAttribute = XACMLUtils.createBooleanAttributeValue(value);
        addAttribute(attribute, stringAttribute);

        return this;
    }


    public XACMLRequestBuilder addIntegerAttribute(String attribute, int value) throws XACMLBuilderException {

        StdAttributeValue<Integer> stringAttribute = XACMLUtils.createIntegerAttributeValue(value);
        addAttribute(attribute, stringAttribute);

        return this;
    }



    public XACMLRequestBuilder addDateTimeAttribute(String attribute, Date value) throws XACMLBuilderException {

        StdAttributeValue<String> dateAttribute = XACMLUtils.createDateTimeAttributeValue(value);
        addAttribute(attribute, dateAttribute);

        return this;
    }

    public XACMLRequestBuilder addCDATAAttribute(String attribute, String value) throws XACMLBuilderException {

        value = "<![CDATA[" + value + "]]>";
        return addStringAttribute(attribute, value);
    }

    public XACMLRequestBuilder addBase64Attribute(String attribute, byte[] value) throws XACMLBuilderException {

        String base64Value = Base64.encodeBase64String(value);
        return addStringAttribute(attribute, base64Value);
    }




    public <T> XACMLRequestBuilder addAttribute(String attributeId, String dataType, T value) throws XACMLBuilderException {

        StdAttributeValue<T> attribute =
                new StdAttributeValue<>(new IdentifierImpl(dataType), value);

        addAttribute(attributeId, attribute);

        return this;

    }


    public void addAttribute(String attributeId, AttributeValue value) throws XACMLBuilderException {

        checkCurrentAttributes();

        if(value.getValue() != null) {
            StdMutableAttribute attribute = new StdMutableAttribute();
            attribute.setCategory(this.currentAttributes.getCategory());
            attribute.setAttributeId(new IdentifierImpl(attributeId));
            attribute.addValue(value);

            this.currentAttributes.add(attribute);
        } else {
            log.debug("Ignoring NULL value for attribute: " + attributeId);
        }

    }

    public void addAttribute(String attributeId, List<AttributeValue> values) throws XACMLBuilderException {

        checkCurrentAttributes();

        if(values != null && !values.isEmpty()) {

            StdMutableAttribute attribute = new StdMutableAttribute();
            attribute.setCategory(this.currentAttributes.getCategory());
            attribute.setAttributeId(new IdentifierImpl(attributeId));

            for (AttributeValue value : values) {
                attribute.addValue(value);
            }

            this.currentAttributes.add(attribute);
        }

    }


    public Request build() {
        closeCurrentAttributes();

        return request;
    }


    private void closeCurrentAttributes() {
        if(this.currentAttributes != null) {
            this.request.add(this.currentAttributes);
            this.currentAttributes = null;
        }
    }




    private void checkCurrentAttributes() throws XACMLBuilderException {

        if(this.currentAttributes == null) {
            throw new XACMLBuilderException("You must start a new category first!");
        }

    }





}
