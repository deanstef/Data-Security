package org.sunfish.icsp.common.exceptions;

import javax.ws.rs.core.Response.Status;

public class InvalidRequestException extends ICSPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3880670720377628732L;

	public InvalidRequestException() {
		super(Status.BAD_REQUEST);
	}

}
