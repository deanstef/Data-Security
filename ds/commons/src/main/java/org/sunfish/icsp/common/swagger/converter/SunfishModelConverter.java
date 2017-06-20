package org.sunfish.icsp.common.swagger.converter;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishModelConverter implements ModelConverter {



    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {


        if(isCustomType(type)) {
            return context.resolveProperty(String.class, annotations);
        }

        if (chain.hasNext()) {
            return chain.next().resolveProperty(type, context, annotations, chain);
        } else {
            return null;
        }
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        if(isCustomType(type)) {
            return chain.next().resolve(String.class, context, chain);
        }


        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        } else {
            return null;
        }
    }


    private boolean isCustomType(Type type) {


        JavaType _type = Json.mapper().constructType(type);
        if (_type != null) {
            Class<?> cls = _type.getRawClass();

            if(cls!= null) {

                Package pck = cls.getPackage();
                if(pck!=null) {
                    String pack = cls.getPackage().getName();

                    if (pack.startsWith("oasis.names.tc.xacml._3_0.core.schema.wd_17") || pack.startsWith("org.apache.openaz.xacml") ||
                        pack.startsWith("eu.sunfishproject.icsp.prp.api.v1") || pack.startsWith("org.sunfish.icsp.common.api.pip") ||
                        pack.startsWith("eu.sunfishproject.icsp.pip.net.api.v1") || pack.startsWith("eu.sunfishproject.icsp.pap.net.api.v1")) {
                        return true;
                    }
                }
            }
        }

        return false;

    }



}
