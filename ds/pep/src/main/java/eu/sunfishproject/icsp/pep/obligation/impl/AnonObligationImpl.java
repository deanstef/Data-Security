package eu.sunfishproject.icsp.pep.obligation.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.AttributeAssignment;
import org.apache.openaz.xacml.api.Obligation;

import java.util.Iterator;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class AnonObligationImpl {

    private String configId = null;

    private static final Logger log = LogManager.getLogger(AnonObligationImpl.class);


    public AnonObligationImpl(Obligation obligation) {


        Iterator<AttributeAssignment> iterator = obligation.getAttributeAssignments().iterator();

        while (iterator.hasNext()) {
            AttributeAssignment attribute = iterator.next();

            String category = attribute.getCategory().stringValue();

            switch (category) {
                case "ConfigId":
                    this.setConfigId(attribute.getAttributeValue().getValue().toString());
                    break;
                default:
                    log.info("Unknown Attribute category: " + category);
                    break;
            }

        }

    }


    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }
}
