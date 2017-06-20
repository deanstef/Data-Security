package org.sunfish.icsp.common.xacml;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.StdAttributeValue;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishResponse;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.util.CommonUtil;
import org.sunfish.icsp.common.xacml.exceptions.XACMLBuilderException;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class XACMLUtils {



    public static StdAttributeValue<String> createStringAttributeValue(String value) {

//        value = StringEscapeUtils.escapeXml(value);
        StdAttributeValue<String> stringAttributeValue =
                new StdAttributeValue<>(new IdentifierImpl(XMLDataTypes.XML_TYPE_STRING), value);

        return stringAttributeValue;

    }

    public static StdAttributeValue<Boolean> createBooleanAttributeValue(Boolean value) {

        StdAttributeValue<Boolean> booleanAttributeValue =
                new StdAttributeValue<>(new IdentifierImpl(XMLDataTypes.XML_TYPE_BOOLEAN), value);

        return booleanAttributeValue;

    }

    public static StdAttributeValue<Integer> createIntegerAttributeValue(int value) {

        StdAttributeValue<Integer> integerAttributeValue =
                new StdAttributeValue<>(new IdentifierImpl(XMLDataTypes.XML_TYPE_INTEGER), value);

        return integerAttributeValue;

    }


    public static StdAttributeValue<String> createURIAttributeValue(String value) {
        StdAttributeValue<String> uriAttributeValue =
                new StdAttributeValue<>(new IdentifierImpl(XMLDataTypes.XML_TYPE_URI), value);

        return uriAttributeValue;

    }


    public static StdAttributeValue<String> createDateTimeAttributeValue(Date value) {

        String dateString = CommonSetup.DATE_FORMAT.format(new Date());

        StdAttributeValue<String> dateAttributeValue =
                new StdAttributeValue<>(new IdentifierImpl(XMLDataTypes.XML_TYPE_DATE_TIME), dateString);

        return dateAttributeValue;

    }


    public static AttributeDesignator createAttributeDesignator(String attributeId, String category, String dataTypeId) {

        final AttributeDesignator designator = new AttributeDesignator();
        designator.setAttributeId(new IdentifierImpl(attributeId));
        designator.setCategory(new IdentifierImpl(category));
        designator.setDataTypeId(new IdentifierImpl(dataTypeId));

        return designator;

    }



}
