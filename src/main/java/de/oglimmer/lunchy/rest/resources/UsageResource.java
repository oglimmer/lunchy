package de.oglimmer.lunchy.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.oglimmer.lunchy.database.dao.UsageDao;
import de.oglimmer.lunchy.database.dao.UsageDao.LongTimeToken;

@Path("usage")
public class UsageResource {

	@POST
	@Path("{action}/{context}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void register(@Context HttpServletRequest request, @PathParam("action") String action,
			@PathParam("context") String context, LongTimeToken longTimeToken) {

		UsageDao.INSTANCE.store(action, context, request, longTimeToken);
	}

}
