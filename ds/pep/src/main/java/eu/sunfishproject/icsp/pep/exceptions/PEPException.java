package eu.sunfishproject.icsp.pep.exceptions;

import javax.ws.rs.core.Response.Status;

import org.sunfish.icsp.common.exceptions.ICSPException;

public class PEPException extends ICSPException {


    private static final long serialVersionUID = 1L;

    public PEPException(Status status) {
        super(status);
    }

    public PEPException(String message, Status status) {
        super(message, status);
    }

    public PEPException(Throwable cause, Status status) {
        super(cause, status);
    }

    public PEPException(String message, Throwable cause, Status status) {
        super(message, cause, status);
    }

    public PEPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                        Status status) {
        super(message, cause, enableSuppression, writableStackTrace, status);
    }

}
