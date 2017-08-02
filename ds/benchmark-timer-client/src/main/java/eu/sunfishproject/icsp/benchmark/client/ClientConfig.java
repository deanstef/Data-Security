package eu.sunfishproject.icsp.benchmark.client;

import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class ClientConfig {

    public static final String ACCEPTED_TYPE = MediaType.APPLICATION_XML;
    public static final String SRV_PROTO     = "http";
    public static final String SRV_PATH      = "/resource/id/";
    public static final int    SRV_PORT      = 8080;
    public static final String ENCODING      = "gzip";


}
