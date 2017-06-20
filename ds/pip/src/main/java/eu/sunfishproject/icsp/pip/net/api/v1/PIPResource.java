package eu.sunfishproject.icsp.pip.net.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.swagger.Documentation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Api
@Path(SunfishServices.PIP_VERSION)
public class PIPResource {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    @ApiOperation(value="hello", hidden = true)
    public String get() {
        return "HELLO";
    }


    @Path(SunfishServices.PIP_PATH_REQUEST)
    public PIPRequestResource request(@ApiParam(value = Documentation.PIP_HEADER_ISSUER, required = true)
                                      @HeaderParam(SunfishServices.HEADER_ISSUER) String issuer,
                                      @ApiParam(hidden = true)
                                      @Context HttpServletResponse servletResponse) {


        return new PIPRequestResource(issuer, servletResponse);

    }


    @Path(SunfishServices.PIP_PATH_COLLECT)
    public PIPCollectResource collect(@ApiParam(value = Documentation.PIP_HEADER_ISSUER, required = true)
                                      @HeaderParam(SunfishServices.HEADER_ISSUER) String issuer,
                                      @ApiParam(hidden = true)
                                      @Context HttpServletResponse servletResponse) {


        return new PIPCollectResource(issuer, servletResponse);

    }

}
