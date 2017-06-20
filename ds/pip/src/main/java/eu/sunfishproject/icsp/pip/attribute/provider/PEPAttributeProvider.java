package eu.sunfishproject.icsp.pip.attribute.provider;

import eu.sunfishproject.icsp.pip.attribute.AttributeProvider;
import eu.sunfishproject.icsp.pip.database.FileDatabaseService;
import eu.sunfishproject.icsp.pip.util.PIPUtil;
import org.apache.openaz.xacml.api.Attribute;
import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.RequestAttributes;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.sunfish.icsp.common.util.CommonUtil;
import org.sunfish.icsp.common.xacml.XACMLConstants;
import org.sunfish.icsp.common.xacml.XACMLUtils;
import org.sunfish.icsp.common.xacml.XMLDataTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PEPAttributeProvider implements AttributeProvider {


    @Override
    public boolean supportsAttribute(final AttributeDesignator attribute) {
        return attribute.getCategory().stringValue().equals(XACMLConstants.CATEGORY_PEP) &&
                attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_SERVICE_PEP) &&
                attribute.getDataTypeId().stringValue().equals(XMLDataTypes.XML_TYPE_STRING);
    }

    @Override
    public List<AttributeValue> getValuesForAttribute(final AttributeDesignator requestedAttribute, final Request request) {

        List<AttributeValue> requestedAttributeValues = new ArrayList<>();
        Iterator<RequestAttributes> it = request.getRequestAttributes(new IdentifierImpl(XACMLConstants.CATEGORY_SERVICE));


        while(it.hasNext()) {
            RequestAttributes requestAttribute = it.next();
            Iterator<Attribute> attributeIterators = requestAttribute.getAttributes(new IdentifierImpl(XACMLConstants.ATTRIBUTE_SERVICE_ZONE));
            while(attributeIterators.hasNext()) {
                Attribute attributeId = attributeIterators.next();
                Collection<AttributeValue<?>> attributeIdValues = attributeId.getValues();
                for(AttributeValue<?> attributeIdValue : attributeIdValues) {

                    String zone = PIPUtil.getStringForAttributeValue(attributeIdValue);
                    String pep = FileDatabaseService.getInstance().getPEPForZone(zone);
                    requestedAttributeValues.add(XACMLUtils.createStringAttributeValue(pep));

                }
            }

        }

        return requestedAttributeValues;
    }
}
