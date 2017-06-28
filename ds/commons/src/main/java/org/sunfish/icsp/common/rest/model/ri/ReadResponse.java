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
 * HTTP response for a successful read operation
 */
@ApiModel(description = "HTTP response for a successful read operation")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-02-23T08:20:44.040Z")
public class ReadResponse {
  private String policy = null;

  private String expirationTime = null;

  public ReadResponse policy(final String policy) {
    this.policy = policy;
    return this;
  }

   /**
   * BASE64-enocded data of the requested policy.
   * @return policy
  **/
  @ApiModelProperty(example = "null", required = true, value = "BASE64-enocded data of the requested policy.")
  public String getPolicy() {
    return policy;
  }

  public void setPolicy(final String policy) {
    this.policy = policy;
  }

  public ReadResponse expirationTime(final String expirationTime) {
    this.expirationTime = expirationTime;
    return this;
  }

   /**
   * Specifies the expiration time of the policy set in milliseconds starting from midnight, January 1, 1970 UTC. After this time the policy set must not be used.
   * @return expirationTime
  **/
  @ApiModelProperty(example = "null", required = true, value = "Specifies the expiration time of the policy set in milliseconds starting from midnight, January 1, 1970 UTC. After this time the policy set must not be used.")
  public String getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(final String expirationTime) {
    this.expirationTime = expirationTime;
  }


  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ReadResponse readResponse = (ReadResponse) o;
    return Objects.equals(this.policy, readResponse.policy) &&
        Objects.equals(this.expirationTime, readResponse.expirationTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policy, expirationTime);
  }


  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class ReadResponse {\n");

    sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
    sb.append("    expirationTime: ").append(toIndentedString(expirationTime)).append("\n");
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
