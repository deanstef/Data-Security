/*
 * Copyright 2003 Federal Chancellery Austria
 * MOA-ID has been developed in a cooperation between BRZ, the Federal
 * Chancellery Austria - ICT staff unit, and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */


package eu.sunfishproject.icsp.pep.obligation.services.oa.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.sunfishproject.icsp.pep.obligation.services.oa.Constants;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MOAEntityResolver;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MOAErrorHandler;
import eu.sunfishproject.icsp.pep.obligation.services.oa.utils.MiscUtil;
import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XMLGrammarPreparser;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Various utility functions for handling XML DOM trees.
 *
 * The parsing methods in this class make use of some features internal to the
 * Xerces DOM parser, mainly for performance reasons. As soon as JAXP
 * (currently at version 1.2) is better at schema handling, it should be used as
 * the parser interface.
 *
 * @author Patrick Peck
 * @version $Id$
 */
public class DOMUtils {

    /** Feature URI for namespace aware parsing. */
    private static final String NAMESPACES_FEATURE =
            "http://xml.org/sax/features/namespaces";
    /** Feature URI for validating parsing. */
    private static final String VALIDATION_FEATURE =
            "http://xml.org/sax/features/validation";
    /** Feature URI for schema validating parsing. */
    private static final String SCHEMA_VALIDATION_FEATURE =
            "http://apache.org/xml/features/validation/schema";
    /** Feature URI for normalization of element/attribute values. */
    private static final String NORMALIZED_VALUE_FEATURE =
            "http://apache.org/xml/features/validation/schema/normalized-value";
    /** Feature URI for parsing ignorable whitespace. */
    private static final String INCLUDE_IGNORABLE_WHITESPACE_FEATURE =
            "http://apache.org/xml/features/dom/include-ignorable-whitespace";
    /** Feature URI for creating EntityReference nodes in the DOM tree. */
    private static final String CREATE_ENTITY_REF_NODES_FEATURE =
            "http://apache.org/xml/features/dom/create-entity-ref-nodes";
    /** Property URI for providing external schema locations. */
    private static final String EXTERNAL_SCHEMA_LOCATION_PROPERTY =
            "http://apache.org/xml/properties/schema/external-schemaLocation";
    /** Property URI for providing the external schema location for elements
     * without a namespace. */
    private static final String EXTERNAL_NO_NAMESPACE_SCHEMA_LOCATION_PROPERTY =
            "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";

    private static final String EXTERNAL_GENERAL_ENTITIES_FEATURE =
            "http://xml.org/sax/features/external-general-entities";

    private static final String EXTERNAL_PARAMETER_ENTITIES_FEATURE =
            "http://xml.org/sax/features/external-parameter-entities";

    public static final String DISALLOW_DOCTYPE_FEATURE =
            "http://apache.org/xml/features/disallow-doctype-decl";



    /** Property URI for the Xerces grammar pool. */
    private static final String GRAMMAR_POOL =
            org.apache.xerces.impl.Constants.XERCES_PROPERTY_PREFIX
                    + org.apache.xerces.impl.Constants.XMLGRAMMAR_POOL_PROPERTY;
    /** A prime number for initializing the symbol table. */
    private static final int BIG_PRIME = 2039;
    /** Symbol table for the grammar pool. */
    private static SymbolTable symbolTable = new SymbolTable(BIG_PRIME);
    /** Xerces schema grammar pool. */
    private static XMLGrammarPool grammarPool = new XMLGrammarPoolImpl();
    /** Set holding the NamespaceURIs of the grammarPool, to prevent multiple
     * entries of same grammars to the pool */
    private static Set grammarNamespaces;

    static {
        grammarPool.lockPool();
        grammarNamespaces = new HashSet();
    }

