package org.sunfish.icsp.common.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.SunfishObligationExpressionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.XACML3;
import org.apache.openaz.xacml.pdp.policy.PolicyDef;
import org.apache.openaz.xacml.pdp.policy.dom.DOMPolicyDef;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.apache.openaz.xacml.std.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySetType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;

public class XACMLIO {

  public static class XACMLIOException extends Exception {
    public XACMLIOException(final Exception e) {
      super(e);
    }
  }

  public static abstract class ResourcePool<T> {
    protected final BlockingQueue<T> pool;
    protected final ReentrantLock    lock           = new ReentrantLock();
    private int                      createdObjects = 0;
    private final int                size;

    protected ResourcePool(final int size) throws InterruptedException {
      this(size, false);
    }

    protected ResourcePool(final int size, final Boolean dynamicCreation) throws InterruptedException {
      // Enable the fairness; otherwise, some threads
      // may wait forever.
      pool = new ArrayBlockingQueue<>(size, true);
      this.size = size;
      if (!dynamicCreation) {
        for (int i = 0; i < size; ++i) {
          pool.put(createObject());
        }
        lock.lock();
      }
    }

    public T acquire() throws Exception {
      if (!lock.isLocked()) {
        if (lock.tryLock()) {
          try {
            ++createdObjects;
            return createObject();
          } finally {
            if (createdObjects < size) {
              lock.unlock();
            }
          }
        }
      }
      return pool.take();
    }

    public void recycle(final T resource) {
      // Will throws Exception when the queue is full,
      // but it should never happen.
      pool.add(resource);
    }

    public void createPool() {
      if (lock.isLocked()) {
        for (int i = 0; i < size; ++i) {
          pool.add(createObject());
          createdObjects++;
        }
      }
    }

    protected abstract T createObject();
  }

  public static class PolicyReaderPool extends ResourcePool<PolicyReader> {

    public PolicyReaderPool(final int size, final Boolean dynamicCreation) throws InterruptedException {
      super(size, dynamicCreation);
    }

    @Override
    protected PolicyReader createObject() {
      return new PolicyReader();
    }

  }

  public static class GenericReaderPool<T> extends ResourcePool<XACMLReader<T>> {

    private final Class<T> type;

    public GenericReaderPool(final int size, final boolean dynamicCreation, final Class<T> type)
        throws InterruptedException {
      super(size, true);
      log.info("Creating reader pool for{}", type);
      this.type = type;
      if (!dynamicCreation) {
        for (int i = 0; i < size; ++i) {
          pool.put(createObject());
        }
        lock.lock();
      }
    }

    @Override
    protected XACMLReader<T> createObject() {
      return new XACMLReader<>(type);
    }

  }

  public static class GenericWriterPool<T> extends ResourcePool<XACMLWriter<T>> {

    private final Class<T> type;

    public GenericWriterPool(final int size, final boolean dynamicCreation, final Class<T> type)
        throws InterruptedException {
      super(size, true);
      log.info("Creating reader pool for{}", type);
      this.type = type;
      if (!dynamicCreation) {
        for (int i = 0; i < size; ++i) {
          pool.put(createObject());
        }
        lock.lock();
      }
    }

    @Override
    protected XACMLWriter<T> createObject() {
      return new XACMLWriter<>(type);
    }

  }

  private static Logger log = LogManager.getLogger(XACMLIO.class);

  public static class XACMLReader<T> {
    private JAXBContext            context;
    private Unmarshaller           unmarshaller;
    private JAXBException          initException;
    private DocumentBuilderFactory documentBuilderFactory;
    private Class<T>               type;

    private XACMLReader(final Class<T> type) {
      try {
        this.type = type;
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        context = JAXBContext.newInstance(type);
        unmarshaller = context.createUnmarshaller();
        initException = null;

      } catch (final JAXBException e) {
        initException = e;
        log.error("Unmarshallers could not be instantiated! Policies and PolicySets will not be loadable", e);
      }
    }

    public JAXBElement<T> read(final InputStream is) throws XACMLIOException {
      try {
        final DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        final Document doc = db.parse(is);
        if (initException != null) {
          throw initException;
        }
        return unmarshaller.unmarshal(doc, type);
      } catch (final Exception e) {
        throw new XACMLIOException(e);
      }
    }
  }

  public static class XACMLWriter<T> {
    private JAXBContext            context;
    private Marshaller             marshaller;
    private JAXBException          initException;
    private DocumentBuilderFactory documentBuilderFactory;

    public XACMLWriter(final Class<T> type) {
      try {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        context = JAXBContext.newInstance(type);
        marshaller = context.createMarshaller();
        initException = null;

      } catch (final JAXBException e) {
        initException = e;
        log.error("Unmarshallers could not be instantiated! Policies and PolicySets will not be loadable", e);
      }
    }

