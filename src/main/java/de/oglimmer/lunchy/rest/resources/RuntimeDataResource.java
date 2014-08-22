package de.oglimmer.lunchy.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.rest.SecurityProvider;
import de.oglimmer.lunchy.services.LunchyProperties;
import de.oglimmer.lunchy.services.MBeanServies;

@Path("runtime")
public class RuntimeDataResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("dbpool")
	public String queryDB(@Context HttpServletRequest request) {
		checkRuntimePassword(request);
		return DBConn.INSTANCE.getDriverStats().toString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("rest")
	public String queryRest(@Context HttpServletRequest request) {
		checkRuntimePassword(request);
		JsonObject data = new JsonObject();
		MBeanServies.copyAllNodes("org.glassfish.jersey:*,subType=Uris,executionTimes=*", data, "detail=");
		return data.toString();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cache")
	public void resetCaches(@Context HttpServletRequest request) {
		checkRuntimePassword(request);
		SecurityProvider.INSTANCE.reset();
		UserDao.INSTANCE.resetCache();
	}

	private void checkRuntimePassword(HttpServletRequest request) {
		String password = request.getHeader("pass");
		if (!LunchyProperties.INSTANCE.getRuntimePassword().equals(password)) {
			throw new RuntimeException("Wrong password");
		}
	}

}
