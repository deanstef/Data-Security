package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.util.XACMLIO.PolicyReader;
import org.sunfish.icsp.common.util.XACMLIO.PolicyReaderPool;

@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPolicyListPolicySetListTypeReader implements MessageBodyReader<List<PolicyDef>> {
  private static PolicyReaderPool pool;

  static {
    try {
      pool = new PolicyReaderPool(CommonSetup.POOL_SZ, false);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    return List.class.isAssignableFrom(type);
  }

  @Override
  public List<PolicyDef> readFrom(final Class<List<PolicyDef>> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType,
      final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
      throws IOException, WebApplicationException {
    try {
      final PolicyReader reader = pool.acquire();
      final List<PolicyDef> def = reader.readDefList(entityStream);
      pool.recycle(reader);
      return def;
    } catch (final Exception e) {
      e.printStackTrace();
      throw new IOException(e);
    }
  }

}