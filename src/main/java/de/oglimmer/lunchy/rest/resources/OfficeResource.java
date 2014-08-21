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

import de.oglimmer.lunchy.database.LocationDao;
import de.oglimmer.lunchy.database.OfficeDao;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.rest.dto.Office;
import de.oglimmer.lunchy.rest.dto.ResultParam;
import de.oglimmer.lunchy.services.Community;

@Path("offices")
public class OfficeResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Office> queryReviews(@Context HttpServletRequest request) {
		List<Office> resultList = new ArrayList<>();
		for (OfficesRecord officeRec : OfficeDao.INSTANCE.query(Community.get(request))) {
			resultList.add(Office.getInstance(officeRec));
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Office get(@PathParam("id") int id) {
		OfficesRecord officeRec = OfficeDao.INSTANCE.getById(id);
		return Office.getInstance(officeRec);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("defaultOffice")
	public ResultParam getDefaultOffice(@Context HttpServletRequest request) {
		return new ResultParam(true, Integer.toString(OfficeDao.INSTANCE.getDefaultOffice(Community.get(request))));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/locations")
	public List<LocationQuery> query(@Context HttpServletRequest request, @PathParam("id") int id) {
		Integer fkUser = null;
		if (request.getSession(false) != null) {
			fkUser = (Integer) request.getSession(false).getAttribute("userId");
		}
		return LocationDao.INSTANCE.getList(Community.get(request), fkUser, id);
	}
}
