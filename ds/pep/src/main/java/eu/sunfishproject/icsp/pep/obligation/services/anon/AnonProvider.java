package eu.sunfishproject.icsp.pep.obligation.services.anon;


import eu.sunfishproject.icsp.pep.obligation.services.masking.MaskingDataType;
import org.sunfish.icsp.common.exceptions.ICSPException;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface AnonProvider {


    public byte[] anon(byte[] content, MaskingDataType dataType, String configId) throws ICSPException;


}
