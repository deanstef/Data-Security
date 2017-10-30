package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.json.MultivaluedMapSerializer;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishRequestData {

    private static final Logger log = LogManager.getLogger(SunfishRequestData.class);


    @JsonProperty("query-string")
    private String queryString;

    @JsonProperty("headers")
    private HashMap<String, String> headers;

    public SunfishRequestData() {
    }


    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(final String queryString) {
        this.queryString = queryString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @JsonIgnore
    public MultivaluedMap<String, Object> getMultivaluedHeaders() {
        return new MultivaluedHashMap<String, Object>(this.headers);
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = new HashMap(headers);
    }


    public String toJson() {

        final StringBuilder builder = new StringBuilder();

        builder.append("{");

        builder.append("\"headers\":");

        final ObjectMapper mapper = new ObjectMapper();




        try {

            if(headers instanceof MultivaluedMap) {
                final SimpleModule module = new SimpleModule();
                module.addSerializer(MultivaluedMap.class, new MultivaluedMapSerializer());
                mapper.registerModule(module);
                builder.append(mapper.writeValueAsString(getMultivaluedHeaders()));

            } else {
                builder.append(mapper.writeValueAsString(this.headers));
            }


        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }


        if(queryString != null && !queryString.isEmpty()) {
            builder.append(",\"query-string\":\"?"+queryString+"\"");
        }


        builder.append("");

        builder.append("}");


        return builder.toString();


    }


    public Map<String, String> getQueryParams() {

        String query = this.queryString;
        if(query == null) {
            query = "";
        }

        if(query.startsWith("?")) {
            query = query.replaceFirst("\\?", "");
        }


        final Map<String, String> queryParams = new LinkedHashMap<>();

        try {
            final String[] pairs = query.split("&");
            for (final String pair : pairs) {
                final int idx = pair.indexOf("=");
                if(idx!=-1) {
                  queryParams.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                }
            }
        } catch(final Exception e) {
            log.error("Could not retrieve query params:" , e);
        }
        return queryParams;
    }

    public Map<String, String> getCookies() {


        String rawCookie = headers==null?null:this.headers.get("Cookie");

        if(rawCookie == null) {
            rawCookie = "";
        }

        final String[] rawCookies = rawCookie.split(";");

        final Map<String, String> cookies = new HashMap<>();

        for(final String cookie : rawCookies) {
            final String[] pair = cookie.split("=");
            if(pair.length == 2) {
                final String name = pair[0];
                final String value = pair[1];

                cookies.put(name, value);
            }

        }

        return cookies;

    }




}
