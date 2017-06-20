package org.sunfish.icsp.common.rest.pip;

import java.util.Set;
import java.util.SortedSet;

import org.apache.openaz.xacml.api.Request;
import org.apache.openaz.xacml.pdp.policy.expressions.AttributeDesignator;
import org.sunfish.icsp.common.api.pip.SunfishPIPRequest;
import org.sunfish.icsp.common.exceptions.ICSPException;

public interface PIPAdapter {

  public String getName();

  public Request request(SunfishPIPRequest request) throws ICSPException;

  public Set<AttributeDesignator> collect() throws ICSPException;

  public boolean matches(SunfishPIPRequest req);
  public SortedSet<AttributeDesignator> getMatchingAttributes(SunfishPIPRequest req);
}