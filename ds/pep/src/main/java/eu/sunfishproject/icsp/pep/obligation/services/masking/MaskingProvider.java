package eu.sunfishproject.icsp.pep.obligation.services.masking;


import org.sunfish.icsp.common.exceptions.ICSPException;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface MaskingProvider {


    public byte[] mask(byte[] content, MaskingDataType dataType, String policyId, String maskingContext) throws ICSPException;

    public byte[] unmask(byte[] content, MaskingDataType dataType, String policyId, String maskingContext) throws ICSPException;


}
