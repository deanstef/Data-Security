package org.sunfish.icsp.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class MultivaluedMapSerializer extends StdSerializer<MultivaluedMap> {

    public MultivaluedMapSerializer() {
        this(null);
    }

    public MultivaluedMapSerializer(Class<MultivaluedMap> t) {
        super(t);
    }

    @Override
    public void serialize(MultivaluedMap map, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();


        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            List<List<String>> values = (List)map.get(key);

            for(List<String> valueList : values) {
                for(String value : valueList) {
                    jgen.writeStringField(key, value);
                }
            }
        }


        jgen.writeEndObject();
    }
}