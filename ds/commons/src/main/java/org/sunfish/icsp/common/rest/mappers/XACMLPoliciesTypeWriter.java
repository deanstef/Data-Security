package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
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
import org.sunfish.icsp.common.xacml.generated.PoliciesType;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
@Produces(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLPoliciesTypeWriter implements MessageBodyWriter<PoliciesType> {
  private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();
  private static GenericWriterPool<PoliciesType> pool;
  static {
    try {
      pool = new GenericWriterPool<>(CommonSetup.POOL_SZ, false, PoliciesType.class);
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }
	private static final Logger log = LogManager.getLogger(XACMLPoliciesTypeWriter.class);
	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return PolicyType.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(final PoliciesType t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(final PoliciesType t, final Class<?> type, final Type genericType, final Annotation[] annotations,
			final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
					throws IOException, WebApplicationException {
		writePoliciesFile(entityStream, t);
	}

	public void writePoliciesFile(final OutputStream os, final PoliciesType policies) throws IOException {
		try {
      final XACMLWriter<PoliciesType> writer = pool.acquire();
      writer.write(os, OBJECT_FACTORY.createPolicies(policies));
      pool.recycle(writer);
    } catch (final Exception e) {
      throw new IOException(e);
    }
	}
}