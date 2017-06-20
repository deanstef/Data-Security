package org.sunfish.icsp.common.exceptions;

import org.sunfish.icsp.common.exceptions.ICSPException;

import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class DeserializationException extends ICSPException {


    private static final long serialVersionUID = 1L;

    public DeserializationException(Response.Status status) {
        super(status);
    }

    public DeserializationException(String message, Response.Status status) {
        super(message, status);
    }

    public DeserializationException(Throwable cause, Response.Status status) {
        super(cause, status);
    }

    public DeserializationException(String message, Throwable cause, Response.Status status) {
        super(message, cause, status);
    }

    public DeserializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                        Response.Status status) {
        super(message, cause, enableSuppression, writableStackTrace, status);
    }

}