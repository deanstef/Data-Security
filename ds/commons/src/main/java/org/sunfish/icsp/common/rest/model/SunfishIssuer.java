package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishIssuer {

//    @JsonProperty("issuer-id")
//    private String issuerId;
//
//    @JsonProperty("host")
//    private String host;
//
//    @JsonProperty("source-zone")
//    private String sourceZone;
//
//    @JsonProperty("pep-id")
//    private String pepId;
//
//    // Not possible in JSON
    @JsonProperty("token")
    private String token;

    @JsonProperty("method")
    private String method;


    public SunfishIssuer() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }


    public String getMethod() {
        return method;
    }


    public void setMethod(final String method) {
        this.method = method;
    }


//    public String getIssuerId() {
//        return issuerId;
//    }
//
//    public void setIssuerId(final String issuerId) {
//        this.issuerId = issuerId;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(final String host) {
//        this.host = host;
//    }
//
//    public String getSourceZone() {
//        return sourceZone;
//    }
//
//    public void setSourceZone(final String sourceZone) {
//        this.sourceZone = sourceZone;
//    }
//
//    public String getPepId() {
//        return pepId;
//    }
//
//    public void setPepId(final String pepId) {
//        this.pepId = pepId;
//    }


}
