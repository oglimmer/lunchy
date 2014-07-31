package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

import org.dozer.DozerBeanMapper;

import de.oglimmer.lunchy.database.LocationDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.rest.dto.Location;

@Path("locations")
public class LocationResource {

	private DozerBeanMapper dbm = new DozerBeanMapper();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> query() {
		List<Location> resultList = new ArrayList<Location>();
		for (LocationRecord locationRec : LocationDao.INSTANCE.getList()) {
			Location locationDto = new Location();
			dbm.map(locationRec, locationDto);
			resultList.add(locationDto);
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Location get(@PathParam("id") int id) {
		LocationRecord locationRec = LocationDao.INSTANCE.getById(id);
		Location locationDto = new Location();
		dbm.map(locationRec, locationDto);
		return locationDto;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Location post(@Context HttpServletRequest request,
			Location locationDto) {

		if (locationDto.getId() == null || locationDto.getId() == 0) {
			locationDto.setFkuser((Integer) request.getSession(false)
					.getAttribute("userId"));
			locationDto.setCreatedon(new Timestamp(new Date().getTime()));
			locationDto.setLastupdate(new Timestamp(new Date().getTime()));
		}

		System.out.println(locationDto);

		LocationRecord locationRec = new LocationRecord();
		dbm.map(locationDto, locationRec);
		LocationDao.INSTANCE.store(locationRec);
		Location backLocationDto = new Location();
		dbm.map(locationRec, backLocationDto);
		return backLocationDto;
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") int id) {
		LocationDao.INSTANCE.delete(id);
	}

}
