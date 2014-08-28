package de.oglimmer.lunchy.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.jooq.Record;

import de.oglimmer.lunchy.database.dao.LocationDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.rest.dto.Office;
import de.oglimmer.lunchy.services.Community;

@Path("offices")
public class OfficeResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Office> queryOffices(@Context HttpServletRequest request) {
		return query(Community.get(request), Office.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response get(@Context HttpServletRequest request, @PathParam("id") int id) {
		return get(request, id, Office.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("defaultOffice")
	public DefaultOfficeResponse getDefaultOffice(@Context HttpServletRequest request) {
		return new DefaultOfficeResponse(true, OfficeDao.INSTANCE.getDefaultOffice(Community.get(request)));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/locations")
	public List<LocationQuery> queryLocations(@Context HttpServletRequest request, @PathParam("id") int id) {
		Integer fkUser = SessionProvider.INSTANCE.getLoggedInUserId(request);
		List<LocationQuery> resultList = new ArrayList<>();
		for (Record rec : LocationDao.INSTANCE.getList(fkUser, id)) {
			resultList.add(LocationQuery.getInstance(rec, Community.get(request)));
		}
		return resultList;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DefaultOfficeResponse {
		private boolean success;
		private Integer defaultOffice;
	}
}
