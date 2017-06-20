/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

/*
 *                        AT&T - PROPRIETARY
 *          THIS FILE CONTAINS PROPRIETARY INFORMATION OF
 *        AT&T AND IS NOT TO BE DISCLOSED OR USED EXCEPT IN
 *             ACCORDANCE WITH APPLICABLE AGREEMENTS.
 *
 *          Copyright (c) 2013 AT&T Knowledge Ventures
 *              Unpublished and Not for Publication
 *                     All Rights Reserved
 */
package org.apache.openaz.xacml.std.dom;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.Version;
import org.apache.openaz.xacml.api.XACML2;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.StdVersion;
import org.apache.openaz.xacml.std.StdVersionMatch;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOMUtil contains a number of utility functions for DOM document elements.
 */
public class DOMUtil {
  private static String[]               NAMESPACES    = {
      XACML3.XMLNS,
      XACML2.XMLNS };

  private static DocumentBuilderFactory documentBuilderFactory;

  /*
   * The namespace string for the "xml" prefix
   */
  private static final String           XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";

  protected DOMUtil() {
  }

  /**
   * Creates a copy of the given <code>Node</code> such that it appears to be
   * the direct child of a
   * <code>Document</code>>
   *
   * @param node
   *          the <code>Node</code> to convert
   * @return the new <code>Node</code>
   */
  public static Node getDirectDocumentChild(final Node node) throws DOMStructureException {
    Node nodeResult = null;
    try {
      final DocumentBuilder documentBuilder = getDocumentBuilder();
      final Document documentRoot = documentBuilder.newDocument();
      final Node nodeTopRoot = documentRoot.importNode(node, true);
      documentRoot.appendChild(nodeTopRoot);
      nodeResult = documentRoot.getDocumentElement();
    } catch (final Exception ex) {
      throw new DOMStructureException("Exception generating Document root Node from Node: " + ex.getMessage(),
          ex);
    }
    return nodeResult;
  }

  /**
   * Converts the given <code>Node</code> to a <code>Element</code> if possible.
   *
   * @param node
   *          the <code>Node</code> to convert
   * @return the <code>Node</code> cast as an <code>Element</code>.
   * @throws DOMStructureException
   *           if the cast cannot be made
   */
  public static Element getElement(final Node node) throws DOMStructureException {
    if (node == null) {
      throw new DOMStructureException(node, new NullPointerException("Null Node"));
    } else if (node.getNodeType() != Node.ELEMENT_NODE) {
      throw new DOMStructureException(node, "Non-element Node");
    }
    return (Element) node;
  }

  /**
   * Determines if the given <code>Node</code> is non-null and is an XML
   * Element.
   *
   * @param node
   *          the <code>Node</code> to check
   * @return true if the <code>Node</code> is non-null and is an XML element
   */
  public static boolean isElement(final Node node) {
    return node != null && node.getNodeType() == Node.ELEMENT_NODE;
  }

  /**
   * Determines if the given <code>Node</code> belongs to the namespace with the
   * given <code>String</code>
   * name.
   *
   * @param node
   *          the <code>Node</code> to check
   * @param namespace
   *          the <code>String</code> namespace
   * @return true if the <code>Node</code> namespace matches, else false
   */
  public static boolean isInNamespace(final Node node, final String namespace) {
    return namespace.equals(node.getNamespaceURI());
  }

  /**
   * Determines if the given <code>Node</code> is an <code>Element</code> and is
   * in the given
   * <code>String</code> namespace.
   *
   * @param node
   *          the <code>Node</code> to check
   * @param namespace
   *          the <code>String</code> namespace to check or null if no namespace
   *          check is required
   * @return true if the given <code>Node</code> is an <code>Element</code> and
   *         the <code>namespace</code>
   *         is null or matches the <code>Node</code> namespace.
   */
  public static boolean isNamespaceElement(final Node node, final String namespace) {
    if (node == null) {
      return false;
    } else if (node.getNodeType() != Node.ELEMENT_NODE) {
      return false;
    } else if (namespace != null && !namespace.equals(node.getNamespaceURI())) {
      return false;
    } else {
      return true;
    }
  }

  public static String getNodeLabel(final Node node) {
    final String namespaceURI = node.getNamespaceURI();
    return (namespaceURI == null ? node.getLocalName() : namespaceURI + ":" + node.getLocalName());
  }

  public static DOMStructureException newUnexpectedElementException(final Node node) {
    return new DOMStructureException(node, "Unexpected element \"" + getNodeLabel(node) + "\"");
  }

