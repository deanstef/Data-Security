package eu.sunfishproject.icsp.pep.obligation;

import org.apache.openaz.xacml.api.Obligation;
import org.sunfish.icsp.common.exceptions.ObligationException;
import org.sunfish.icsp.common.rest.model.SunfishHttpObject;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface ObligationService {

    public boolean canHandleObligation(String obligationId);

    public void handleObligation(Obligation obligation, SunfishHttpObject sunfishHttpObject, Object... arguments) throws ObligationException;

}
