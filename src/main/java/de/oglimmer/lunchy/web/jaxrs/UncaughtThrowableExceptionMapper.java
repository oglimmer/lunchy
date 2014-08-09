package de.oglimmer.lunchy.web.jaxrs;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import org.glassfish.jersey.spi.ExtendedExceptionMapper;

@Slf4j
@Provider
public class UncaughtThrowableExceptionMapper implements ExtendedExceptionMapper<Throwable> {

	@Override
	public boolean isMappable(Throwable throwable) {
		return !(throwable instanceof WebApplicationException);
	}

	@Override
	public Response toResponse(Throwable t) {
		log.error("Unhandled REST exception", t);
		return Response.serverError().build();
	}
}
