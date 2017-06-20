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
import org.sunfish.icsp.common.xacml.generated.PolicySetListType;


@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPolicySetListTypeReader implements MessageBodyReader<PolicySetListType> {

  private static GenericReaderPool<PolicySetListType> pool;
  static {
    try {
      pool = new GenericReaderPool<>(CommonSetup.POOL_SZ, false, PolicySetListType.class);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

	private static final Logger log = LogManager.getLogger(XACMLPolicySetListTypeReader.class);

	@Override
	public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return PolicySetListType.class.isAssignableFrom(type);
	}

	@Override
	public PolicySetListType readFrom(final Class<PolicySetListType> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
					throws IOException, WebApplicationException {
	  XACMLReader<PolicySetListType> reader;
    try {
      reader = pool.acquire();
      final JAXBElement<PolicySetListType> policiesElement = reader.read(entityStream);
      pool.recycle(reader);
      return policiesElement.getValue();
    } catch (final Exception e) {
      throw new IOException(e);
    }
	}

}