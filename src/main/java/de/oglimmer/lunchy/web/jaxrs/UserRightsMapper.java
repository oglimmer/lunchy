package de.oglimmer.lunchy.web.jaxrs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.oglimmer.lunchy.rest.UserRightException;

@Provider
public class UserRightsMapper implements ExceptionMapper<UserRightException> {

	public Response toResponse(UserRightException ex) {
		return Response.status(ex.getResponse().getStatus()).build();
	}

}
