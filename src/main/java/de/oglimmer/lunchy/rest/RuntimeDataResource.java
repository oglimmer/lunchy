package de.oglimmer.lunchy.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.Data;
import de.oglimmer.lunchy.database.connection.DBConn;

@Path("/runtime")
public class RuntimeDataResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/dbpool")
	public String query(@Context HttpServletRequest request) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		return DBConn.INSTANCE.getDriverStats().toString();
	}

	@Data
	public static class PermissionInput {
		private Integer permissions;
	}

}
