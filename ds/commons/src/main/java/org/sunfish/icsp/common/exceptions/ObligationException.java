package org.sunfish.icsp.common.exceptions;


import javax.ws.rs.core.Response;

/**
 * Created by dziegler on 17/10/2016.
 */
public class ObligationException extends ICSPException {


    public ObligationException() {
        super(Response.Status.INTERNAL_SERVER_ERROR);
    }

    public ObligationException(String string) {
        super(string, Response.Status.INTERNAL_SERVER_ERROR);
    }


}
