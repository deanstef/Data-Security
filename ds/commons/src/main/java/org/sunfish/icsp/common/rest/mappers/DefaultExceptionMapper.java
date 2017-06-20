/**
 * 
 */
package org.sunfish.icsp.common.rest.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.sunfish.icsp.common.exceptions.ICSPException;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {

  /**
   * Returns a HTTP 400 Bad Request for general exceptions containing the simple
   * name of the exception
   * class (to enable automatic parsing) and
   * the exception message itself.<br>
   * 
   * In case an ICSPException was thrown, the status of this exception is used.
   * 
   * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
   */
  @Override
  public Response toResponse(Exception ex) {
    ex.printStackTrace();

    if (ex instanceof ICSPException) {
      return Response.status(((ICSPException) ex).getStatus())
          .entity(ex.getClass().getSimpleName() + ":\n" + ex.getMessage()).type("text/plain").build();
    }

    return Response.status(400).entity(ex.getClass().getSimpleName() + ":\n" + ex.getMessage())
        .type("text/plain").build();
  }
}
