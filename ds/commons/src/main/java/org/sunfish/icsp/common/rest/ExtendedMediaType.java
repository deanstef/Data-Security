package org.sunfish.icsp.common.rest;

import javax.ws.rs.core.MediaType;

public class ExtendedMediaType extends MediaType {

  public static final MediaType APPLICATION_XML_XACML_TYPE = new MediaType("application", "xml+xacml");
  public static final String    APPLICATION_XML_XACML      = "application/xml+xacml";
  public static final MediaType APPLICATION_HOME_XML_TYPE  = new MediaType("application", "home+xml");
  public static final String    APPLICATION_HOME_XML       = "application/home+xml";
}