    /**
     * Preparse a schema and add it to the schema pool.
     * The method only adds the schema to the pool if a schema having the same
     * <code>systemId</code> (namespace URI) is not already present in the pool.
     *
     * @param inputStream An <code>InputStream</code> providing the contents of
     * the schema.
     * @param systemId The systemId (namespace URI) to use for the schema.
     * @throws IOException An error occurred reading the schema.
     */
    public static void addSchemaToPool(InputStream inputStream, String systemId)
            throws IOException {
        XMLGrammarPreparser preparser;

        if (!grammarNamespaces.contains(systemId)) {

            grammarNamespaces.add(systemId);

            // unlock the pool so that we can add another grammar
            grammarPool.unlockPool();

            // prepare the preparser
            preparser = new XMLGrammarPreparser(symbolTable);
            preparser.registerPreparser(XMLGrammarDescription.XML_SCHEMA, null);
            preparser.setProperty(GRAMMAR_POOL, grammarPool);
            preparser.setFeature(NAMESPACES_FEATURE, true);
            preparser.setFeature(VALIDATION_FEATURE, true);

            // add the grammar to the pool
            preparser.preparseGrammar(
                    XMLGrammarDescription.XML_SCHEMA,
                    new XMLInputSource(null, systemId, null, inputStream, null));

            // lock the pool again so that schemas are not added automatically
            grammarPool.lockPool();
        }
    }