    public void write(final OutputStream os, final T policy) throws XACMLIOException {
      try {
        if (initException != null) {
          throw initException;
        }
        marshaller.marshal(policy, os);
      } catch (final Exception e) {
        throw new XACMLIOException(e);
      }
    }

    public void write(final OutputStream os, final JAXBElement<T> policy) throws XACMLIOException {
      try {
        if (initException != null) {
          throw initException;
        }
        marshaller.marshal(policy, os);
      } catch (final Exception e) {
        throw new XACMLIOException(e);
      }
    }
  }

  public static class PolicyReader {
    private JAXBContext            policyContext;
    private Unmarshaller           policyUnmarshaller;
    private JAXBContext            setContext;
    private Unmarshaller           setUnmarshaller;
    private JAXBException          initException;
    private DocumentBuilderFactory documentBuilderFactory;

    private PolicyReader() {
      try {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        policyContext = JAXBContext.newInstance(PolicyType.class, SunfishObligationExpressionType.class);
        policyUnmarshaller = policyContext.createUnmarshaller();
        setContext = JAXBContext.newInstance(PolicySetType.class, SunfishObligationExpressionType.class);
        setUnmarshaller = setContext.createUnmarshaller();
        initException = null;
      } catch (final JAXBException e) {
        initException = e;
        log.error("Unmarshallers could not be instantiated! Policies and PolicySets will not be loadable", e);
      }
    }

    public PolicyDef readDef(final InputStream in) throws DOMStructureException {
      /*
       * Parse the XML file
       */
      PolicyDef policyDef = null;
      try {
        final Document document = documentBuilderFactory.newDocumentBuilder()
            .parse(in);
        if (document == null) {
          throw new DOMStructureException("Null document returned");
        }
        policyDef = DOMPolicyDef.newInstance(document, null);
      } catch (final Exception ex) {
        throw new DOMStructureException(ex);
      }
      return policyDef;
    }

    public List<PolicyDef> readDefList(final InputStream in) throws DOMStructureException {
      try {
        final Document document = documentBuilderFactory.newDocumentBuilder().parse(in);
        if (document == null) {
          throw new DOMStructureException("Null document returned");
        }
        final Node rootNode = DOMUtil.getFirstChildElement(document);
        if (rootNode == null) {
          throw new DOMStructureException("No child in document");
        }
        if (DOMUtil.isInNamespace(rootNode, XACML3.XMLNS)) {
          throw new DOMStructureException("Invalid Namespace: " + rootNode.getNamespaceURI());
        }
        // if (!"ns2:PolicyListPolicySetList".equals(rootNode.getNodeName())) {
        // throw new DOMStructureException(
        // "Invalid Type: " + rootNode.getNodeName() + ", expected:
        // ns2:PolicyListPolicySetList");
        // }
        final List<PolicyDef> defList = new LinkedList<>();
        for (int i = 0; i < rootNode.getChildNodes().getLength(); ++i) {
          defList.add(DOMPolicyDef.readPolicyDef(rootNode.getChildNodes().item(i), null));
        }
        return defList;
      } catch (final Exception ex) {
        throw new DOMStructureException("Exception parsing Policy: " + ex.getMessage(), ex);
      }
    }

    public Object readPolicy(final InputStream is) throws XACMLIOException {
      try {
        //
        // Parse the policy file
        //
        final DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        final Document doc = db.parse(is);
        if (initException != null) {
          throw initException;
        }
        //
        // Because there is no root defined in xacml,
        // find the first element
        //
        final NodeList nodes = doc.getChildNodes();
        Node node = nodes.item(0);
        if (node.getNodeType() == 8) {
          node = nodes.item(1);
        }
        Element e = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          e = (Element) node;
          //
          // Is it a 3.0 policy?
          //
          if (e.getNamespaceURI().equals("urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
            //
            // A policyset or policy could be the root
            //
            if (e.getNodeName().endsWith("Policy")) {
              final JAXBElement<PolicyType> root = policyUnmarshaller.unmarshal(e, PolicyType.class);
              //
              // Here is our policy set class
              //
              return root.getValue();
            } else if (e.getNodeName().endsWith("PolicySet")) {
              final JAXBElement<PolicySetType> root = setUnmarshaller.unmarshal(e, PolicySetType.class);
              //
              // Here is our policy set class
              //
              return root.getValue();
            } else {
              if (log.isDebugEnabled()) {
                log.debug("Not supported yet: " + e.getNodeName());
              }
            }
          } else {
            log.warn("unsupported namespace: " + e.getNamespaceURI());
          }
        } else {
          if (log.isDebugEnabled()) {
            log.debug("No root element contained in policy " + " Name: " + node.getNodeName() + " type: "
                + node.getNodeType() + " Value: " + node.getNodeValue());
          }
        }
        throw new JAXBException("Unsupported datatype");
      } catch (final Exception e) {
        throw new XACMLIOException(e);
      }
    }
  }
}
