package eu.sunfishproject.icsp.pap.exceptions;

import org.sunfish.icsp.common.exceptions.ICSPException;

import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PAPException extends ICSPException {


    private static final long serialVersionUID = 1L;

    public PAPException(Response.Status status) {
        super(status);
    }

    public PAPException(String message, Response.Status status) {
        super(message, status);
    }

    public PAPException(Throwable cause, Response.Status status) {
        super(cause, status);
    }

    public PAPException(String message, Throwable cause, Response.Status status) {
        super(message, cause, status);
    }

    public PAPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                        Response.Status status) {
        super(message, cause, enableSuppression, writableStackTrace, status);
    }

}