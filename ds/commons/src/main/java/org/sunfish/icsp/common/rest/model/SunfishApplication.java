package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by dziegler on 21/10/2016.
 */
public class SunfishApplication {

    @JsonProperty("id")
    private String id;

    @JsonProperty("host")
    private String host;

    @JsonProperty("host")
    private String zone;

    public SunfishApplication() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
