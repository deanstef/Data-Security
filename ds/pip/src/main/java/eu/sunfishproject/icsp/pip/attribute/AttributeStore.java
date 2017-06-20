package eu.sunfishproject.icsp.pip.attribute;

import eu.sunfishproject.icsp.pip.attribute.finder.FileAttributeFinder;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorSet;
import org.sunfish.icsp.common.util.Config;

import java.io.File;

/**
 * Created by dziegler on 6/14/2016.
 */
public class AttributeStore {


    public static final String ATTRIBUTE_DIR = Config.CONF_DIR + "pip"+File.separator+"attributes";
//    public static final String ATTRIBUTE_DIR =  "/attributes";

    private final AttributeFinder attributeFinder = new FileAttributeFinder(ATTRIBUTE_DIR);
    private static AttributeStore instance;


    private AttributeStore() {

    }


    public static AttributeStore getInstance() {
        if (instance == null) {
            instance = new AttributeStore();
        }
        return instance;
    }

    public AttributeDesignatorSet getAttributes() {
        return this.attributeFinder.getAttributes();
    }


}
