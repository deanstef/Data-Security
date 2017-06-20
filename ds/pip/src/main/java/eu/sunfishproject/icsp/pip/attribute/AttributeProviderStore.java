package eu.sunfishproject.icsp.pip.attribute;

import eu.sunfishproject.icsp.pip.attribute.provider.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class AttributeProviderStore {


    private final List<AttributeProvider> attributeProviders;
    private static AttributeProviderStore instance;
    private Logger log = LogManager.getLogger(AttributeProviderStore.class);


    private AttributeProviderStore() {

        attributeProviders = new ArrayList<>();
        attributeProviders.add(new BodyDataAttributeProvider());
        attributeProviders.add(new HeaderDataAttributeProvider());
        attributeProviders.add(new ContentTypeAttributeProvider());
        attributeProviders.add(new ServiceAttributeProvider());
        attributeProviders.add(new PEPAttributeProvider());


    }

    public static AttributeProviderStore getInstance() {
        if (instance == null) {
            instance = new AttributeProviderStore();
        }
        return instance;
    }

    public List<AttributeProvider> getAttributeProviders() {
        return this.attributeProviders;
    }

    public AttributeProvider getAttributeProviderForAttribute(AttributeDesignator attributeDesignator) {

        for(AttributeProvider attributeProvider : this.attributeProviders) {
            if(attributeProvider.supportsAttribute(attributeDesignator)) {
                return attributeProvider;
            }
        }

        return null;
    }


}
