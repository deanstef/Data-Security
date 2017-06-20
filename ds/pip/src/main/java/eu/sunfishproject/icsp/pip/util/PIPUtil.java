package eu.sunfishproject.icsp.pip.util;

import org.apache.openaz.xacml.api.AttributeValue;
import org.sunfish.icsp.common.xacml.XMLDataTypes;

/**
 * Created by dziegler on 19/10/2016.
 */
public class PIPUtil {


    public static String getStringForAttributeValue(AttributeValue<?> attributeValue) {

        if(attributeValue.getDataTypeId().toString().equals(XMLDataTypes.XML_TYPE_STRING)) {
            return attributeValue.getValue().toString();
        }

        return "";

    }


}
