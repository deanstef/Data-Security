package eu.sunfishproject.icsp.pep.obligation.services;

import eu.sunfishproject.icsp.pep.config.PEPConfig;
import eu.sunfishproject.icsp.pep.obligation.ObligationService;
import eu.sunfishproject.icsp.pep.obligation.impl.AnonObligationImpl;
import eu.sunfishproject.icsp.pep.obligation.impl.MaskingObligationImpl;
import eu.sunfishproject.icsp.pep.obligation.services.anon.AnonProvider;
import eu.sunfishproject.icsp.pep.obligation.services.anon.IBMAnonProvider;
import eu.sunfishproject.icsp.pep.obligation.services.masking.IBMMaskingProvider;
import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingDataType;
import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Obligation;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.ObligationException;
import org.sunfish.icsp.common.rest.model.SunfishHttpObject;
import org.sunfish.icsp.common.xacml.XACMLConstants;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class AnonObligationService implements ObligationService {

    private static final Logger log = LogManager.getLogger(AnonObligationService.class);


    @Override
    public boolean canHandleObligation(final String obligationId) {
        return XACMLConstants.OBLIGATION_ID_ANON.equals(obligationId);
    }


    @Override
    public void handleObligation(Obligation obligation, SunfishHttpObject sunfishHttpObject, Object... arguments) throws ObligationException {

        try {
            AnonObligationImpl anon = new AnonObligationImpl(obligation);
            handleAnonObligation(anon, sunfishHttpObject);
        } catch (Exception e) {
            throw new ObligationException("Could not fulfill obligation");
        }
    }


    private void handleAnonObligation(AnonObligationImpl anon, SunfishHttpObject sunfishHttpObject) throws ICSPException {

        AnonProvider    provider        = new IBMAnonProvider(PEPConfig.getInstance().getAnonServiceUrl());
        MaskingDataType maskingDataType = MaskingDataType.TYPE_TEXT;

        byte[] maskedData = provider.anon(sunfishHttpObject.getBody(), maskingDataType, anon.getConfigId());
        sunfishHttpObject.setBody(maskedData);


    }


}
