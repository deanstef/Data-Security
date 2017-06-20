package eu.sunfishproject.icsp.pdp.model;

import io.swagger.annotations.ApiModelProperty;
import org.sunfish.icsp.common.swagger.Documentation;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class VerifyPolicyResult {


    private String status;
    private String description;
    private int statusCode;


    public VerifyPolicyResult(String status, String description, int statusCode) {
        this.status = status;
        this.description = description;
        this.statusCode = statusCode;
    }

    @ApiModelProperty(value = Documentation.PDP_VERIFY_POLICY_RESULT_STATUS, allowableValues = "success,error")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ApiModelProperty(value = Documentation.PDP_VERIFY_POLICY_RESULT_DESCRIPTION)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = Documentation.PDP_VERIFY_POLICY_RESULT_STATUS_CODE)
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
