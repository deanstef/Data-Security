/**
 * 
 */
package eu.sunfishproject.icsp.prp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;
import org.glassfish.jersey.server.ContainerRequest;
import org.sunfish.icsp.common.rest.SunfishServices;

/**
 * @author amarsalek
 *
 */
public class AuthenticationtFilter implements ContainerRequestFilter {

	private static final Logger log = LogManager.getLogger(AuthenticationtFilter.class);
	@Context
	private ResourceInfo resourceInfo;
	private static final Response ACCESS_DENIED = Response.status(Response.Status.FORBIDDEN).entity("You cannot access this resource").build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).entity("Access blocked for all users !!").build();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		String path = requestContext.getUriInfo().getPath(false);

		if(path.endsWith("swagger.json")) {
			return;
		}

		Method method = resourceInfo.getResourceMethod();
		//Access allowed for all
		if( ! method.isAnnotationPresent(PermitAll.class))
		{
			//Access denied for all
			if(method.isAnnotationPresent(DenyAll.class))
			{
				requestContext.abortWith(ACCESS_FORBIDDEN);
				return;
			}

			String issuer = requestContext.getHeaderString(SunfishServices.HEADER_ISSUER);

			if(issuer == null)
			{
				requestContext.abortWith(ACCESS_DENIED);
				return;
			}
			//TODO check auth info
			//FIXME
			//Verify user access
			if(method.isAnnotationPresent(RolesAllowed.class))
			{
				if(!issuer.equals("Admin"))
				{
					requestContext.abortWith(ACCESS_DENIED);
					return;
				}
			}
		}
	}

}
