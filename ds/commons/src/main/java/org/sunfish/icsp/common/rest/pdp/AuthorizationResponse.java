package org.sunfish.icsp.common.rest.pdp;

import org.apache.openaz.xacml.api.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class AuthorizationResponse {

    private String signature;
    private Response response;

    public AuthorizationResponse(String signature, Response response) {
        this.signature = signature;
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public String getSignature() {
        return signature;
    }
}
