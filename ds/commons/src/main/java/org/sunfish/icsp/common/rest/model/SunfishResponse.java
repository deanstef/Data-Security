package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishResponse {

    @JsonProperty("content-type")
    private String contentType;

    @JsonProperty("response-code")
    private int responseCode;

    @JsonProperty("header-parameters")
    private List headerParameters;

    @JsonProperty("enforced-action")
    private String enforcedAction;

    @JsonProperty("body-data")
    private String bodyData;

    @JsonProperty("not-valid-before")
    private Date notValidBefore;

    @JsonProperty("not-valid-after")
    private Date notValidAfter;


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public List getHeaderParameters() {
        return headerParameters;
    }

    public void setHeaderParameters(List headerParameters) {
        this.headerParameters = headerParameters;
    }

    public String getEnforcedAction() {
        return enforcedAction;
    }

    public void setEnforcedAction(String enforcedAction) {
        this.enforcedAction = enforcedAction;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData;
    }

    public Date getNotValidBefore() {
        return notValidBefore;
    }

    public void setNotValidBefore(Date notValidBefore) {
        this.notValidBefore = notValidBefore;
    }

    public Date getNotValidAfter() {
        return notValidAfter;
    }

    public void setNotValidAfter(Date notValidAfter) {
        this.notValidAfter = notValidAfter;
    }
}
