package eu.sunfishproject.icsp.proxy.pep;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class ProxyFilter implements ContainerResponseFilter {

  @Override
  public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
      throws IOException {
   responseContext.getEntityStream();

  }

}
