package org.sunfish.icsp.common.util;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.Request;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicyTypeReader;
import org.sunfish.icsp.common.rest.mappers.XACMLPolicyTypeWriter;
import org.sunfish.icsp.common.rest.mappers.XACMLRequestProvider;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class CommonUtil {

    private static Logger log = LogManager.getLogger(CommonUtil.class);

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }


    public static String xacmlRequestToString(Request request) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String xacml = null;
        try {
            XACMLRequestProvider.writeTo(request, baos, true);
            xacml = new String(baos.toByteArray());
        } catch(Exception e) {
            log.error("Could not write to output stream");
        }

        return xacml;

    }


    public static Map<String,String> convert(MultivaluedMap<String, Object> multivaluedMap) {

        Map<String,String> result = new HashMap<>();

        Iterator<String> it = multivaluedMap.keySet().iterator();


        while(it.hasNext()){
            String key = it.next();

            List<String> values = (List)multivaluedMap.get(key);


            result.put(key, values.get(0));


//            for(List<String> valueList : values) {
//                for(String value : valueList) {
//                    jgen.writeStringField(key, value);
//                }
//            }

            //parameters.put(key,multivaluedMap.getFirst(key));
        }

        return result;

    }

    public static MultivaluedMap<String, Object> convert(Map<String, String> mapToConvert) {
        MultivaluedMap<String, Object> multivaluedMap = new MultivaluedHashMap();


        Iterator<String> it = mapToConvert.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            multivaluedMap.putSingle(key, mapToConvert.get(key));
        }

        return multivaluedMap;

    }


    public static String policyTypeToString(PolicyType policyType) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XACMLPolicyTypeWriter writer = new XACMLPolicyTypeWriter();
            writer.writeTo(policyType, PolicyType.class, null, null, null, null, outputStream);
            String policyString = new String(outputStream.toByteArray(), "UTF-8");
            return policyString;
        } catch (Exception e) {
            return null;
        }

    }


    public static PolicyType stringToPolicyType(String policyTypeString) {

        try {

            InputStream inputStream = new ByteArrayInputStream(policyTypeString.getBytes(StandardCharsets.UTF_8));

            XACMLPolicyTypeReader reader = new XACMLPolicyTypeReader();
            PolicyType policyType = reader.readFrom(PolicyType.class, null, null, null, null, inputStream);

            return policyType;

        } catch (Exception e) {
            return null;
        }

    }


}
