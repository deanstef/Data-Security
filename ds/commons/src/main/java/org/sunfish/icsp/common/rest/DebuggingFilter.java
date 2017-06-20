/**
 *
 */
package org.sunfish.icsp.common.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
public class DebuggingFilter implements ContainerResponseFilter, ContainerRequestFilter {

  private static final Logger log = LogManager.getLogger(DebuggingFilter.class);

  /*
   * (non-Javadoc)
   *
   * @see
   * javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.
   * ContainerRequestContext)
   */
  @Override
  public void filter(final ContainerRequestContext requestContext) throws IOException {
    log.debug("Intercepting REQ  " + requestContext.getUriInfo().getPath(true));

    final StringBuilder sb = new StringBuilder();
//    sb.append("User: ").append(requestContext.getSecurityContext().getUserPrincipal() == null ? "unknown" : requestContext.getSecurityContext().getUserPrincipal());
    sb.append(" - Path: ").append(requestContext.getUriInfo().getPath());
    sb.append(" - Header: ").append(requestContext.getHeaders());
//    sb.append(" - Entity: ").append(getEntityBody(requestContext));
    log.debug("HTTP REQUEST : " + sb.toString());

  }

  /*
   * (non-Javadoc)
   *
   * @see
   * javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container.
   * ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
   */
  @Override
  public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
      throws IOException {
    log.debug("Intercepting RESP " + requestContext.getUriInfo().getPath(true));
    final StringBuilder sb = new StringBuilder();
    sb.append("Header: ").append(responseContext.getHeaders());
    sb.append(" - Entity: ").append(responseContext.getEntity());
    log.debug("HTTP RESPONSE : " + sb.toString());
  }

  private String getEntityBody(final ContainerRequestContext requestContext)
  {
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final InputStream in = requestContext.getEntityStream();

      final StringBuilder b = new StringBuilder();
      try
      {
          ReaderWriter.writeTo(in, out);

          final byte[] requestEntity = out.toByteArray();
          if (requestEntity.length == 0)
          {
              b.append("").append("\n");
          }
          else
          {
              b.append(new String(requestEntity)).append("\n");
          }
          requestContext.setEntityStream( new ByteArrayInputStream(requestEntity) );

      } catch (final IOException ex) {
          //Handle logging error
      }
      return b.toString();
  }
}