    /**
     * Parse an XML document from an <code>InputStream</code>.
     *
     * @param inputStream The <code>InputStream</code> containing the XML
     * document.
     * @param validating If <code>true</code>, parse validating.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @param entityResolver An <code>EntityResolver</code> to resolve external
     * entities (schemas and DTDs). If <code>null</code>, it will not be set.
     * @param errorHandler An <code>ErrorHandler</code> to decide what to do
     * with parsing errors. If <code>null</code>, it will not be set.
     * @return The parsed XML document as a DOM tree.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Document parseDocument(
            InputStream inputStream,
            boolean validating,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation,
            EntityResolver entityResolver,
            ErrorHandler errorHandler,
            Map<String, Object> parserFeatures)
            throws  SAXException, IOException, ParserConfigurationException {

        DOMParser parser;

//    class MyEntityResolver implements EntityResolver {
//
//		public InputSource resolveEntity(String publicId, String systemId)
//				throws SAXException, IOException {
//		    return new InputSource(new ByteArrayInputStream(new byte[0]));
//		}
//    }


        //if Debug is enabled make a copy of inputStream to enable debug output in case of SAXException
        byte buffer [] = null;
        ByteArrayInputStream baStream = null;
//        if(true == Logger.isDebugEnabled()) {
//            buffer = IOUtils.toByteArray(inputStream);
//            baStream = new ByteArrayInputStream(buffer);
//
//        }



        // create the DOM parser
        if (symbolTable != null) {
            parser = new DOMParser(symbolTable, grammarPool);
        } else {
            parser = new DOMParser();
        }

        // set parser features and properties
        try {
            parser.setFeature(NAMESPACES_FEATURE, true);
            parser.setFeature(VALIDATION_FEATURE, validating);
            parser.setFeature(SCHEMA_VALIDATION_FEATURE, validating);
            parser.setFeature(NORMALIZED_VALUE_FEATURE, false);
            parser.setFeature(INCLUDE_IGNORABLE_WHITESPACE_FEATURE, true);
            parser.setFeature(CREATE_ENTITY_REF_NODES_FEATURE, false);
            parser.setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
            parser.setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);

            //set external added parser features
            if (parserFeatures != null) {
                for (Entry<String, Object> el : parserFeatures.entrySet()) {
                    String key = el.getKey();
                    if (MiscUtil.isNotEmpty(key)) {
                        Object value = el.getValue();
                        if (value != null && value instanceof Boolean)
                            parser.setFeature(key, (boolean)value);
//
//                        else
//                            Logger.warn("This XML parser only allows features with 'boolean' values");

                    }
//                    else
//                        Logger.warn("Can not set 'null' feature to XML parser");
                }
            }

            //fix XXE problem
            //parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);


            if (validating) {
                if (externalSchemaLocations != null) {
                    parser.setProperty(
                            EXTERNAL_SCHEMA_LOCATION_PROPERTY,
                            externalSchemaLocations);
                }
                if (externalNoNamespaceSchemaLocation != null) {
                    parser.setProperty(
                            EXTERNAL_NO_NAMESPACE_SCHEMA_LOCATION_PROPERTY,
                            externalNoNamespaceSchemaLocation);
                }
            }

            // set entity resolver and error handler
            if (entityResolver != null) {
                parser.setEntityResolver(entityResolver);
            }
            if (errorHandler != null) {
                parser.setErrorHandler(errorHandler);
            }

            // parse the document and return it
            // if debug is enabled: use copy of strem (baStream) else use orig stream
            if(null != baStream)
                parser.parse(new InputSource(baStream));
            else
                parser.parse(new InputSource(inputStream));
        } catch(SAXException e) {
//            if(true == Logger.isDebugEnabled() && null != buffer) {
//                String xmlContent = new String(buffer);
//                Logger.debug("SAXException in:\n" + xmlContent);
//            }
            throw(e);
        }

        return parser.getDocument();
    }

    public static Document parseDocumentSimple(InputStream inputStream)
            throws  SAXException, IOException, ParserConfigurationException {

        DOMParser parser;

        parser = new DOMParser();
        // set parser features and properties
        parser.setFeature(NAMESPACES_FEATURE, true);
        parser.setFeature(VALIDATION_FEATURE, false);
        parser.setFeature(SCHEMA_VALIDATION_FEATURE, false);
        parser.setFeature(NORMALIZED_VALUE_FEATURE, false);
        parser.setFeature(INCLUDE_IGNORABLE_WHITESPACE_FEATURE, true);
        parser.setFeature(CREATE_ENTITY_REF_NODES_FEATURE, false);

        parser.parse(new InputSource(inputStream));

        return parser.getDocument();
    }


    /**
     * Parse an XML document from an <code>InputStream</code>.
     *
     * It uses a <code>MOAEntityResolver</code> as the <code>EntityResolver</code>
     * and a <code>MOAErrorHandler</code> as the <code>ErrorHandler</code>.
     *
     * @param inputStream The <code>InputStream</code> containing the XML
     * document.
     * @param validating If <code>true</code>, parse validating.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @param parserFeatures
     * @return The parsed XML document as a DOM tree.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Document parseDocument(
            InputStream inputStream,
            boolean validating,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation, Map<String, Object> parserFeatures)
            throws SAXException, IOException, ParserConfigurationException {



        return parseDocument(
                inputStream,
                validating,
                externalSchemaLocations,
                externalNoNamespaceSchemaLocation,
                new MOAEntityResolver(),
                new MOAErrorHandler(),
                parserFeatures);
    }

    /**
     * Parse an XML document from a <code>String</code>.
     *
     * It uses a <code>MOAEntityResolver</code> as the <code>EntityResolver</code>
     * and a <code>MOAErrorHandler</code> as the <code>ErrorHandler</code>.
     *
     * @param xmlString The <code>String</code> containing the XML document.
     * @param encoding The encoding of the XML document.
     * @param validating If <code>true</code>, parse validating.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @return The parsed XML document as a DOM tree.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Document parseDocument(
            String xmlString,
            String encoding,
            boolean validating,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation,
            Map<String, Object> parserFeatures)
            throws SAXException, IOException, ParserConfigurationException {

        InputStream in = new ByteArrayInputStream(xmlString.getBytes(encoding));
        return parseDocument(
                in,
                validating,
                externalSchemaLocations,
                externalNoNamespaceSchemaLocation,
                parserFeatures);
    }


    /**
     * Parse an XML document from a <code>String</code>.
     *
     * It uses a <code>MOAEntityResolver</code> as the <code>EntityResolver</code>
     * and a <code>MOAErrorHandler</code> as the <code>ErrorHandler</code>.
     *
     * @param xmlString The <code>String</code> containing the XML document.
     * @param encoding The encoding of the XML document.
     * @param validating If <code>true</code>, parse validating.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @return The parsed XML document as a DOM tree.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Document parseDocument(
            String xmlString,
            String encoding,
            boolean validating,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation)
            throws SAXException, IOException, ParserConfigurationException {

        InputStream in = new ByteArrayInputStream(xmlString.getBytes(encoding));
        return parseDocument(
                in,
                validating,
                externalSchemaLocations,
                externalNoNamespaceSchemaLocation,
                null);
    }

    /**
     * Parse an UTF-8 encoded XML document from a <code>String</code>.
     *
     * @param xmlString The <code>String</code> containing the XML document.
     * @param validating If <code>true</code>, parse validating.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @return The parsed XML document as a DOM tree.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Document parseDocument(
            String xmlString,
            boolean validating,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation)
            throws SAXException, IOException, ParserConfigurationException {

        return parseDocument(
                xmlString,
                "UTF-8",
                validating,
                externalSchemaLocations,
                externalNoNamespaceSchemaLocation);
    }

    /**
     * A convenience method to parse an XML document validating.
     *
     * @param inputStream The <code>InputStream</code> containing the XML
     * document.
     * @return The root element of the parsed XML document.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Element parseXmlValidating(InputStream inputStream)
            throws ParserConfigurationException, SAXException, IOException {
        return DOMUtils
                .parseDocument(inputStream, true, Constants.ALL_SCHEMA_LOCATIONS, null, null)
                .getDocumentElement();
    }

    /**
     * A convenience method to parse an XML document validating.
     *
     * @param inputStream The <code>InputStream</code> containing the XML
     * document.
     * @param parserFeatures Set additional features to XML parser
     * @return The root element of the parsed XML document.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Element parseXmlValidating(InputStream inputStream, Map<String, Object> parserFeatures)
            throws ParserConfigurationException, SAXException, IOException {
        return DOMUtils
                .parseDocument(inputStream, true, Constants.ALL_SCHEMA_LOCATIONS, null, parserFeatures)
                .getDocumentElement();
    }

    /**
     * A convenience method to parse an XML document non validating.
     *
     * @param inputStream The <code>InputStream</code> containing the XML
     * document.
     * @return The root element of the parsed XML document.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * parser.
     */
    public static Element parseXmlNonValidating(InputStream inputStream)
            throws ParserConfigurationException, SAXException, IOException {
        return DOMUtils
                .parseDocument(inputStream, false, Constants.ALL_SCHEMA_LOCATIONS, null, null)
                .getDocumentElement();
    }

