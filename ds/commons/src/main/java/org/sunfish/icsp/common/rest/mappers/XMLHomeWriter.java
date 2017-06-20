package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.sunfish.icsp.common.rest.ExtendedMediaType;

public class XMLHomeWriter implements MessageBodyWriter<String> {

  @Override
  public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    return ExtendedMediaType.APPLICATION_HOME_XML_TYPE.equals(mediaType);
  }

  @Override
  public long getSize(final String t, final Class<?> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(final String t, final Class<?> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType,
      final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
      throws IOException, WebApplicationException {
    entityStream.write(t.getBytes("UTF-8"));
  }
}