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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.LocationDao;
import de.oglimmer.lunchy.database.OfficeDao;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.rest.dto.Office;
import de.oglimmer.lunchy.services.Community;

@Path("offices")
public class OfficeResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Office> queryOffices(@Context HttpServletRequest request) {
		List<Office> resultList = new ArrayList<>();
		for (OfficesRecord officeRec : OfficeDao.INSTANCE.query(Community.get(request))) {
			resultList.add(BeanMappingProvider.INSTANCE.map(officeRec, Office.class));
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Office get(@Context HttpServletRequest request, @PathParam("id") int id) {
		OfficesRecord officeRec = OfficeDao.INSTANCE.getById(id, Community.get(request));
		return BeanMappingProvider.INSTANCE.map(officeRec, Office.class);
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
		Integer fkUser = null;
		if (request.getSession(false) != null) {
			fkUser = (Integer) request.getSession(false).getAttribute("userId");
		}
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
