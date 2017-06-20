package eu.sunfishproject.icsp.pip.net.api.v1;

import eu.sunfishproject.icsp.pip.attribute.AttributeProvider;
import eu.sunfishproject.icsp.pip.attribute.AttributeProviderStore;
import eu.sunfishproject.icsp.pip.attribute.AttributeStore;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.sunfish.icsp.common.api.pip.AttributeDesignatorSet;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.rest.ExtendedMediaType;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.swagger.Documentation;
import org.sunfish.icsp.common.xacml.XACMLRequestBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class PIPCollectResource {

    private static final Logger log = LogManager.getLogger(PIPRequestResource.class);


    private final String issuer;
    private final HttpServletResponse servletResponse;

    public PIPCollectResource(String issuer, HttpServletResponse servletResponse) {
        this.issuer = issuer;
        this.servletResponse = servletResponse;
    }

    @GET
    @Produces(ExtendedMediaType.APPLICATION_XML_XACML)
    @ApiOperation(value = Documentation.PIP_COLLECT, nickname = "collect")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Documentation.PIP_COLLECT_200, responseHeaders = @ResponseHeader(name = SunfishServices.HEADER_ISSUER, description = Documentation.PIP_HEADER_ISSUER, response = String.class), response = String.class),
            @ApiResponse(code = 400, message = Documentation.PIP_COLLECT_400),
            @ApiResponse(code = 403, message = Documentation.PIP_COLLECT_403)
    })
    public AttributeDesignatorSet collect() {
        return AttributeStore.getInstance().getAttributes();
    }


}
