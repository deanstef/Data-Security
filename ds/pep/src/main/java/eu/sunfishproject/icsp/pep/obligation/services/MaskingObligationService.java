package eu.sunfishproject.icsp.pep.obligation.services;

import eu.sunfishproject.icsp.pep.net.api.v1.PEPRequestResource;
import eu.sunfishproject.icsp.pep.obligation.ObligationService;
import eu.sunfishproject.icsp.pep.obligation.impl.MaskingObligationImpl;
import eu.sunfishproject.icsp.pep.obligation.services.masking.IBMMaskingProvider;
import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingDataType;
import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.AttributeAssignment;
import org.apache.openaz.xacml.api.Obligation;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.ObligationException;
import org.sunfish.icsp.common.rest.model.SunfishHttpObject;
import org.sunfish.icsp.common.xacml.XACMLConstants;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Iterator;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class MaskingObligationService implements ObligationService {

    private static final Logger log = LogManager.getLogger(MaskingObligationService.class);



    @Override
    public boolean canHandleObligation(final String obligationId) {
        return XACMLConstants.OBLIGATION_ID_MASK.equals(obligationId);
    }


    @Override
    public void handleObligation(Obligation obligation, SunfishHttpObject sunfishHttpObject, Object... arguments) throws ObligationException {

        try {


            MaskingObligationImpl maskingObligation = new MaskingObligationImpl(obligation);

            handleMaskingObligation(maskingObligation, sunfishHttpObject);


        } catch (Exception e) {
            throw new ObligationException("Could not fulfill obligation");
        }

    }


    private void handleMaskingObligation(MaskingObligationImpl maskingObligation, SunfishHttpObject sunfishHttpObject) throws ICSPException {

        MaskingProvider maskingProvider = new IBMMaskingProvider("http://195.110.40.69:50002/DM/v1/");
        MaskingDataType maskingDataType = MaskingDataType.TYPE_TEXT;


        byte[] maskedData;
        switch (maskingObligation.getObligationId()) {
            case "encrypt" :
                maskedData = maskingProvider.mask(sunfishHttpObject.getBody(), maskingDataType, maskingObligation.getMaskingPolicy(), maskingObligation.getMaskingContext());
                break;
            case "decrypt" :
                maskedData = maskingProvider.unmask(sunfishHttpObject.getBody(), maskingDataType, maskingObligation.getMaskingPolicy(), maskingObligation.getMaskingContext());
                break;
            default:
                maskedData = null;
                break;
        }


        sunfishHttpObject.setBody(maskedData);


    }


}
