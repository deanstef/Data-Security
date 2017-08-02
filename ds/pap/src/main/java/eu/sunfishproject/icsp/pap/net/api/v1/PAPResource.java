package eu.sunfishproject.icsp.pap.net.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.SunfishService;
import org.sunfish.icsp.common.swagger.Documentation;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
@Api
@Path(SunfishServices.PAP_VERSION)
public class PAPResource {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    @ApiOperation(value="hello", hidden = true)
    public String get() {
        return "HELLO PAP";
    }



    @Path(SunfishServices.PAP_PATH_POLICIES)
    public PAPPolicyResource policies(@ApiParam(value = Documentation.PAP_HEADER_ISSUER, required = true)
                                      @HeaderParam(SunfishServices.HEADER_ISSUER)
                                      final String issuer) {
        return new PAPPolicyResource(issuer);
    }




}
