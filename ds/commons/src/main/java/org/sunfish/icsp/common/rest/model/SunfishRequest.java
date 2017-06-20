package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishRequest {

    @JsonProperty("method")
    private String method;

    @JsonProperty("host")
    private String host;

    @JsonProperty("path")
    private String path;

    @JsonProperty("port")
    private int port;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("content-type")
    private String contentType;

    @JsonProperty("header-parameters")
    private List headerParameters;

    @JsonProperty("url-parameters")
    private String urlParameters;

    @JsonProperty("body-data")
    private String bodyData;

    @JsonProperty("not-valid-before")
    private Date notValidBefore;

    @JsonProperty("not-valid-after")
    private Date notValidAfter;


    public SunfishRequest() {

    }

    @JsonIgnore
    public boolean isValid() {
        return method != null && path != null && port > 0 && protocol != null;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public List<String> getHeaderParameters() {
        return headerParameters;
    }

    public void setHeaderParameters(final List<String> headerParameters) {
        this.headerParameters = headerParameters;
    }

    public String getUrlParameters() {
        return urlParameters;
    }

    public void setUrlParameters(final String urlParameters) {
        this.urlParameters = urlParameters;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(final String bodyData) {
        this.bodyData = bodyData;
    }

    public Date getNotValidBefore() {
        return notValidBefore;
    }

    public void setNotValidBefore(final Date notValidBefore) {
        this.notValidBefore = notValidBefore;
    }

    public Date getNotValidAfter() {
        return notValidAfter;
    }

    public void setNotValidAfter(final Date notValidAfter) {
        this.notValidAfter = notValidAfter;
    }



}
