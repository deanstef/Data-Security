package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishRequestParameters {


    @JsonProperty("data-tags")
    private String dataTags;

    @JsonProperty("masking-context")
    private String maskingContext;

    public SunfishRequestParameters() {

    }

    public String getDataTags() {
        return dataTags;
    }

    public void setDataTags(String dataTags) {
        this.dataTags = dataTags;
    }

    public String getMaskingContext() {
        return maskingContext;
    }

    public void setMaskingContext(String maskingContext) {
        this.maskingContext = maskingContext;
    }
}
