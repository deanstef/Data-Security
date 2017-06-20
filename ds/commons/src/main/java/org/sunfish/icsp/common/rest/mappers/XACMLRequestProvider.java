package org.sunfish.icsp.common.rest.mappers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.security.auth.x500.X500Principal;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.api.RequestAttributes;
import org.apache.openaz.xacml.api.RequestAttributesReference;
import org.apache.openaz.xacml.api.RequestReference;
import org.apache.openaz.xacml.api.SemanticString;
import org.apache.openaz.xacml.std.datatypes.ExtendedNamespaceContext;
import org.apache.openaz.xacml.std.datatypes.XPathExpressionWrapper;
import org.apache.openaz.xacml.std.dom.DOMRequest;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.w3c.dom.Node;

@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
@Produces(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLRequestProvider implements MessageBodyReader<Request>, MessageBodyWriter<Request> {

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Request.class.isAssignableFrom(type);
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    boolean assignableFrom = Request.class.isAssignableFrom(type);
    return assignableFrom;
  }

  @Override
  public long getSize(Request t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(Request t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException, WebApplicationException {
    writeTo(t, entityStream, true);

  }

  @Override
  public Request readFrom(Class<Request> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
      throws IOException, WebApplicationException {

    try {
      Request req = DOMRequest.load(entityStream);
      return req;
    } catch (DOMStructureException e) {
      e.printStackTrace();
      throw new IOException(e);
    }
  }

  public static void writeTo(Request req, OutputStream out, boolean pp) throws IOException {
    writeTo(req, out, pp, false);
  }

  public static String stringify(Request request) throws IOException {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    writeTo(request, bOut, false, true);
    return bOut.toString("UTF-8");
  }

  public static void writeTo(Request req, OutputStream out, boolean pp, boolean omitHeader)
      throws IOException {

    LogManager.getLogger(XACMLRequestProvider.class).debug("This MessageBodyWriter has not been tested!");
    OutputStreamWriter osw = new OutputStreamWriter(out, Charset.forName("UTF-8"));
    // Header

    if (!omitHeader) {
      osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      if (pp)
        osw.write("\n");
    }

    // root

    osw.write("<Request ");
    osw.write(" xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"");
    osw.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    osw.write(" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
    osw.write(" http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"");
    osw.write(" ReturnPolicyIdList=\"");
    osw.write(Boolean.toString(req.getReturnPolicyIdList()));
    osw.write("\"");
    osw.write(" CombinedDecision=\"");
    osw.write(Boolean.toString(req.getCombinedDecision()));
    osw.write("\"");

    osw.write(">");

    if (pp)
      osw.write("\n  ");
    if (req.getRequestDefaults() != null) {
      osw.write("<RequestDefaults>");

      if (pp)
        osw.write("\n    ");
      osw.write("<XPathVersion>");
      osw.write(req.getRequestDefaults().getXPathVersion().toString());
      osw.write("</XPathVersion>");

      if (pp)
        osw.write("\n  ");
      osw.write("</RequestDefaults>");
    }
    Iterator<RequestAttributes> attrIter = req.getRequestAttributes().iterator();
    while (attrIter.hasNext()) {
      RequestAttributes attrs = attrIter.next();
      if (pp)
        osw.write("\n  ");
      osw.write("<Attributes Category=\"");
      osw.write(attrs.getCategory().getUri().toString());
      osw.write("\"");
      if (attrs.getXmlId() != null && !attrs.getXmlId().trim().isEmpty()) {
        osw.write(" id=\"");
        osw.write(attrs.getXmlId());
        osw.write("\"");
      }
      osw.write(">");

      Node contentRoot = attrs.getContentRoot();
      if (contentRoot != null) {
        if (pp)
          osw.write("\n    ");
        try {
          Transformer transformer = TransformerFactory.newInstance().newTransformer();
          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
          if (pp)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
          transformer.transform(new DOMSource(contentRoot), new StreamResult(osw));
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
          throw new IOException(e);
        }
      }
      if (!attrs.getAttributes().isEmpty()) {
        for (org.apache.openaz.xacml.api.Attribute att : attrs.getAttributes()) {
          if (pp)
            osw.write("\n    ");
          osw.write("<Attribute AttributeId=\"");
          osw.write(att.getAttributeId().getUri().toString());
          osw.write("\" IncludeInResult=\"");
          osw.write(Boolean.toString(att.getIncludeInResults()));
          osw.write("\"");
          if (att.getIssuer() != null && !att.getIssuer().trim().isEmpty()) {
            osw.write("  Issuer=\"");
            StringEscapeUtils.escapeXml(osw, att.getIssuer());
            osw.write("\"");
          }
          osw.write(">");
          for (AttributeValue<?> value : att.getValues()) {
            if (pp)
              osw.write("\n      ");
            try {
              osw.write("<AttributeValue" + getNamespaces(value.getValue()));
              osw.write(" DataType=\"");
              osw.write(value.getDataTypeId().getUri().toString());
              osw.write("\">");
              osw.write(outputValueValue(value.getValue()) + "</AttributeValue>");
            } catch (DOMStructureException e) {
              throw new IOException(e);
            }
          }
          if (pp) {
            osw.write("\n    ");
          }
          osw.write("</Attribute>");
        }
      }
      if(pp) {
        osw.write("\n  ");
      }
      osw.write("</Attributes>");
    }
    if (!req.getMultiRequests().isEmpty()) {
      if (pp)
        osw.write("\n  ");
      osw.write("<MultiRequests>");
      for (RequestReference ref : req.getMultiRequests()) {
        if (pp)
          osw.write("\n    ");
        osw.write("<RequestReference>");
        for (RequestAttributesReference attref : ref.getAttributesReferences()) {
          if (pp)
            osw.write("\n      ");
          osw.write("<AttributesReference ReferenceId=\"");
          osw.write(attref.getReferenceId());
          osw.write("\"/>");
        }
        if (pp)
          osw.write("\n    ");
        osw.write("</RequestReference>");
      }
      if (pp)
        osw.write("\n  ");
      osw.write("</MultiRequests>");
    }
    if(pp) {
      osw.write("\n");
    }
    // end root
    osw.write("</Request>");
    osw.flush();

  }

  private static String getNamespaces(Object valueObject) {
    String returnString = "";
    if (!(valueObject instanceof XPathExpressionWrapper)) {
      // value is not XPathExpression, so has no Namespace info in it
      return returnString;
    }
    XPathExpressionWrapper xw = (XPathExpressionWrapper) valueObject;

    ExtendedNamespaceContext namespaceContext = xw.getNamespaceContext();
    if (namespaceContext != null) {
      // get the list of all namespace prefixes
      Iterator<String> prefixIt = namespaceContext.getAllPrefixes();
      while (prefixIt.hasNext()) {
        String prefix = prefixIt.next();
        String namespaceURI = namespaceContext.getNamespaceURI(prefix);
        if (prefix == null || prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
          returnString += " xmlns=\"" + namespaceURI + "\"";
        } else {
          returnString += " xmlns:" + prefix + "=\"" + namespaceURI + "\"";
        }
      }

    }
    return returnString;
  }

  private static String outputValueValue(Object obj) throws DOMStructureException {
    if (obj instanceof String || obj instanceof Boolean || obj instanceof Integer
        || obj instanceof BigInteger) {
      return StringEscapeUtils.escapeXml(obj.toString());
    } else if (obj instanceof Double) {
      Double d = (Double) obj;
      if (d == Double.NaN) {
        return "NaN";
      } else if (d == Double.POSITIVE_INFINITY) {
        return "INF";
      } else if (d == Double.NEGATIVE_INFINITY) {
        return "-INF";
      }
      return obj.toString();
    } else if (obj instanceof SemanticString) {
      return ((SemanticString) obj).stringValue();
    } else if (obj instanceof X500Principal || obj instanceof URI) {
      // something is very weird with X500Principal data type. If left on its
      // own the output is a map
      // that includes encoding.
      return obj.toString();
    } else if (obj instanceof XPathExpressionWrapper) {

      XPathExpressionWrapper xw = (XPathExpressionWrapper) obj;

      return xw.getPath();
    } else {
      throw new DOMStructureException("Unhandled data type='" + obj.getClass().getName() + "'");
    }
  }

}