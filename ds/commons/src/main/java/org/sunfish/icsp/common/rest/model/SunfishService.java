package org.sunfish.icsp.common.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.sunfish.icsp.common.swagger.Documentation;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@ApiModel(value = "SunfishService", description = Documentation.HEADER_SERVICE)
public class SunfishService {

    @JsonProperty("id")
    private String id;

    @JsonProperty("host")
    private String host;

    @JsonProperty("zone")
    private String zone;

    public SunfishService() {

    }


    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(final String zone) {
        this.zone = zone;
    }
}
