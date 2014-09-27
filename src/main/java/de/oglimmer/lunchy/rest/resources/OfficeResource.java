package de.oglimmer.lunchy.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.LocationDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.rest.dto.OfficeCreateInput;
import de.oglimmer.lunchy.rest.dto.OfficeResponse;
import de.oglimmer.lunchy.rest.dto.OfficeUpdateInput;
import de.oglimmer.lunchy.security.SecurityProvider;
import de.oglimmer.lunchy.services.CommunityService;

@Path("offices")
public class OfficeResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<OfficeResponse> queryOffices(@Context HttpServletRequest request) {
		return query(CommunityService.get(request), OfficeResponse.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response get(@Context HttpServletRequest request, @PathParam("id") int id) {
		return get(request, id, OfficeResponse.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("defaultOffice")
	public DefaultOfficeResponse getDefaultOffice(@Context HttpServletRequest request) {
		return new DefaultOfficeResponse(true, OfficeDao.INSTANCE.getDefaultOffice(CommunityService.get(request)));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/locations")
	public List<LocationQuery> queryLocations(@Context HttpServletRequest request, @PathParam("id") int id) {
		Integer fkUser = SessionProvider.INSTANCE.getLoggedInUserId(request);
		List<LocationQuery> resultList = new ArrayList<>();
		for (Record rec : LocationDao.INSTANCE.getList(fkUser, id)) {
			resultList.add(LocationQuery.getInstance(rec, CommunityService.get(request)));
		}
		return resultList;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public OfficeResponse create(@Context HttpServletRequest request, OfficeCreateInput officeDto) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		return new CreateUpdateLogic(request, officeDto).create();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public OfficeResponse update(@Context HttpServletRequest request, @PathParam("id") int id, OfficeUpdateInput officeDto) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		return new CreateUpdateLogic(request, officeDto).update(id);
	}

	@DELETE
	@Path("{id}")
	public void delete(@Context HttpServletRequest request, @PathParam("id") int id) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		OfficeDao.INSTANCE.delete(id, CommunityService.get(request));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DefaultOfficeResponse {
		private boolean success;
		private Integer defaultOffice;
	}

	@AllArgsConstructor
	class CreateUpdateLogic {

		private HttpServletRequest request;
		private OfficeUpdateInput officeDto;

		public OfficeResponse create() {
			OfficesRecord locationRec = copyDtoToRecord(new OfficesRecord());
			addInitialData(locationRec);
			return updateRec(locationRec);
		}

		public OfficeResponse update(Integer id) {
			OfficesRecord locationRec = copyDtoToRecord(OfficeDao.INSTANCE.getById(id, CommunityService.get(request)));
			return updateRec(locationRec);
		}

		private void addInitialData(OfficesRecord locationRec) {
			locationRec.setFkCommunity(CommunityService.get(request));
			if (locationRec.getCountry() == null) {
				locationRec.setCountry("");
			}
		}

		private OfficesRecord copyDtoToRecord(OfficesRecord locationRec) {
			BeanMappingProvider.INSTANCE.map(officeDto, locationRec);
			return locationRec;
		}

		private OfficeResponse updateRec(OfficesRecord locationRec) {
			OfficeDao.INSTANCE.store(locationRec);
			return BeanMappingProvider.INSTANCE.map(locationRec, OfficeResponse.class);
		}

	}
}
