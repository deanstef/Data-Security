/*
 * SUNFISH Blockchain API - Storage of access control policies
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 2.0.0
 * Contact: S.Ferdous@soton.ac.uk
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.sunfish.icsp.common.rest.model.ri;

import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * HTTP body defining the read operation for the /read endpoint
 */
@ApiModel(description = "HTTP body defining the read operation for the /read endpoint")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-23T08:20:44.040Z")
public class ServiceBody {
  private String requestorID = null;

  private String token       = null;

  private String serviceID   = null;

  private String policyType  = null;

  public ServiceBody() {
  }

  public ServiceBody(final String requestorID, final String token, final String serviceID, final String policyType) {
    this.requestorID = requestorID;
    this.token = token;
    this.serviceID = serviceID;
    this.policyType = policyType;
  }

  public ServiceBody requestorID(final String requestorID) {
    this.requestorID = requestorID;
    return this;
  }

  /**
   * Identifier of the requesting entity.
   *
   * @return requestorID
   **/
  @ApiModelProperty(example = "null", required = true, value = "Identifier of the requesting entity.")
  public String getRequestorID() {
    return requestorID;
  }

  public void setRequestorID(final String requestorID) {
    this.requestorID = requestorID;
  }

  public ServiceBody token(final String token) {
    this.token = token;
    return this;
  }

  /**
   * A crypto token to validate if the entity is allowed to perform the
   * requested operation.
   *
   * @return token
   **/
  @ApiModelProperty(example = "null", required = true, value = "A crypto token to validate if the entity is allowed to perform the requested operation.")
  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }

  public ServiceBody serviceID(final String serviceID) {
    this.serviceID = serviceID;
    return this;
  }

  /**
   * Specifies the service identifier to be used to retrieve the set of policies
   * belonging to that identifier.
   *
   * @return serviceID
   **/
  @ApiModelProperty(example = "null", required = true, value = "Specifies the service identifier to be used to retrieve the set of policies belonging to that identifier.")
  public String getServiceID() {
    return serviceID;
  }

  public void setServiceID(final String serviceID) {
    this.serviceID = serviceID;
  }

  public ServiceBody policyType(final String policyType) {
    this.policyType = policyType;
    return this;
  }

  /**
   * The type of the policy.
   *
   * @return policyType
   **/
  @ApiModelProperty(example = "null", required = true, value = "The type of the policy.")
  public String getPolicyType() {
    return policyType;
  }

  public void setPolicyType(final String policyType) {
    this.policyType = policyType;
  }

  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ServiceBody serviceBody = (ServiceBody) o;
    return Objects.equals(this.requestorID, serviceBody.requestorID)
        && Objects.equals(this.token, serviceBody.token)
        && Objects.equals(this.serviceID, serviceBody.serviceID)
        && Objects.equals(this.policyType, serviceBody.policyType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestorID, token, serviceID, policyType);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class ServiceBody {\n");

    sb.append("    requestorID: ").append(toIndentedString(requestorID)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    serviceID: ").append(toIndentedString(serviceID)).append("\n");
    sb.append("    policyType: ").append(toIndentedString(policyType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(final java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
