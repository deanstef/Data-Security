package eu.sunfishproject.icsp.pep.obligation;

import eu.sunfishproject.icsp.pep.obligation.services.AuthenticationObligationService;
import eu.sunfishproject.icsp.pep.obligation.services.MaskingObligationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class ObligationServiceStore {


    private final List<ObligationService> obligationServices;
    private static ObligationServiceStore instance;
    private Logger log = LogManager.getLogger(ObligationServiceStore.class);


    private ObligationServiceStore() {

        obligationServices = new ArrayList<>();
        obligationServices.add(new MaskingObligationService());
        obligationServices.add(new AuthenticationObligationService());

    }

    public static ObligationServiceStore getInstance() {
        if (instance == null) {
            instance = new ObligationServiceStore();
        }
        return instance;
    }


    public ObligationService getObligationServiceForObligation(String obligationId) {

        for(ObligationService obligationService : this.obligationServices) {
            if(obligationService.canHandleObligation(obligationId)) {
                return obligationService;
            }
        }

        return null;

    }






}
