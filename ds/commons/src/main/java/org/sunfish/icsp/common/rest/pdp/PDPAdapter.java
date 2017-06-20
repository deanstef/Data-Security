package org.sunfish.icsp.common.rest.pdp;

import org.apache.openaz.xacml.api.Request;
import org.sunfish.icsp.common.exceptions.ICSPException;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface PDPAdapter {

    AuthorizationResponse authorization(String signature, String reqReference, Request request, String serviceId, String policyType) throws ICSPException;
}
