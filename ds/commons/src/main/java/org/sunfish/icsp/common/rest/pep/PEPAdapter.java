package org.sunfish.icsp.common.rest.pep;

import org.apache.openaz.xacml.api.Request;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.rest.model.SunfishSignature;
import org.sunfish.icsp.common.rest.pdp.AuthorizationResponse;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface PEPAdapter {

    public String getName();

    javax.ws.rs.core.Response request(String issuer, SunfishService service, SunfishRequest request, String requestParams,                                  SunfishRequestData requestData,
                                      SunfishSignature signature, byte[] body) throws ICSPException;

}
