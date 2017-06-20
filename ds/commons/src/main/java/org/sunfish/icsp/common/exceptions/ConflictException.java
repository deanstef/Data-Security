package org.sunfish.icsp.common.exceptions;

import javax.ws.rs.core.Response.Status;

public class ConflictException extends ICSPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3880670720377628732L;

	public ConflictException() {
		super(Status.CONFLICT);
	}

	public ConflictException(String string) {
		super(string, Status.CONFLICT);
	}

}
