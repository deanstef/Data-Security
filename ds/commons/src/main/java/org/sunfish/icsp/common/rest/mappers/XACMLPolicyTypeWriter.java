package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.util.XACMLIO.GenericWriterPool;
import org.sunfish.icsp.common.util.XACMLIO.XACMLWriter;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObjectFactory;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

@Provider
@Produces(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPolicyTypeWriter implements MessageBodyWriter<PolicyType> {

  private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();
  private static GenericWriterPool<PolicyType> pool;
  static {
    try {
      pool = new GenericWriterPool<>(CommonSetup.POOL_SZ, false, PolicyType.class);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }

  }

  @Override
  public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
    return PolicyType.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(final PolicyType t, final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(final PolicyType t, final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
      throws IOException, WebApplicationException {
    try {
      final XACMLWriter<PolicyType> writer = pool.acquire();
      writer.write(entityStream,  OBJECT_FACTORY.createPolicy(t));
      pool.recycle(writer);
    } catch (final Exception e) {
      throw new IOException(e);
    }
  }
}