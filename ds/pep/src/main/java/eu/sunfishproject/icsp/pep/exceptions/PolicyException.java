package eu.sunfishproject.icsp.pep.exceptions;

import org.sunfish.icsp.common.exceptions.ICSPException;

import javax.ws.rs.core.Response;

/**
 * Created by dziegler on 17/10/2016.
 */
public class PolicyException extends ICSPException {


    private static final long serialVersionUID = 1L;

    public PolicyException(Response.Status status) {
        super(status);
    }

    public PolicyException(String message, Response.Status status) {
        super(message, status);
    }

    public PolicyException(Throwable cause, Response.Status status) {
        super(cause, status);
    }

    public PolicyException(String message, Throwable cause, Response.Status status) {
        super(message, cause, status);
    }

    public PolicyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                        Response.Status status) {
        super(message, cause, enableSuppression, writableStackTrace, status);
    }
}
