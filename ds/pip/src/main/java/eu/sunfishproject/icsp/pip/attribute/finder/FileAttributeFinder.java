package eu.sunfishproject.icsp.pip.attribute.finder;

import eu.sunfishproject.icsp.pip.attribute.AttributeFinder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.pdp.policy.dom.DOMAttributeDesignator;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.apache.openaz.xacml.std.dom.DOMStructureException;
import org.apache.openaz.xacml.std.dom.DOMUtil;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

/**
 * Created by dziegler on 6/14/2016.
 */
public class FileAttributeFinder implements AttributeFinder {


    private final AttributeDesignatorSet attributeDesignators = new AttributeDesignatorSet();
    private static final Logger log = LogManager.getLogger(FileAttributeFinder.class);


    public FileAttributeFinder(final String directory) {
        this.loadAttributes(directory);
    }


    @Override
    public AttributeDesignatorSet getAttributes() {
        return this.attributeDesignators;
    }


    private void loadAttributes(final String directory) {

        File rootDirectory = new File(directory);
        log.debug("getting Attributes from {}", rootDirectory.getAbsolutePath());
        if (!rootDirectory.isAbsolute()) {
            final URL resourceUrl = this.getClass().getResource(directory);
            final String filePath = resourceUrl.getPath();
            rootDirectory = new File(filePath);
        }

        final String[] extensions = {"xml"};

        final Collection<File> files = FileUtils.listFiles(rootDirectory, extensions, false);
        for (final File file : files) {

            final AttributeDesignator attribute = loadAttributeFromFile(file);
            if (attribute != null) {
                this.attributeDesignators.add(attribute);
            }
        }

    }


    private AttributeDesignator loadAttributeFromFile(final File file) {

        AttributeDesignator attribute = null;

        try {
            final InputStream is = new FileInputStream(file);
            final Document ex = DOMUtil.loadDocument(is);
            if (ex == null) {
                throw new DOMStructureException("Null document returned");
            } else {
                final Element rootNode = DOMUtil.getFirstChildElement(ex);
                if (rootNode == null) {
                    throw new DOMStructureException("No child in document");
                } else if (DOMUtil.isInNamespace(rootNode, "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17")) {
                    if ("AttributeDesignator".equals(rootNode.getLocalName())) {
                        attribute = DOMAttributeDesignator.newInstance(rootNode);
                    }
                }

            }

        } catch (final Exception e) {
            log.error("Could not read attribute from file", e);
        }

        return attribute;
    }

}


