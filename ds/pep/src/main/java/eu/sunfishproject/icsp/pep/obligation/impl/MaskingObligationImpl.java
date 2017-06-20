package eu.sunfishproject.icsp.pep.obligation.impl;

import eu.sunfishproject.icsp.pep.obligation.services.MaskingObligationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.AttributeAssignment;
import org.apache.openaz.xacml.api.Obligation;

import java.util.Iterator;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class MaskingObligationImpl {

    private String obligationId = null;
    private String maskingContext = null;
    private String maskingPolicy = null;

    private static final Logger log = LogManager.getLogger(MaskingObligationImpl.class);


    public MaskingObligationImpl(Obligation obligation) {



        Iterator<AttributeAssignment> iterator = obligation.getAttributeAssignments().iterator();

        while(iterator.hasNext()) {
            AttributeAssignment attribute = iterator.next();

            String category = attribute.getCategory().stringValue();

            switch (category) {
                case "MaskingContext":
                    this.maskingContext = attribute.getAttributeValue().getValue().toString();
                    break;
                case "MaskingPolicyId":
                    this.maskingPolicy = attribute.getAttributeValue().getValue().toString();
                    break;
                case "ObligationId":
                    this.obligationId = attribute.getAttributeValue().getValue().toString();
                    break;
                default:
                    log.info("Unknown Attribute category: " + category);
                    break;
            }

        }

    }


    public String getObligationId() {
        return obligationId;
    }

    public void setObligationId(String obligationId) {
        this.obligationId = obligationId;
    }

    public String getMaskingContext() {
        return maskingContext;
    }

    public void setMaskingContext(String maskingContext) {
        this.maskingContext = maskingContext;
    }

    public String getMaskingPolicy() {
        return maskingPolicy;
    }

    public void setMaskingPolicy(String maskingPolicy) {
        this.maskingPolicy = maskingPolicy;
    }
}
