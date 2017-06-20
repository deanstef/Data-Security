package org.sunfish.icsp.common.api.pip;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishPIPRequest {

  private Request                               request;
  private final Collection<AttributeDesignator> attributeDesignators;

  public SunfishPIPRequest() {
    this.attributeDesignators = new TreeSet<>(new AttributeDesignatorComparator());
  }

  public SunfishPIPRequest(final Request request,
      final Collection<AttributeDesignator> attributeDesignators) {
    this.request = request;
    this.attributeDesignators = new TreeSet<>(new AttributeDesignatorComparator());
    this.attributeDesignators.addAll(attributeDesignators);
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(final Request request) {
    this.request = request;
  }

  public Collection<AttributeDesignator> getAttributeDesignators() {
    return attributeDesignators;
  }

  public void setAttributeDesignators(final Collection<AttributeDesignator> attributeDesignators) {
    this.attributeDesignators.addAll(attributeDesignators);
  }

  public void add(final AttributeDesignator attributeDesignator) {
    this.attributeDesignators.add(attributeDesignator);
  }
}
