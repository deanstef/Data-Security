package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishAudience {

    @JsonProperty("target-zone")
    private String targetZone;

    @JsonProperty("target-service")
    private String targetService;

    public SunfishAudience() {

    }


    public String getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(String targetZone) {
        this.targetZone = targetZone;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }
}
