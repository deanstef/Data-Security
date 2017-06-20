package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishValidity {

    @JsonProperty("not-valid-before")
    private Date notValidBefore;

    @JsonProperty("not-valid-after")
    private Date notValidAfter;


    public SunfishValidity() {

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
