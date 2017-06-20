package org.sunfish.icsp.common.exceptions;

import javax.ws.rs.core.Response.Status;

public class NotFoundException extends ICSPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3880670720377628732L;

	public NotFoundException() {
		super(Status.NOT_FOUND);
	}

	public NotFoundException(String string) {
		super(string, Status.NOT_FOUND);
	}

}
