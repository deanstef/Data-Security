package org.sunfish.icsp.common.rest.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */

// Adapted from http://blog.dejavu.sk/2014/02/11/inject-custom-java-types-via-jax-rs-parameter-annotations/

@Provider
public class JacksonJsonParamConverterProvider implements ParamConverterProvider {


    @Context
    private Providers providers;

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {



        final MessageBodyReader<T> messageBodyReader = providers.getMessageBodyReader(
                                                        rawType, genericType, annotations,
                                                        MediaType.APPLICATION_JSON_TYPE);


        if (messageBodyReader == null ||
            !messageBodyReader.isReadable(rawType, genericType, annotations, MediaType.APPLICATION_JSON_TYPE)||
            genericType == String.class) {
            return null;
        }



        final ContextResolver<ObjectMapper> contextResolver = providers.getContextResolver(
                                                                ObjectMapper.class,
                                                                MediaType.APPLICATION_JSON_TYPE);


        final ObjectMapper mapper = contextResolver != null ? contextResolver.getContext(rawType) : new ObjectMapper();


        return new ParamConverter<T>() {

            @Override
            public T fromString(final String value) {
                if(value == null) {
                    return null;
                }

                try {
                    return mapper.readerFor(rawType).readValue(value);
                } catch(IOException e) {
                    throw new ProcessingException(e);
                }

            }

            @Override
            public String toString(final T value) {
                try {
                    return mapper.writer().writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new ProcessingException(e);
                }
            }


        };
    }

}
