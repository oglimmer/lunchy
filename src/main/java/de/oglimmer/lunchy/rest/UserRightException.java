package de.oglimmer.lunchy.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UserRightException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	public UserRightException() {
		super(Response.Status.UNAUTHORIZED);
	}
}