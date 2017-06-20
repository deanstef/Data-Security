package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.util.XACMLIO.GenericReaderPool;
import org.sunfish.icsp.common.util.XACMLIO.XACMLReader;
import org.sunfish.icsp.common.xacml.generated.PoliciesType;
import org.sunfish.icsp.common.xacml.generated.PolicySetsType;

@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPolicySetsTypeReader implements MessageBodyReader<PolicySetsType> {

  private static final Logger                      log = LogManager
      .getLogger(XACMLPolicySetsTypeReader.class);

  private static GenericReaderPool<PolicySetsType> pool;
  static {
    try {
      pool = new GenericReaderPool<>(CommonSetup.POOL_SZ, false, PolicySetsType.class);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }

  }

  @Override
  public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    return PoliciesType.class.isAssignableFrom(type);
  }

  @Override
  public PolicySetsType readFrom(final Class<PolicySetsType> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType,
      final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
      throws IOException, WebApplicationException {

    XACMLReader<PolicySetsType> reader;
    try {
      reader = pool.acquire();
      final JAXBElement<PolicySetsType> policiesElement = reader.read(entityStream);
      pool.recycle(reader);
      return policiesElement.getValue();
    } catch (final Exception e) {
      throw new IOException(e);
    }

  }

}