  public static DOMStructureException newUnexpectedElementException(final Node node, final Node parent) {
    return new DOMStructureException(node,
        "Unexpected element \"" + getNodeLabel(node) + "\" in \"" + getNodeLabel(parent) + "\"");
  }

  /**
   * Gets the first child {@link org.w3c.dom.Element} of the given
   * <code>Node</code>.
   *
   * @param node
   *          the <code>Node</code> to search
   * @return the first child <code>Element</code> of the given <code>Node</code>
   */
  public static Element getFirstChildElement(final Node rootNode) {
    if (rootNode == null) {
      return null;
    }
    Node node = rootNode.getFirstChild();
    while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
      node = node.getNextSibling();
    }
    if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
      return (Element) node;
    }
    return null;
  }

  protected static DOMStructureException newMissingAttributeException(final Node node,
      final String attributeName) {
    return new DOMStructureException(
        "Missing attribute \"" + attributeName + "\" in \"" + getNodeLabel(node) + "\"");
  }

  protected static DOMStructureException newMissingAttributeException(final Node node, final String namespace,
      final String attributeName) {
    return new DOMStructureException("Missing attribute \"" + (namespace == null ? "" : namespace + ":")
        + attributeName + "\" in \"" + getNodeLabel(node) + "\"");
  }

  protected static DOMStructureException newMissingContentException(final Node node) {
    return new DOMStructureException("Missing content for \"" + getNodeLabel(node) + "\"");
  }

  public static DOMStructureException newMissingElementException(final Node node, final String namespace,
      final String elementName) {
    return new DOMStructureException("Missing element \"" + (namespace == null ? "" : namespace + ":")
        + elementName + "\" in \"" + getNodeLabel(node));
  }

  public static Node getAttribute(final Node node, final String[] nameSpace, final String localName,
      final boolean bRequired) throws DOMStructureException {
    Node nodeResult = null;
    for (final String namespace : nameSpace) {
      if ((nodeResult = node.getAttributes().getNamedItemNS(namespace, localName)) != null) {
        return nodeResult;
      }
    }
    if (bRequired) {
      throw newMissingAttributeException(node, localName);
    }
    return null;
  }

  public static Node getAttribute(final Node node, final String[] nameSpace, final String localName) {
    Node nodeResult = null;
    for (final String namespace : nameSpace) {
      if ((nodeResult = node.getAttributes().getNamedItemNS(namespace, localName)) != null) {
        return nodeResult;
      }
    }
    return nodeResult;
  }

  /**
   * Retrieves an attribute value from the given <code>Node</code> with the
   * given <code>String</code>
   * namespace and <code>String</code> local name.
   *
   * @param node
   * @param nameSpace
   * @param localName
   * @return
   */
  public static Node getAttribute(final Node node, final String nameSpace, final String localName,
      final boolean bRequired) throws DOMStructureException {
    final Node nodeAttribute = node.getAttributes().getNamedItemNS(nameSpace, localName);
    if (bRequired && nodeAttribute == null) {
      throw newMissingAttributeException(node, nameSpace, localName);
    }
    return nodeAttribute;
  }

  public static Node getAttribute(final Node node, final String nameSpace, final String localName) {
    return node.getAttributes().getNamedItemNS(nameSpace, localName);
  }

  /**
   * Retrieves an attribute value from the given <code>Node</code> by the given
   * local <code>String</code>
   * name by searching all known namespaces.
   *
   * @param node
   * @param localName
   * @return
   */
  public static Node getAttribute(final Node node, final String localName, final boolean bRequired)
      throws DOMStructureException {
    final Node nodeAttribute = node.getAttributes().getNamedItem(localName);
    if (bRequired && nodeAttribute == null) {
      throw newMissingAttributeException(node, localName);
    }
    return nodeAttribute;
  }

  public static Node getAttribute(final Node node, final String localName) {
    return node.getAttributes().getNamedItem(localName);
  }

  public static String getStringAttribute(final Node node, final String[] nameSpaces, final String localName,
      final boolean bRequired) throws DOMStructureException {
    final Node nodeAttribute = getAttribute(node, nameSpaces, localName, bRequired);
    return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
  }

  public static String getStringAttribute(final Node node, final String[] nameSpaces,
      final String localName) {
    final Node nodeAttribute = getAttribute(node, nameSpaces, localName);
    return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
  }

  public static String getStringAttribute(final Node node, final String nameSpace, final String localName,
      final boolean bRequired) throws DOMStructureException {
    final Node nodeAttribute = getAttribute(node, nameSpace, localName, bRequired);
    return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
  }

  public static String getStringAttribute(final Node node, final String nameSpace, final String localName) {
    final Node nodeAttribute = getAttribute(node, nameSpace, localName);
    return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
  }

  public static String getStringAttribute(final Node node, final String localName, final boolean bRequired)
      throws DOMStructureException {
    final Node nodeAttribute = getAttribute(node, localName, bRequired);
    return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
  }

  public static String getStringAttribute(final Node node, final String localName) {
    final Node nodeAttribute = getAttribute(node, localName);
    return (nodeAttribute == null ? null : nodeAttribute.getNodeValue());
  }

  public static String getXmlId(final Node node) {
    return getStringAttribute(node, XML_NAMESPACE, "id");
  }

  public static String getXmlId(final Node node, final boolean bRequired) throws DOMStructureException {
    return getStringAttribute(node, XML_NAMESPACE, "id", bRequired);
  }

  private static Identifier getIdentifierFromString(final Node node, final String stringAttribute)
      throws DOMStructureException {
    if (stringAttribute == null) {
      return null;
    } else {
      Identifier identifierResult = null;
      try {
        identifierResult = new IdentifierImpl(stringAttribute);
      } catch (final IllegalArgumentException ex) {
        throw new DOMStructureException(node,
            "Invalid Identifier \"" + stringAttribute + "\" in \"" + getNodeLabel(node) + "\"", ex);
      }
      return identifierResult;
    }
  }

  public static Identifier getIdentifierAttribute(final Node node, final String[] nameSpaces,
      final String localName, final boolean bRequired) throws DOMStructureException {
    return getIdentifierFromString(node, getStringAttribute(node, nameSpaces, localName, bRequired));
  }

  public static Identifier getIdentifierAttribute(final Node node, final String[] nameSpaces,
      final String localName) throws DOMStructureException {
    return getIdentifierFromString(node, getStringAttribute(node, nameSpaces, localName));
  }

  public static Identifier getIdentifierAttribute(final Node node, final String nameSpace,
      final String localName, final boolean bRequired) throws DOMStructureException {
    return getIdentifierFromString(node, getStringAttribute(node, nameSpace, localName, bRequired));
  }

  public static Identifier getIdentifierAttribute(final Node node, final String nameSpace,
      final String localName) throws DOMStructureException {
    return getIdentifierFromString(node, getStringAttribute(node, nameSpace, localName));
  }

  public static Identifier getIdentifierAttribute(final Node node, final String localName,
      final boolean bRequired) throws DOMStructureException {
    return getIdentifierFromString(node, getStringAttribute(node, localName, bRequired));
  }

  public static Identifier getIdentifierAttribute(final Node node, final String localName)
      throws DOMStructureException {
    return getIdentifierFromString(node, getStringAttribute(node, localName));
  }

  public static Identifier getIdentifierContent(final Node node, final boolean bRequired)
      throws DOMStructureException {
    final Identifier identifier = getIdentifierFromString(node, node.getTextContent());
    if (bRequired && identifier == null) {
      throw newMissingContentException(node);
    }
    return identifier;
  }

  public static Identifier getIdentifierContent(final Node node) throws DOMStructureException {
    return getIdentifierFromString(node, node.getTextContent());
  }

  private static Integer getIntegerFromString(final Node node, final String stringValue)
      throws DOMStructureException {
    if (stringValue == null) {
      return null;
    } else {
      Integer iresult = null;
      try {
        iresult = Integer.parseInt(stringValue);
      } catch (final NumberFormatException ex) {
        throw new DOMStructureException(node,
            "Invalid Integer \"" + stringValue + "\" in \"" + getNodeLabel(node) + "\"", ex);
      }
      return iresult;
    }
  }

  public static Integer getIntegerAttribute(final Node node, final String[] nameSpaces,
      final String localName, final boolean bRequired) throws DOMStructureException {
    return getIntegerFromString(node, getStringAttribute(node, nameSpaces, localName, bRequired));
  }

  public static Integer getIntegerAttribute(final Node node, final String[] nameSpaces,
      final String localName) throws DOMStructureException {
    return getIntegerFromString(node, getStringAttribute(node, nameSpaces, localName));
  }

  public static Integer getIntegerAttribute(final Node node, final String nameSpace, final String localName,
      final boolean bRequired) throws DOMStructureException {
    return getIntegerFromString(node, getStringAttribute(node, nameSpace, localName, bRequired));
  }

  public static Integer getIntegerAttribute(final Node node, final String nameSpace, final String localName)
      throws DOMStructureException {
    return getIntegerFromString(node, getStringAttribute(node, nameSpace, localName));
  }

  public static Integer getIntegerAttribute(final Node node, final String localName, final boolean bRequired)
      throws DOMStructureException {
    return getIntegerFromString(node, getStringAttribute(node, localName, bRequired));
  }

  public static Integer getIntegerAttribute(final Node node, final String localName)
      throws DOMStructureException {
    return getIntegerFromString(node, getStringAttribute(node, localName));
  }

  private static Version getVersionFromString(final Node node, final String stringValue)
      throws DOMStructureException {
    Version version = null;
    try {
      version = StdVersion.newInstance(stringValue);
    } catch (final ParseException ex) {
      throw new DOMStructureException(node,
          "Invalid Version \"" + stringValue + "\" in \"" + getNodeLabel(node) + "\"", ex);
    }
    return version;
  }

  public static Version getVersionAttribute(final Node node, final String[] nameSpaces,
      final String localName, final boolean bRequired) throws DOMStructureException {
    return getVersionFromString(node, getStringAttribute(node, nameSpaces, localName, bRequired));
  }

  public static Version getVersionAttribute(final Node node, final String[] nameSpaces,
      final String localName) throws DOMStructureException {
    return getVersionFromString(node, getStringAttribute(node, nameSpaces, localName));
  }

  public static Version getVersionAttribute(final Node node, final String nameSpace, final String localName,
      final boolean bRequired) throws DOMStructureException {
    return getVersionFromString(node, getStringAttribute(node, nameSpace, localName, bRequired));
  }

  public static Version getVersionAttribute(final Node node, final String nameSpace, final String localName)
      throws DOMStructureException {
    return getVersionFromString(node, getStringAttribute(node, nameSpace, localName));
  }

  public static Version getVersionAttribute(final Node node, final String localName, final boolean bRequired)
      throws DOMStructureException {
    return getVersionFromString(node, getStringAttribute(node, localName, bRequired));
  }

  public static Version getVersionAttribute(final Node node, final String localName)
      throws DOMStructureException {
    return getVersionFromString(node, getStringAttribute(node, localName));
  }

  private static URI getURIFromString(final Node node, final String stringAttribute)
      throws DOMStructureException {
    if (stringAttribute == null) {
      return null;
    } else {
      URI uriResult = null;
      try {
        uriResult = new URI(stringAttribute);
      } catch (final URISyntaxException ex) {
        throw new DOMStructureException(node,
            "Illegal URI value \"" + stringAttribute + "\" in \"" + getNodeLabel(node) + "\"", ex);
      }
      return uriResult;
    }
  }

  public static URI getURIContent(final Node node, final boolean bRequired) throws DOMStructureException {
    final URI uri = getURIFromString(node, node.getTextContent());
    if (bRequired && uri == null) {
      throw newMissingContentException(node);
    }
    return uri;
  }

  public static URI getURIContent(final Node node) throws DOMStructureException {
    return getURIFromString(node, node.getTextContent());
  }

  protected static Boolean toBoolean(final Node node, final String stringAttribute)
      throws DOMStructureException {
    if (stringAttribute == null) {
      return null;
    } else if (stringAttribute.equals("0") || stringAttribute.equalsIgnoreCase("false")) {
      return Boolean.FALSE;
    } else if (stringAttribute.equals("1") || stringAttribute.equalsIgnoreCase("true")) {
      return Boolean.TRUE;
    } else {
      throw new DOMStructureException(
          "Illegal Boolean value \"" + stringAttribute + "\" in \"" + getNodeLabel(node) + "\"");
    }
  }

  public static Boolean getBooleanAttribute(final Node node, final String[] nameSpaces,
      final String localName, final boolean bRequired) throws DOMStructureException {
    return toBoolean(node, getStringAttribute(node, nameSpaces, localName, bRequired));
  }

  public static Boolean getBooleanAttribute(final Node node, final String[] nameSpaces,
      final String localName) throws DOMStructureException {
    return toBoolean(node, getStringAttribute(node, nameSpaces, localName));
  }

  public static Boolean getBooleanAttribute(final Node node, final String nameSpace, final String localName,
      final boolean bRequired) throws DOMStructureException {
    return toBoolean(node, getStringAttribute(node, nameSpace, localName, bRequired));
  }

  public static Boolean getBooleanAttribute(final Node node, final String nameSpace, final String localName)
      throws DOMStructureException {
    return toBoolean(node, getStringAttribute(node, nameSpace, localName));
  }

  public static Boolean getBooleanAttribute(final Node node, final String localName, final boolean bRequired)
      throws DOMStructureException {
    return toBoolean(node, getStringAttribute(node, localName, bRequired));
  }

  public static Boolean getBooleanAttribute(final Node node, final String localName)
      throws DOMStructureException {
    return toBoolean(node, getStringAttribute(node, localName));
  }

  public static NodeList getNodes(final Element element, final String[] nameSpaces, final String localName) {
    NodeList nodeListResult = null;
    for (final String namespace : nameSpaces) {
      if ((nodeListResult = element.getElementsByTagNameNS(namespace, localName)) != null
          && nodeListResult.getLength() > 0) {
        return nodeListResult;
      }
    }
    return null;
  }

  public static Node getNode(final Element element, final String[] nameSpaces, final String localName)
      throws DOMStructureException {
    final NodeList nodeList = getNodes(element, nameSpaces, localName);
    if (nodeList == null || nodeList.getLength() == 0) {
      return null;
    } else if (nodeList.getLength() > 1) {
      throw new DOMStructureException(element, "More than one \"" + localName + "\" element");
    } else {
      return nodeList.item(0);
    }
  }

  public static NodeList getNodes(final Element element, final String nameSpace, final String localName) {
    return element.getElementsByTagNameNS(nameSpace, localName);
  }

  public static Node getNode(final Element element, final String nameSpace, final String localName)
      throws DOMStructureException {
    final NodeList nodeList = getNodes(element, nameSpace, localName);
    if (nodeList == null || nodeList.getLength() == 0) {
      return null;
    } else if (nodeList.getLength() > 1) {
      throw new DOMStructureException(element, "More than one \"" + localName + "\" element");
    } else {
      return nodeList.item(0);
    }
  }

  /**
   * Gets a {@link org.w3c.dom.NodeList} of all <code>Node</code>s that are
   * children of the given
   * {@link org.w3c.dom.Element} with the given <code>String</code> local name
   * by searching all available
   * namespaces.
   *
   * @param element
   * @param localName
   * @return
   */
  public static NodeList getNodes(final Element element, final String localName) {
    return getNodes(element, NAMESPACES, localName);
  }

  public static Node getNode(final Element element, final String localName) throws DOMStructureException {
    final NodeList nodeList = getNodes(element, localName);
    if (nodeList == null || nodeList.getLength() == 0) {
      return null;
    } else if (nodeList.getLength() > 1) {
      throw new DOMStructureException(element, "More than one \"" + localName + "\" element");
    } else {
      return nodeList.item(0);
    }
  }

  public static String toString(final Document document) throws DOMStructureException {
    try {
      final TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", new Integer(4));
      final Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      final Source source = new DOMSource(document);
      final StringWriter stringOut = new StringWriter();
      final Result result = new StreamResult(stringOut);

      transformer.transform(source, result);
      return stringOut.toString();
    } catch (final Exception ex) {
      throw new DOMStructureException(document, "Exception converting Document to a String", ex);
    }
  }

  public static boolean repairIdentifierAttribute(final Element element, final String attributeName,
      final Identifier identifierDefault, final Log logger) throws DOMStructureException {
    Identifier identifier = getIdentifierAttribute(element, attributeName);
    if (identifier == null) {
      if (identifierDefault != null) {
        identifier = identifierDefault;
      } else {
        identifier = IdentifierImpl.gensym("urn:" + attributeName.toLowerCase());
      }
      logger.warn("Setting missing " + attributeName + " attribute to " + identifier.stringValue());
      element.setAttribute(attributeName, identifier.stringValue());
      return true;
    }
    return false;
  }

  public static boolean repairIdentifierAttribute(final Element element, final String attributeName,
      final Log logger) throws DOMStructureException {
    return repairIdentifierAttribute(element, attributeName, null, logger);
  }

  public static boolean repairIdentifierContent(final Element element, final Log logger)
      throws DOMStructureException {
    Identifier identifier = getIdentifierContent(element);
    if (identifier == null) {
      identifier = IdentifierImpl.gensym();
      logger.warn("Setting missing content to " + identifier.stringValue());
      element.setTextContent(identifier.stringValue());
      return true;
    }
    return false;
  }

  public static boolean repairBooleanAttribute(final Element element, final String attributeName,
      final boolean bvalue, final Log logger) throws DOMStructureException {
    Boolean booleanValue = null;
    try {
      booleanValue = getBooleanAttribute(element, attributeName);
    } catch (final DOMStructureException ex) {
      logger.warn("Setting invalid " + attributeName + " attribute to " + bvalue);
      element.setAttribute(attributeName, Boolean.toString(bvalue));
      return true;
    }
    if (booleanValue == null) {
      logger.warn("Setting missing " + attributeName + " attribute to " + bvalue);
      element.setAttribute(attributeName, Boolean.toString(bvalue));
      return true;
    }
    return false;
  }

  public static boolean repairVersionMatchAttribute(final Element element, final String attributeName,
      final Log logger) {
    final String versionString = getStringAttribute(element, attributeName);
    if (versionString == null) {
      return false;
    }

    try {
      StdVersionMatch.newInstance(versionString);
    } catch (final ParseException ex) {
      logger.warn("Deleting invalid " + attributeName + " string " + versionString, ex);
      element.removeAttribute(attributeName);
      return true;
    }

    return false;
  }

  public static boolean repairVersionAttribute(final Element element, final String attributeName,
      final Log logger) {
    final String versionString = getStringAttribute(element, attributeName);
    if (versionString == null) {
      logger.warn("Adding default " + attributeName + " string 1.0");
      element.setAttribute(attributeName, "1.0");
      return true;
    }

    try {
      StdVersion.newInstance(versionString);
    } catch (final ParseException ex) {
      logger.warn("Setting invalid " + attributeName + " string " + versionString + " to 1.0", ex);
      element.setAttribute(attributeName, "1.0");
      return true;
    }

    return false;
  }

  public static boolean repairStringAttribute(final Element element, final String attributeName,
      String defaultValue, final Log logger) {
    final String attributeValue = getStringAttribute(element, attributeName);
    if (attributeValue == null) {
      if (defaultValue == null) {
        defaultValue = IdentifierImpl.gensym().stringValue();
      }
      logger.warn("Setting missing " + attributeName + " attribute to " + defaultValue);
      element.setAttribute(attributeName, defaultValue);
      return true;
    }
    return false;
  }

  public static DocumentBuilder getDocumentBuilder() throws DOMStructureException {
    /*
     * Get the DocumentBuilderFactory
     */
    final DocumentBuilderFactory documentBuilderFactory = getDOMBuilderFactory();

    /*
     * Get the DocumentBuilder
     */
    try {
      return documentBuilderFactory.newDocumentBuilder();
    } catch (final Exception ex) {
      throw new DOMStructureException("Exception creating DocumentBuilder: " + ex.getMessage(), ex);
    }
  }

  private static DocumentBuilderFactory getDOMBuilderFactory() throws DOMStructureException {
    if (documentBuilderFactory != null) {
      return documentBuilderFactory;
    }
    documentBuilderFactory = DocumentBuilderFactory.newInstance();
    if (documentBuilderFactory == null) {
      throw new DOMStructureException("No XML DocumentBuilderFactory configured");
    }
    documentBuilderFactory.setNamespaceAware(true);
    return documentBuilderFactory;
  }

  public static Document loadDocument(final File fileDocument) throws DOMStructureException {
    final DocumentBuilder documentBuilder = getDocumentBuilder();

    /*
     * Parse the XML file
     */
    Document document = null;
    try {
      document = documentBuilder.parse(fileDocument);
      if (document == null) {
        throw new DOMStructureException("Null document returned");
      }
    } catch (final Exception ex) {
      throw new DOMStructureException(
          "Exception loading file \"" + fileDocument.getAbsolutePath() + "\": " + ex.getMessage(), ex);
    }
    return document;

  }

  public static Document loadDocument(final InputStream inputStreamDocument) throws DOMStructureException {
    final DocumentBuilder documentBuilder = getDocumentBuilder();

    /*
     * Parse the XML file
     */
    Document document = null;
    try {
      document = documentBuilder.parse(inputStreamDocument);
      if (document == null) {
        throw new DOMStructureException("Null document returned");
      }
    } catch (final Exception ex) {
      throw new DOMStructureException("Exception loading file from stream: " + ex.getMessage(), ex);
    }
    return document;

  }
}
