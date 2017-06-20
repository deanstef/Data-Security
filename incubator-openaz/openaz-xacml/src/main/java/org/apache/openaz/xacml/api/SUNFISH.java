package org.apache.openaz.xacml.api;

import org.apache.openaz.xacml.std.IdentifierImpl;

public interface SUNFISH {
  public static final Identifier ID_SUNFISH                   = new IdentifierImpl("urn:sunfish");
  public static final Identifier ID_ENVIRONMENT               = new IdentifierImpl(ID_SUNFISH, "environment");


  public static final String SUNFISH_OBLIGATION_EXPRESSION      = "SunfishObligationExpression";
  public static final String SUNFISH_OBLIGATION_EXPRESSION_TYPE = "SunfishObligationExpressionType";



//  public static final Identifier ID_ATTRIBUTE_CATEGORY = new IdentifierImpl(ID_SUNFISH, "attribute-category");

  public static final Identifier ID_ENVIRONMENT_CURRENT_DAY   = new IdentifierImpl(ID_ENVIRONMENT,
      "current-day-in-month");
  public static final Identifier ID_ENVIRONMENT_CURRENT_MONTH = new IdentifierImpl(ID_ENVIRONMENT,
      "current-month");
  public static final Identifier ID_ENVIRONMENT_CURRENT_YEAR  = new IdentifierImpl(ID_ENVIRONMENT,
      "current-year");
}
