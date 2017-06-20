package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishResponseData {

    @JsonProperty("StatusCode")
    private int statusCode;

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("Headers")
    private Map<String, String> headers;

    public SunfishResponseData() {

    }

    public SunfishResponseData(int statusCode, String reason, Map<String, String> headers) {

        this.statusCode = statusCode;
        this.reason = reason;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
