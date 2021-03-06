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
public class ReadBody {
  private String requestorID = null;

  private String token       = null;

  private String dataId = null;

  public ReadBody() {

  }

  public ReadBody(final String requestorID, final String token, final String dataId) {
    this.requestorID = requestorID;
    this.token = token;
    this.dataId = dataId;
  }

  public ReadBody requestorID(final String requestorID) {
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

  public ReadBody token(final String token) {
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

  public ReadBody id(final String id) {
    this.dataId = id;
    return this;
  }

  /**
   * The identifer for the policy.
   *
   * @return dataId
   **/
  @ApiModelProperty(example = "null", required = true, value = "The identifer for the policy.")
  public String getDataId() {
    return dataId;
  }

  public void setDataId(final String dataId) {
    this.dataId = dataId;
  }

  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ReadBody readBody = (ReadBody) o;
    return Objects.equals(this.requestorID, readBody.requestorID)
        && Objects.equals(this.token, readBody.token) && Objects.equals(this.dataId, readBody.dataId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestorID, token, dataId);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class ReadBody {\n");

    sb.append("    requestorID: ").append(toIndentedString(requestorID)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    dataId: ").append(toIndentedString(dataId)).append("\n");
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
