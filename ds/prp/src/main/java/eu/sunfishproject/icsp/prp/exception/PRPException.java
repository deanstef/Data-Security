package eu.sunfishproject.icsp.prp.exception;

import org.sunfish.icsp.common.exceptions.ICSPException;

import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PRPException extends ICSPException {


    private static final long serialVersionUID = 1L;

    public PRPException(Response.Status status) {
        super(status);
    }

    public PRPException(String message, Response.Status status) {
        super(message, status);
    }

    public PRPException(Throwable cause, Response.Status status) {
        super(cause, status);
    }

    public PRPException(String message, Throwable cause, Response.Status status) {
        super(message, cause, status);
    }

    public PRPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                        Response.Status status) {
        super(message, cause, enableSuppression, writableStackTrace, status);
    }

}