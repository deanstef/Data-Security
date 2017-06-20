package eu.sunfishproject.icsp.pip.attribute;

import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.api.Request;

import java.util.List;


/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface AttributeProvider {

    boolean supportsAttribute(AttributeDesignator attribute);

    List<AttributeValue> getValuesForAttribute(AttributeDesignator requestedAttribute, Request request);

}
