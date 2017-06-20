package org.sunfish.icsp.common.exceptions;

import javax.ws.rs.core.Response.Status;

public abstract class ICSPException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private final Status      status;

  public ICSPException(Status status) {
    this.status = status;
  }

  public ICSPException(String message, Status status) {
    super(message);
    this.status = status;
  }

  public ICSPException(Throwable cause, Status status) {
    super(cause);
    this.status = status;
  }

  public ICSPException(String message, Throwable cause, Status status) {
    super(message, cause);
    this.status = status;
  }

  public ICSPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
      Status status) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

}
