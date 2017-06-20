package org.sunfish.icsp.common.rest.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.policy.dom.DOMAttributeDesignator;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.dom.DOMProperties;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.apache.openaz.xacml.std.dom.DOMUtil;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorSet;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLSunfishAttributeDesignatorSetProvider
    implements MessageBodyReader<AttributeDesignatorSet>, MessageBodyWriter<AttributeDesignatorSet> {

  private static final Logger log = LogManager.getLogger(XACMLSunfishAttributeDesignatorSetProvider.class);

  @Override
  public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    if (!mediaType.equals(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)) {
      return false;
    }
    final boolean assignableFrom = AttributeDesignatorSet.class.isAssignableFrom(type);
    return assignableFrom;
  }

  @Override
  public AttributeDesignatorSet readFrom(final Class<AttributeDesignatorSet> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType,
      final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
      throws IOException, WebApplicationException {

    try {
      final AttributeDesignatorSet attributeDesignatorSet = loadFrom(entityStream);
      return attributeDesignatorSet;
    } catch (final DOMStructureException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
  }

  @Override
  public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    if (!mediaType.equals(ExtendedMediaType.APPLICATION_XML_XACML_TYPE)) {
      return false;
    }
    return AttributeDesignatorSet.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(final AttributeDesignatorSet attributeDesignatorSet, final Class<?> type,
      final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(final AttributeDesignatorSet attributeDesignatorSet, final Class<?> type,
      final Type genericType, final Annotation[] annotations, final MediaType mediaType,
      final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
      throws IOException, WebApplicationException {
    writeTo(attributeDesignatorSet, entityStream, false);
  }

  public static void writeTo(final AttributeDesignatorSet attributeDesignatorSet, final OutputStream out,
      final boolean pp) throws IOException {

    log.debug("This MessageBodyWriter has not been tested!");
    final OutputStreamWriter osw = new OutputStreamWriter(out, Charset.forName("UTF-8"));

    osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    if (pp) {
      osw.write("\n");
    }

    // root

    osw.write("<AttributeDesignatorSet ");
    osw.write(" xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"");
    osw.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    osw.write(" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
    osw.write(" http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"");
    osw.write(">");

    if (pp) {
      osw.write("\n    ");
    }

    osw.flush();

    for (final AttributeDesignator attributeDesignator : attributeDesignatorSet) {
      try {
        convert(attributeDesignator, out, pp);
      } catch (final DOMStructureException e) {
        log.error("Could not convert Attribute Designator:", e);
        throw new IOException(e);
      }
    }

    if (pp) {
      osw.write("\n");
    }
    osw.write("</AttributeDesignatorSet>");
    osw.flush();

  }

  public static AttributeDesignatorSet loadFrom(final InputStream is) throws DOMStructureException {

    AttributeDesignatorSet attributeDesignatorSet = null;

    try {
      final Document ex = DOMUtil.loadDocument(is);
      if (ex == null) {
        throw new DOMStructureException("Null document returned");
      }
      final Element rootNode = DOMUtil.getFirstChildElement(ex);
      if (rootNode == null) {
        throw new DOMStructureException("No child in document");
      } else if (DOMUtil.isInNamespace(rootNode, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
        if ("AttributeDesignatorSet".equals(rootNode.getLocalName())) {
          attributeDesignatorSet = newInstance(rootNode);
          if (attributeDesignatorSet == null) {
            throw new DOMStructureException("Failed to parse Request");
          }
          return attributeDesignatorSet;
        }
        throw DOMUtil.newUnexpectedElementException(rootNode);
      } else {
        throw DOMUtil.newUnexpectedElementException(rootNode);
      }
    } catch (final Exception var4) {
      var4.printStackTrace();
      throw new DOMStructureException("Exception loading Request: " + var4.getMessage(), var4);
    }

  }

  public static AttributeDesignatorSet newInstance(final Node nodePIPRequest) throws DOMStructureException {
    {
      final Element elementRequest = DOMUtil.getElement(nodePIPRequest);
      final boolean bLenient = DOMProperties.isLenient();

      final AttributeDesignatorSet attributeDesignators = new AttributeDesignatorSet();

      final NodeList children = elementRequest.getChildNodes();

      int numChildren;
      if (children != null && (numChildren = children.getLength()) > 0) {
        for (int i = 0; i < numChildren; ++i) {
          final Node child = children.item(i);
          if (DOMUtil.isElement(child)) {
            if (DOMUtil.isInNamespace(child, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
              if (DOMUtil.isInNamespace(child, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
                if ("AttributeDesignator".equals(child.getLocalName())) {
                  attributeDesignators.add(DOMAttributeDesignator.newInstance(child));
                } else if (!bLenient) {
                  throw DOMUtil.newUnexpectedElementException(child, nodePIPRequest);
                }
              } else if (!bLenient) {
                throw DOMUtil.newUnexpectedElementException(child, nodePIPRequest);
              }
            } else if (!bLenient) {
              throw DOMUtil.newUnexpectedElementException(child, nodePIPRequest);
            }
          }
        }
      }

      return attributeDesignators;

    }
  }

  public static void convert(final AttributeDesignator attributeDesignator, final OutputStream outputStream,
      final boolean prettyPrint) throws IOException, DOMStructureException {

    final OutputStreamWriter osw = new OutputStreamWriter(outputStream);
    final StringBuilder sb = new StringBuilder();

    // response with attributes
    sb.append("<AttributeDesignator");

    // Currently this is hard-coded for just the standard XACML namespaces, but
    // ideally should use
    // Namespaces from incoming Request to get non-standard ones.
    sb.append(" xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"");
    sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    sb.append(" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
    sb.append(" http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"");
    sb.append(" Category=\"" + attributeDesignator.getCategory().stringValue() + "\"");
    sb.append(" AttributeId=\"" + attributeDesignator.getAttributeId().stringValue() + "\"");
    sb.append(" DataType=\"" + attributeDesignator.getDataTypeId().stringValue() + "\"");
    if (attributeDesignator.getIssuer() != null) {
      sb.append(" Issuer=\"" + attributeDesignator.getIssuer() + "\"");
    }
    sb.append(" MustBePresent=\""
        + (attributeDesignator.getMustBePresent() == null ? false : attributeDesignator.getMustBePresent())
        + "\"");

    sb.append("/>");

    if (prettyPrint) {
      sb.append("\n");
    }

    // all done

    osw.write(sb.toString());

    // force output
    osw.flush();

  }
}
