package eu.sunfishproject.icsp.pip.attribute.provider;

import eu.sunfishproject.icsp.pip.attribute.AttributeProvider;
import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.sunfish.icsp.common.xacml.XACMLConstants;
import org.sunfish.icsp.common.xacml.XACMLUtils;
import org.sunfish.icsp.common.xacml.XMLDataTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class BodyDataAttributeProvider implements AttributeProvider {


    @Override
    public boolean supportsAttribute(final AttributeDesignator attribute) {
        return attribute.getCategory().stringValue().equals(XACMLConstants.CATEGORY_REQUEST) &&
                attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_REQUEST_BODY_DATA) &&
                attribute.getDataTypeId().stringValue().equals(XMLDataTypes.XML_TYPE_STRING);
    }

    @Override
    public List<AttributeValue> getValuesForAttribute(final AttributeDesignator attribute, final Request request) {

        List<AttributeValue> attributeValues = new ArrayList<>();
        attributeValues.add(XACMLUtils.createStringAttributeValue("sfbd20812981"));

        return attributeValues;
    }
}
