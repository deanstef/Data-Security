package eu.sunfishproject.icsp.pep.obligation.services;

import eu.sunfishproject.icsp.pep.config.PEPConfig;
import eu.sunfishproject.icsp.pep.obligation.ObligationService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Obligation;
import org.sunfish.icsp.common.exceptions.ObligationException;
import org.sunfish.icsp.common.rest.model.SunfishHttpObject;
import org.sunfish.icsp.common.xacml.XACMLConstants;

import java.io.InputStream;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class AuthenticationObligationService implements ObligationService {

    private static final Logger log = LogManager.getLogger(MaskingObligationService.class);



    @Override
    public boolean canHandleObligation(final String obligationId) {
        return XACMLConstants.OBLIGATION_ID_AUTHENTICATE.equals(obligationId);
    }


    @Override
    public void handleObligation(Obligation obligation, SunfishHttpObject sunfishHttpObject, Object... arguments) throws ObligationException {

        InputStream inputStream = AuthenticationObligationService.class.getClassLoader().getResourceAsStream("oa.html");

        try {
            byte[] content = IOUtils.toByteArray(inputStream);

            String serviceUrl = PEPConfig.getInstance().getOAServiceUrl();
            String demoUrl = PEPConfig.getInstance().getDemoServiceUrl();

            String contentString = new String(content, "UTF-8").replace("{oa.service.url}", serviceUrl).replace("{demo.service.url}", demoUrl);

            sunfishHttpObject.setBody(contentString.getBytes("UTF-8"));
            sunfishHttpObject.setResponseCode(200);


        } catch(Exception e) {

        } finally {
            try {
                inputStream.close();
            } catch(Exception e) {

            }
        }


    }


}