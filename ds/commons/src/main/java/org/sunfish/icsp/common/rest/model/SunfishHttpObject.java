package org.sunfish.icsp.common.rest.model;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import java.util.List;
import java.util.Map;

/**
 * Created by dziegler on 17/10/2016.
 */
public class SunfishHttpObject {


    private int responseCode;
    private MultivaluedMap<String, Object> headers;
    private Map<String, NewCookie> cookies;
    private String queryString;
    private byte[] body;

    public SunfishHttpObject() {

    }


    public SunfishHttpObject(int responseCode, MultivaluedMap<String, Object> headers, Map<String, NewCookie> cookies, String queryString, byte[] body) {
        this.responseCode = responseCode;
        this.headers = headers;
        this.cookies = cookies;
        this.queryString = queryString;
        this.body = body;
    }


    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, NewCookie> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, NewCookie> cookies) {
        this.cookies = cookies;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
