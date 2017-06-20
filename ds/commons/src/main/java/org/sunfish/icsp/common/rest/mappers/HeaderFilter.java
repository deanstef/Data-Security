/**
 * 
 */
package org.sunfish.icsp.common.rest.mappers;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;

import org.sunfish.icsp.common.xacml.AddHeader;
import org.sunfish.icsp.common.xacml.Status;

@Provider
public class HeaderFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
    if (containerResponseContext.getStatus() == 200 || containerResponseContext.getStatus() == 204) {
      for (Annotation annotation : containerResponseContext.getEntityAnnotations()) {
        if(annotation instanceof AddHeader){
        	AddHeader header = (AddHeader) annotation;
        	containerResponseContext.getHeaders().add(header.key(),header.value());
        }
      }
    }
  }
}