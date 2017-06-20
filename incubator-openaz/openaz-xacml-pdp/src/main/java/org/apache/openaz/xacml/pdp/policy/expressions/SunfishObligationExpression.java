package org.apache.openaz.xacml.pdp.policy.expressions;//

import org.apache.openaz.xacml.api.AttributeValue;
import org.apache.openaz.xacml.api.XACML;
import org.apache.openaz.xacml.pdp.eval.EvaluationContext;
import org.apache.openaz.xacml.pdp.eval.EvaluationException;
import org.apache.openaz.xacml.pdp.policy.Bag;
import org.apache.openaz.xacml.pdp.policy.Expression;
import org.apache.openaz.xacml.pdp.policy.ExpressionResult;
import org.apache.openaz.xacml.pdp.policy.PolicyDefaults;
import org.apache.openaz.xacml.std.IdentifierImpl;
import org.apache.openaz.xacml.std.StdAttributeValue;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SunfishObligationExpression extends Expression {

  protected String obligationId;
  private Map<QName, String> otherAttributes = new HashMap<QName, String>();

  /**
   * Gets the value of the obligationId property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getObligationId() {
    return obligationId;
  }

  /**
   * Sets the value of the obligationId property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setObligationId(String value) {
    this.obligationId = value;
  }

  /**
   * Gets a map that contains attributes that aren't bound to any typed property on this class.
   *
   * <p>
   * the map is keyed by the name of the attribute and
   * the value is the string value of the attribute.
   *
   * the map returned by this method is live, and you can add new attribute
   * by updating the map directly. Because of this design, there's no setter.
   *
   *
   * @return
   *     always non-null
   */
  public Map<QName, String> getOtherAttributes() {
    return otherAttributes;
  }

  @Override
  public ExpressionResult evaluate(final EvaluationContext evaluationContext,
      final PolicyDefaults policyDefaults) throws EvaluationException {

    final Bag bagAttributeValues = new Bag();


    Iterator it = otherAttributes.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<QName, String> pair = (Map.Entry)it.next();
      QName name = pair.getKey();
      String value = pair.getValue();

      AttributeValue<String> attributeValue = new StdAttributeValue<>(new IdentifierImpl(XACML.DATATYPE_STRING),
              value, new IdentifierImpl(name.getLocalPart()));
      bagAttributeValues.add(attributeValue);

    }




    return ExpressionResult.newBag(bagAttributeValues);
  }

  @Override
  protected boolean validateComponent() {
    return true;
  }

}