    /**
     * Schema validate a given DOM element.
     *
     * @param element The element to validate.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @return <code>true</code>, if the <code>element</code> validates against
     * the schemas declared in it.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document from its
     * serialized representation.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * @throws TransformerException An error occurred serializing the element.
     */
    public static boolean validateElement(
            Element element,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation)
            throws
            ParserConfigurationException,
            IOException,
            SAXException,
            TransformerException {

        byte[] docBytes;
        SAXParser parser;

        // create the SAX parser
        if (symbolTable != null) {
            parser = new SAXParser(symbolTable, grammarPool);
        } else {
            parser = new SAXParser();
        }

        // serialize the document
        docBytes = serializeNode(element, "UTF-8");

        // set up parser features and attributes
        parser.setFeature(NAMESPACES_FEATURE, true);
        parser.setFeature(VALIDATION_FEATURE, true);
        parser.setFeature(SCHEMA_VALIDATION_FEATURE, true);
        parser.setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
        parser.setFeature(DISALLOW_DOCTYPE_FEATURE, true);


        if (externalSchemaLocations != null) {
            parser.setProperty(
                    EXTERNAL_SCHEMA_LOCATION_PROPERTY,
                    externalSchemaLocations);
        }
        if (externalNoNamespaceSchemaLocation != null) {
            parser.setProperty(
                    EXTERNAL_NO_NAMESPACE_SCHEMA_LOCATION_PROPERTY,
                    "externalNoNamespaceSchemaLocation");
        }

        // set up entity resolver and error handler
        parser.setEntityResolver(new MOAEntityResolver());
        parser.setErrorHandler(new MOAErrorHandler());

        // parse validating
        parser.parse(new InputSource(new ByteArrayInputStream(docBytes)));
        return true;
    }


    /**
     * Schema validate a given DOM element.
     *
     * @param element The element to validate.
     * @param externalSchemaLocations A <code>String</code> containing namespace
     * URI to schema location pairs, the same way it is accepted by the <code>xsi:
     * schemaLocation</code> attribute.
     * @param externalNoNamespaceSchemaLocation The schema location of the
     * schema for elements without a namespace, the same way it is accepted by the
     * <code>xsi:noNamespaceSchemaLocation</code> attribute.
     * @return <code>true</code>, if the <code>element</code> validates against
     * the schemas declared in it.
     * @throws SAXException An error occurred parsing the document.
     * @throws IOException An error occurred reading the document from its
     * serialized representation.
     * @throws ParserConfigurationException An error occurred configuring the XML
     * @throws TransformerException An error occurred serializing the element.
     */
    public static boolean validateElement(
            Element element,
            String externalSchemaLocations,
            String externalNoNamespaceSchemaLocation,
            EntityResolver entityResolver)
            throws
            ParserConfigurationException,
            IOException,
            SAXException,
            TransformerException {

        byte[] docBytes;
        SAXParser parser;

        // create the SAX parser
        if (symbolTable != null) {
            parser = new SAXParser(symbolTable, grammarPool);
        } else {
            parser = new SAXParser();
        }

        // serialize the document
        docBytes = serializeNode(element, "UTF-8");

        // set up parser features and attributes
        parser.setFeature(NAMESPACES_FEATURE, true);
        parser.setFeature(VALIDATION_FEATURE, true);
        parser.setFeature(SCHEMA_VALIDATION_FEATURE, true);

        if (externalSchemaLocations != null) {
            parser.setProperty(
                    EXTERNAL_SCHEMA_LOCATION_PROPERTY,
                    externalSchemaLocations);
        }
        if (externalNoNamespaceSchemaLocation != null) {
            parser.setProperty(
                    EXTERNAL_NO_NAMESPACE_SCHEMA_LOCATION_PROPERTY,
                    "externalNoNamespaceSchemaLocation");
        }

        // set up entity resolver and error handler
        parser.setEntityResolver(entityResolver);
        parser.setErrorHandler(new MOAErrorHandler());

        // parse validating
        parser.parse(new InputSource(new ByteArrayInputStream(docBytes)));
        return true;
    }

