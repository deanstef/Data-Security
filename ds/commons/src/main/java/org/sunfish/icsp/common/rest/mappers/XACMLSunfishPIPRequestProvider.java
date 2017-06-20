package org.sunfish.icsp.common.rest.mappers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.policy.dom.DOMAttributeDesignator;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.dom.*;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Provider
@Consumes(ExtendedMediaType.APPLICATION_XML_XACML)
public class XACMLSunfishPIPRequestProvider implements MessageBodyReader<SunfishPIPRequest>, MessageBodyWriter<SunfishPIPRequest> {

    private static final Logger log = LogManager.getLogger(XACMLSunfishPIPRequestProvider.class);



    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        final boolean assignableFrom = SunfishPIPRequest.class.isAssignableFrom(type);
        return assignableFrom;
    }

    @Override
    public SunfishPIPRequest readFrom(final Class<SunfishPIPRequest> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException, WebApplicationException {

        try {
            final SunfishPIPRequest sunfishPIPRequest = loadFrom(entityStream);
            return sunfishPIPRequest;
        } catch (final DOMStructureException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return SunfishPIPRequest.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(final SunfishPIPRequest sunfishPIPRequest, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final SunfishPIPRequest sunfishPIPRequest, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        writeTo(sunfishPIPRequest, entityStream, false);
    }


    public static void writeTo(final SunfishPIPRequest sunfishPIPRequest, final OutputStream out, final boolean pp) throws IOException {


        log.debug("This MessageBodyWriter has not been tested!");
        final OutputStreamWriter osw = new OutputStreamWriter(out, Charset.forName("UTF-8"));

        osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        if (pp) {
            osw.write("\n");
        }

        // root

        osw.write("<PIPRequest ");
        osw.write(" xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"");
        osw.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        osw.write(" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17");
        osw.write(" http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\"");
        osw.write(">");

        if (pp) {
            osw.write("\n");
        }

        if(sunfishPIPRequest.getAttributeDesignators() != null) {
            final Collection<AttributeDesignator> attributeDesignators = sunfishPIPRequest.getAttributeDesignators();

            osw.write("<RequestedAttributes>");

            if (pp) {
                osw.write("\n    ");
            }

            osw.flush();

            for(final AttributeDesignator attributeDesignator : attributeDesignators) {
                try {
                    convert(attributeDesignator, out, pp);
                } catch (final DOMStructureException e) {
                    log.error("Could not convert Attribute Designator:", e);
                    throw new IOException(e);
                }
            }

            if (pp) {
                osw.write("\n  ");
            }
            osw.write("</RequestedAttributes>");


        }


        if(sunfishPIPRequest.getRequest() != null) {
            osw.flush();
            XACMLRequestProvider.writeTo(sunfishPIPRequest.getRequest(), out, pp, true);
        }

        osw.write("</PIPRequest>");
        osw.flush();


    }


    public static SunfishPIPRequest loadFrom(final InputStream is) throws DOMStructureException  {


        SunfishPIPRequest sunfishPIPRequest = null;

        try {
            final Document ex = DOMUtil.loadDocument(is);
            if(ex == null) {
                throw new DOMStructureException("Null document returned");
            } else {
                final Element rootNode = DOMUtil.getFirstChildElement(ex);
                if(rootNode == null) {
                    throw new DOMStructureException("No child in document");
                } else if(DOMUtil.isInNamespace(rootNode, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
                    if("PIPRequest".equals(rootNode.getLocalName())) {
                        sunfishPIPRequest = newInstance(rootNode);
                        if(sunfishPIPRequest == null) {
                            throw new DOMStructureException("Failed to parse Request");
                        } else {
                            return sunfishPIPRequest;
                        }
                    } else {
                        throw DOMUtil.newUnexpectedElementException(rootNode);
                    }
                } else {
                    throw DOMUtil.newUnexpectedElementException(rootNode);
                }
            }
        } catch (final Exception var4) {
            throw new DOMStructureException("Exception loading Request: " + var4.getMessage(), var4);
        }

    }


    public static SunfishPIPRequest newInstance(final Node nodePIPRequest) throws DOMStructureException {
        {
            final Element elementRequest = DOMUtil.getElement(nodePIPRequest);
            final boolean bLenient = DOMProperties.isLenient();


            final SunfishPIPRequest sunfishPIPRequest = new SunfishPIPRequest();


            final NodeList children = elementRequest.getChildNodes();
            boolean sawRequest = false;
            int numChildren;
            if (children != null && (numChildren = children.getLength()) > 0) {
                for (int i = 0; i < numChildren; ++i) {
                    final Node child = children.item(i);
                    if (DOMUtil.isElement(child)) {
                        if (DOMUtil.isInNamespace(child, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
                            final String childName = child.getLocalName();

                            if ("Request".equals(childName)) {
                                sunfishPIPRequest.setRequest(DOMRequest.newInstance(child));
                                sawRequest = true;
                            } else if("RequestedAttributes".equals(childName)) {
                                final NodeList grandchildren = child.getChildNodes();
                                int numGrandchildren;
                                if(grandchildren != null && (numGrandchildren = grandchildren.getLength()) > 0) {
                                    for(int j = 0; j < numGrandchildren; ++j) {
                                        final Node grandchild = grandchildren.item(j);
                                        if(DOMUtil.isElement(grandchild)) {
                                            if(DOMUtil.isInNamespace(grandchild, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
                                                if("AttributeDesignator".equals(grandchild.getLocalName())) {
                                                    sunfishPIPRequest.add(DOMAttributeDesignator.newInstance(grandchild));
                                                } else if(!bLenient) {
                                                    throw DOMUtil.newUnexpectedElementException(grandchild, nodePIPRequest);
                                                }
                                            } else if(!bLenient) {
                                                throw DOMUtil.newUnexpectedElementException(grandchild, nodePIPRequest);
                                            }
                                        }
                                    }
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

            if (!sawRequest && !bLenient) {
                throw DOMUtil.newMissingElementException(nodePIPRequest, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17", "Request");
            } else {
                return sunfishPIPRequest;
            }
        }
    }

    public static void convert(final AttributeDesignator attributeDesignator, final OutputStream outputStream, final boolean prettyPrint) throws IOException, DOMStructureException {


        final OutputStreamWriter osw = new OutputStreamWriter(outputStream);
        final StringBuilder sb = new StringBuilder();

        // response with attributes
        sb.append("<AttributeDesignator");

        // Currently this is hard-coded for just the standard XACML namespaces, but ideally should use
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
