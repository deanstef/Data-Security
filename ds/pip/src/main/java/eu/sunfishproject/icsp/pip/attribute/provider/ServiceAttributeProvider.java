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
public class ServiceAttributeProvider implements AttributeProvider {

    @Override
    public boolean supportsAttribute(final AttributeDesignator attribute) {
        return (attribute.getCategory().stringValue().equals(XACMLConstants.CATEGORY_TARGET) &&
                attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_TARGET_HOST) &&
                attribute.getDataTypeId().stringValue().equals(XMLDataTypes.XML_TYPE_STRING)) ||

                (attribute.getCategory().stringValue().equals(XACMLConstants.CATEGORY_TARGET) &&
                 attribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_TARGET_ZONE) &&
                attribute.getDataTypeId().stringValue().equals(XMLDataTypes.XML_TYPE_STRING));
    }

    @Override
    public List<AttributeValue> getValuesForAttribute(final AttributeDesignator requestedAttribute, final Request request) {


        List<AttributeValue> requestedAttributeValues = new ArrayList<>();
        Iterator<RequestAttributes> it = request.getRequestAttributes(new IdentifierImpl(XACMLConstants.CATEGORY_SERVICE));


        while(it.hasNext()) {
            RequestAttributes requestAttribute = it.next();
            Iterator<Attribute> attributeIterators = requestAttribute.getAttributes(new IdentifierImpl(XACMLConstants.ATTRIBUTE_ID));
            while(attributeIterators.hasNext()) {
                Attribute attributeId = attributeIterators.next();
                Collection<AttributeValue<?>> attributeIdValues = attributeId.getValues();
                for(AttributeValue<?> attributeIdValue : attributeIdValues) {

                    String serviceId = PIPUtil.getStringForAttributeValue(attributeIdValue);


                    if(requestedAttribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_TARGET_ZONE)) {
                        String zone = FileDatabaseService.getInstance().getZoneForServiceId(serviceId);

                        if(zone != null && !zone.isEmpty()) {
                            zone = zone.trim();
                            requestedAttributeValues.add(XACMLUtils.createStringAttributeValue(zone));
                        }

                    }
                    else if (requestedAttribute.getAttributeId().stringValue().equals(XACMLConstants.ATTRIBUTE_TARGET_HOST)) {
                        String host = FileDatabaseService.getInstance().getHostForServiceId(serviceId);

                        if(host != null && !host.isEmpty()) {
                            host = host.trim();
                            requestedAttributeValues.add(XACMLUtils.createStringAttributeValue(host));
                        }
                        else {
                            // TODO: Just for debug to support generic applications
                            requestedAttributeValues.add(XACMLUtils.createStringAttributeValue(serviceId));
                        }

                    }
                }
            }

        }


        return requestedAttributeValues;
    }


}