    /**
     * Serialize the given DOM node.
     *
     * The node will be serialized using the UTF-8 encoding.
     *
     * @param node The node to serialize.
     * @return String The <code>String</code> representation of the given DOM
     * node.
     * @throws TransformerException An error occurred transforming the
     * node to a <code>String</code>.
     * @throws IOException An IO error occurred writing the node to a byte array.
     */
    public static String serializeNode(Node node)
            throws TransformerException, IOException {
        return new String(serializeNode(node, "UTF-8", false), "UTF-8");
    }


    /**
     * Serialize the given DOM node.
     *
     * The node will be serialized using the UTF-8 encoding.
     *
     * @param node The node to serialize.
     * @param omitXmlDeclaration The boolean value for omitting the XML Declaration.
     * @return String The <code>String</code> representation of the given DOM
     * node.
     * @throws TransformerException An error occurred transforming the
     * node to a <code>String</code>.
     * @throws IOException An IO error occurred writing the node to a byte array.
     */
    public static String serializeNode(Node node, boolean omitXmlDeclaration)
            throws TransformerException, IOException {
        return new String(serializeNode(node, "UTF-8", omitXmlDeclaration), "UTF-8");
    }

    /**
     * Serialize the given DOM node.
     *
     * The node will be serialized using the UTF-8 encoding.
     *
     * @param node The node to serialize.
     * @param omitXmlDeclaration The boolean value for omitting the XML Declaration.
     * @param lineSeperator Sets the line seperator String of the parser
     * @return String The <code>String</code> representation of the given DOM
     * node.
     * @throws TransformerException An error occurred transforming the
     * node to a <code>String</code>.
     * @throws IOException An IO error occurred writing the node to a byte array.
     */
    public static String serializeNode(Node node, boolean omitXmlDeclaration, String lineSeperator)
            throws TransformerException, IOException {
        return new String(serializeNode(node, "UTF-8", omitXmlDeclaration, lineSeperator), "UTF-8");
    }

    /**
     * Serialize the given DOM node to a byte array.
     *
     * @param node The node to serialize.
     * @param xmlEncoding The XML encoding to use.
     * @return The serialized node, as a byte array. Using a compatible encoding
     * this can easily be converted into a <code>String</code>.
     * @throws TransformerException An error occurred transforming the node to a
     * byte array.
     * @throws IOException An IO error occurred writing the node to a byte array.
     */
    public static byte[] serializeNode(Node node, String xmlEncoding)
            throws TransformerException, IOException {
        return serializeNode(node, xmlEncoding, false);
    }

    /**
     * Serialize the given DOM node to a byte array.
     *
     * @param node The node to serialize.
     * @param xmlEncoding The XML encoding to use.
     * @param omitDeclaration The boolean value for omitting the XML Declaration.
     * @return The serialized node, as a byte array. Using a compatible encoding
     * this can easily be converted into a <code>String</code>.
     * @throws TransformerException An error occurred transforming the node to a
     * byte array.
     * @throws IOException An IO error occurred writing the node to a byte array.
     */
    public static byte[] serializeNode(Node node, String xmlEncoding, boolean omitDeclaration)
            throws TransformerException, IOException {
        return serializeNode(node, xmlEncoding, omitDeclaration, null);
    }


