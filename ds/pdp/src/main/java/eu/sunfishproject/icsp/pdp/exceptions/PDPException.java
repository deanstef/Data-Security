package eu.sunfishproject.icsp.pdp.exceptions;

import javax.ws.rs.core.Response.Status;

import org.sunfish.icsp.common.exceptions.ICSPException;

public class PDPException extends ICSPException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public PDPException(Status status) {
    super(status);
  }

  public PDPException(String message, Status status) {
    super(message, status);
  }

  public PDPException(Throwable cause, Status status) {
    super(cause, status);
  }

  public PDPException(String message, Throwable cause, Status status) {
    super(message, cause, status);
  }

  public PDPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
      Status status) {
    super(message, cause, enableSuppression, writableStackTrace, status);
  }

}
