package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dziegler on 6/13/2016.
 */
public class SunfishEndpoint {


    @JsonProperty("method")
    private String method;

    @JsonProperty("host")
    private String host;

    @JsonProperty("path")
    private String path;

    @JsonProperty("secured")
    private boolean secured;

    @JsonProperty("content-type")
    private String contentType;



    public SunfishEndpoint() {

    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
