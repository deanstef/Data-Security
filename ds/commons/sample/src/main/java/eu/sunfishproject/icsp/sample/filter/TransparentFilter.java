/**
 * 
 */
package eu.sunfishproject.icsp.sample.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
public class TransparentFilter implements ContainerResponseFilter, ContainerRequestFilter {

  private static final Logger log = LogManager.getLogger(TransparentFilter.class);

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.
   * ContainerRequestContext)
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    log.debug("Intercepting REQ  " + requestContext.getUriInfo().getPath(true));

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container.
   * ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
   */
  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    log.debug("Intercepting RESP " + requestContext.getUriInfo().getPath(true));

  }

}