    /**
     * Serialize the given DOM node to a byte array.
     *
     * @param node The node to serialize.
     * @param xmlEncoding The XML encoding to use.
     * @param omitDeclaration The boolean value for omitting the XML Declaration.
     * @param lineSeperator Sets the line seperator String of the parser
     * @return The serialized node, as a byte array. Using a compatible encoding
     * this can easily be converted into a <code>String</code>.
     * @throws TransformerException An error occurred transforming the node to a
     * byte array.
     * @throws IOException An IO error occurred writing the node to a byte array.
     */
    public static byte[] serializeNode(Node node, String xmlEncoding, boolean omitDeclaration, String lineSeperator)
            throws TransformerException, IOException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(16384);

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, xmlEncoding);
        String omit = omitDeclaration ? "yes" : "no";
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omit);
        if (null!=lineSeperator) {
            transformer.setOutputProperty("{http://xml.apache.org/xalan}line-separator", lineSeperator);//does not work for xalan <= 2.5.1
        }
        transformer.transform(new DOMSource(node), new StreamResult(bos));

        bos.flush();
        bos.close();

        return bos.toByteArray();
    }

    /**
     * Return the text that a node contains.
     *
     * This routine:
     * <ul>
     * <li>Ignores comments and processing instructions.</li>
     * <li>Concatenates TEXT nodes, CDATA nodes, and the results recursively
     * processing EntityRef nodes.</li>
     * <li>Ignores any element nodes in the sublist. (Other possible options are
     * to recurse into element sublists or throw an exception.)</li>
     * </ul>
     *
     * @param node A DOM node from which to extract text.
     * @return A String representing its contents.
     */
    public static String getText(Node node) {
        if (!node.hasChildNodes()) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        NodeList list = node.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node subnode = list.item(i);
            if (subnode.getNodeType() == Node.TEXT_NODE) {
                result.append(subnode.getNodeValue());
            } else if (subnode.getNodeType() == Node.CDATA_SECTION_NODE) {
                result.append(subnode.getNodeValue());
            } else if (subnode.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
                // Recurse into the subtree for text
                // (and ignore comments)
                result.append(getText(subnode));
            }
        }
        return result.toString();
    }

    /**
     * Build the namespace prefix to namespace URL mapping in effect for a given
     * node.
     *
     * @param node The context node for which build the map.
     * @return The namespace prefix to namespace URL mapping (
     * a <code>String</code> value to <code>String</code> value mapping).
     */
    public static Map getNamespaceDeclarations(Node node) {
        Map nsDecls = new HashMap();
        int i;

        do {
            if (node.hasAttributes()) {
                NamedNodeMap attrs = node.getAttributes();

                for (i = 0; i < attrs.getLength(); i++) {
                    Attr attr = (Attr) attrs.item(i);

                    // add prefix mapping if none exists
                    if ("xmlns".equals(attr.getPrefix())
                            || "xmlns".equals(attr.getName())) {

                        String nsPrefix =
                                attr.getPrefix() != null ? attr.getLocalName() : "";

                        if (nsDecls.get(nsPrefix) == null) {
                            nsDecls.put(nsPrefix, attr.getValue());
                        }
                    }
                }
            }
        } while ((node = node.getParentNode()) != null);

        return nsDecls;
    }

    /**
     * Add all namespace declarations declared in the parent(s) of a given
     * element and used in the subtree of the given element to the given element.
     *
     * @param context The element to which to add the namespaces.
     */
    public static void localizeNamespaceDeclarations(Element context) {
        Node parent = context.getParentNode();

        if (parent != null) {
            Map namespaces = getNamespaceDeclarations(context.getParentNode());
            Set nsUris = collectNamespaceURIs(context);
            Iterator iter;

            for (iter = namespaces.entrySet().iterator(); iter.hasNext();) {
                Map.Entry e = (Map.Entry) iter.next();

                if (nsUris.contains(e.getValue())) {
                    String prefix = (String) e.getKey();
                    String nsUri = (String) e.getValue();
                    String nsAttrName = "".equals(prefix) ? "xmlns" : "xmlns:" + prefix;

                    context.setAttributeNS(Constants.XMLNS_NS_URI, nsAttrName, nsUri);
                }
            }
        }
    }

    /**
     * Collect all the namespace URIs used in the subtree of a given element.
     *
     * @param context The element that should be searched for namespace URIs.
     * @return All namespace URIs used in the subtree of <code>context</code>,
     * including the ones used in <code>context</code> itself.
     */
    public static Set collectNamespaceURIs(Element context) {
        Set result = new HashSet();

        collectNamespaceURIsImpl(context, result);
        return result;
    }

    /**
     * A recursive method to do the work of <code>collectNamespaceURIs</code>.
     *
     * @param context The context element to evaluate.
     * @param result The result, passed as a parameter to avoid unnecessary
     * instantiations of <code>Set</code>.
     */
    private static void collectNamespaceURIsImpl(Element context, Set result) {
        NamedNodeMap attrs = context.getAttributes();
        NodeList childNodes = context.getChildNodes();
        String nsUri;
        int i;

        // add the namespace of the context element
        nsUri = context.getNamespaceURI();
        if (nsUri != null && nsUri != Constants.XMLNS_NS_URI) {
            result.add(nsUri);
        }

        // add all namespace URIs from attributes
        for (i = 0; i < attrs.getLength(); i++) {
            nsUri = attrs.item(i).getNamespaceURI();
            if (nsUri != null && nsUri != Constants.XMLNS_NS_URI) {
                result.add(nsUri);
            }
        }

        // add all namespaces from subelements
        for (i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                collectNamespaceURIsImpl((Element) node, result);
            }
        }
    }

    /**
     * Check, that each attribute node in the given <code>NodeList</code> has its
     * parent in the <code>NodeList</code> as well.
     *
     * @param nodes The <code>NodeList</code> to check.
     * @return <code>true</code>, if each attribute node in <code>nodes</code>
     * has its parent in <code>nodes</code> as well.
     */
    public static boolean checkAttributeParentsInNodeList(NodeList nodes) {
        Set nodeSet = new HashSet();
        int i;

        // put the nodes into the nodeSet
        for (i = 0; i < nodes.getLength(); i++) {
            nodeSet.add(nodes.item(i));
        }

        // check that each attribute node's parent is in the node list
        for (i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);

            if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
                Attr attr = (Attr) n;
                Element owner = attr.getOwnerElement();

                if (owner == null) {
                    if (!isNamespaceDeclaration(attr)) {
                        return false;
                    }
                }

                if (!nodeSet.contains(owner) && !isNamespaceDeclaration(attr)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Convert an unstructured <code>NodeList</code> into a
     * <code>DocumentFragment</code>.
     *
     * @param nodeList Contains the node list to be converted into a DOM
     * DocumentFragment.
     * @return the resulting DocumentFragment. The DocumentFragment will be
     * backed by a new DOM Document, i.e. all noded of the node list will be
     * cloned.
     * @throws ParserConfigurationException An error occurred creating the
     * DocumentFragment.
     * @precondition The nodes in the node list appear in document order
     * @precondition for each Attr node in the node list, the owning Element is
     * in the node list as well.
     * @precondition each Element or Attr node in the node list is namespace
     * aware.
     */
    public static DocumentFragment nodeList2DocumentFragment(NodeList nodeList)
            throws ParserConfigurationException {

        DocumentBuilder builder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        DocumentFragment result = doc.createDocumentFragment();

        if (null == nodeList || nodeList.getLength() == 0) {
            return result;
        }

        int currPos = 0;
        currPos =
                nodeList2DocumentFragment(nodeList, currPos, result, null, null) + 1;

        while (currPos < nodeList.getLength()) {
            currPos =
                    nodeList2DocumentFragment(nodeList, currPos, result, null, null) + 1;
        }
        return result;
    }

    /**
     * Helper method for the <code>nodeList2DocumentFragment</code>.
     *
     * @param nodeList The <code>NodeList</code> to convert.
     * @param currPos The current position in the <code>nodeList</code>.
     * @param result The resulting <code>DocumentFragment</code>.
     * @param currOrgElem The current original element.
     * @param currClonedElem The current cloned element.
     * @return The current position.
     */
    private static int nodeList2DocumentFragment(
            NodeList nodeList,
            int currPos,
            DocumentFragment result,
            Element currOrgElem,
            Element currClonedElem) {

        while (currPos < nodeList.getLength()) {
            Node currentNode = nodeList.item(currPos);
            switch (currentNode.getNodeType()) {
                case Node.COMMENT_NODE :
                case Node.PROCESSING_INSTRUCTION_NODE :
                case Node.TEXT_NODE :
                {
                    // Append current node either to resulting DocumentFragment or to
                    // current cloned Element
                    if (null == currClonedElem) {
                        result.appendChild(
                                result.getOwnerDocument().importNode(currentNode, false));
                    } else {
                        // Stop processing if current Node is not a descendant of
                        // current Element
                        if (!isAncestor(currOrgElem, currentNode)) {
                            return --currPos;
                        }

                        currClonedElem.appendChild(
                                result.getOwnerDocument().importNode(currentNode, false));
                    }
                    break;
                }

                case Node.ELEMENT_NODE :
                {
                    Element nextCurrOrgElem = (Element) currentNode;
                    Element nextCurrClonedElem =
                            result.getOwnerDocument().createElementNS(
                                    nextCurrOrgElem.getNamespaceURI(),
                                    nextCurrOrgElem.getNodeName());

                    // Append current Node either to resulting DocumentFragment or to
                    // current cloned Element
                    if (null == currClonedElem) {
                        result.appendChild(nextCurrClonedElem);
                        currOrgElem = nextCurrOrgElem;
                        currClonedElem = nextCurrClonedElem;
                    } else {
                        // Stop processing if current Node is not a descendant of
                        // current Element
                        if (!isAncestor(currOrgElem, currentNode)) {
                            return --currPos;
                        }

                        currClonedElem.appendChild(nextCurrClonedElem);
                    }

                    // Process current Node (of type Element) recursively
                    currPos =
                            nodeList2DocumentFragment(
                                    nodeList,
                                    ++currPos,
                                    result,
                                    nextCurrOrgElem,
                                    nextCurrClonedElem);

                    break;
                }

                case Node.ATTRIBUTE_NODE :
                {
                    Attr currAttr = (Attr) currentNode;

                    // GK 20030411: Hack to overcome problems with IAIK IXSIL
                    if (currAttr.getOwnerElement() == null)
                        break;
                    if (currClonedElem == null)
                        break;

                    // currClonedElem must be the owner Element of currAttr if
                    // preconditions are met
                    currClonedElem.setAttributeNS(
                            currAttr.getNamespaceURI(),
                            currAttr.getNodeName(),
                            currAttr.getValue());
                    break;
                }

                default :
                {
                    // All other nodes will be ignored
                }
            }

            currPos++;
        }

        return currPos;
    }

    /**
     * Check, if the given attribute is a namespace declaration.
     *
     * @param attr The attribute to check.
     * @return <code>true</code>, if the attribute is a namespace declaration,
     * <code>false</code> otherwise.
     */
    private static boolean isNamespaceDeclaration(Attr attr) {
        return Constants.XMLNS_NS_URI.equals(attr.getNamespaceURI());
    }

    /**
     * Check, if a given DOM element is an ancestor of a given node.
     *
     * @param candAnc The DOM element to check for being the ancestor.
     * @param cand The node to check for being the child.
     * @return <code>true</code>, if <code>candAnc</code> is an (indirect)
     * ancestor of <code>cand</code>; <code>false</code> otherwise.
     */
    public static boolean isAncestor(Element candAnc, Node cand) {
        Node currPar = cand.getParentNode();

        while (currPar != null) {
            if (candAnc == currPar)
                return true;
            currPar = currPar.getParentNode();
        }
        return false;
    }

    /**
     * Selects the (first) element from a node list and returns it.
     *
     * @param nl  The NodeList to get the element from.
     * @return    The (first) element included in the node list or <code>null</code>
     *            if the node list is <code>null</code> or empty or no element is
     *            included in the list.
     */
    public static Element getElementFromNodeList (NodeList nl) {
        if ((nl == null) || (nl.getLength() == 0)) {
            return null;
        }
        for (int i=0; i<nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)  {
                return  (Element)node;
            }
        }
        return null;
    }

    /**
     * Returns all child elements of the given element.
     *
     * @param parent  The element to get the child elements from.
     *
     * @return A list including all child elements of the given element.
     *         Maybe empty if the parent element has no child elements.
     */
    public static List getChildElements (Element parent) {
        Vector v = new Vector();
        NodeList nl = parent.getChildNodes();
        int length = nl.getLength();
        for (int i=0; i < length; i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                v.add((Element)node);
            }
        }
        return v;
    }

    /**
     * Returns a byte array from given node.
     * @param node
     * @return
     * @throws TransformerException
     */
    public static byte[] nodeToByteArray(Node node) throws TransformerException {
        Source source = new DOMSource(node);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(out);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.transform(source, result);
        return out.toByteArray();
    }


}
