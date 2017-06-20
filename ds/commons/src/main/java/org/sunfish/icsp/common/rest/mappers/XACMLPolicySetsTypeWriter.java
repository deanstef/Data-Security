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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.util.XACMLIO.GenericWriterPool;
import org.sunfish.icsp.common.util.XACMLIO.XACMLWriter;
import org.sunfish.icsp.common.xacml.generated.ObjectFactory;
import org.sunfish.icsp.common.xacml.generated.PolicySetsType;

@Provider
@Produces(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPolicySetsTypeWriter implements MessageBodyWriter<PolicySetsType> {

  private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();
  private static GenericWriterPool<PolicySetsType> pool;
  static {
    try {
      pool = new GenericWriterPool<>(CommonSetup.POOL_SZ, false, PolicySetsType.class);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }
	private static final Logger log = LogManager.getLogger(XACMLPolicySetsTypeWriter.class);
	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return PolicySetsType.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(final PolicySetsType t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(final PolicySetsType t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
					throws IOException, WebApplicationException {
		writePolicyFile(entityStream, t);
	}

	public void writePolicyFile(final OutputStream os, final PolicySetsType policySet) throws IOException {
		 try {
	      final XACMLWriter<PolicySetsType> writer = pool.acquire();
	      writer.write(os, OBJECT_FACTORY.createPolicySets(policySet));
	      pool.recycle(writer);
	    } catch (final Exception e) {
	      throw new IOException(e);
	    }
	}
}