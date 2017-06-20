package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.Response;
import org.apache.openaz.xacml.std.dom.DOMRequest;
import org.apache.openaz.xacml.std.dom.DOMResponse;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.sunfish.icsp.common.rest.ExtendedMediaType;

@Provider
@Produces(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLResponseProvider implements MessageBodyWriter<Response>, MessageBodyReader<Response> {

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Response.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(Response t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Response t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException, WebApplicationException {

    try {
      DOMResponse.convert((Response) t, entityStream);
    } catch (DOMStructureException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    boolean assignableFrom = Response.class.isAssignableFrom(type);
    return assignableFrom;
  }

  @Override
  public Response readFrom(Class<Response> type, Type genericType, Annotation[] annotations,
                          MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
          throws IOException, WebApplicationException {

    try {
      Response response = DOMResponse.load(entityStream);
      return response;
    } catch (DOMStructureException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
  }

}