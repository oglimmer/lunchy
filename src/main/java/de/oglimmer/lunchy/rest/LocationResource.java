package de.oglimmer.lunchy.rest;

import java.io.IOException;
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

import lombok.extern.slf4j.Slf4j;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import de.oglimmer.lunchy.database.LocationDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.rest.dto.Location;

@Slf4j
@Path("locations")
public class LocationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> query() {
		List<Location> resultList = new ArrayList<>();
		for (LocationRecord locationRec : LocationDao.INSTANCE.getList()) {
			resultList.add(Location.getInstance(locationRec));
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Location get(@PathParam("id") int id) {
		LocationRecord locationRec = LocationDao.INSTANCE.getById(id);
		return Location.getInstance(locationRec);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Location post(@Context HttpServletRequest request, Location locationDto) {
		LocationRecord locationRec = convertDtoToRecord(locationDto);
		addInitialData(request, locationRec);
		return updateRec(locationRec);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Location postUpdate(@Context HttpServletRequest request, @PathParam("id") int id, Location locationDto) {
		if (id != locationDto.getId()) {
			throw new RuntimeException("wrong id");
		}
		LocationRecord locationRec = convertDtoToRecord(locationDto);
		addInitialData(request, locationRec);
		return updateRec(locationRec);
	}

	private void addInitialData(HttpServletRequest request, LocationRecord locationRec) {
		if (locationRec.getId() == null || locationRec.getId() == 0) {
			locationRec.setFkuser((Integer) request.getSession(false).getAttribute("userId"));
			locationRec.setCreatedon(new Timestamp(new Date().getTime()));
			locationRec.setCountry("Germany");
		}
	}

	private LocationRecord convertDtoToRecord(Location locationDto) {
		LocationRecord locationRec = getEmptyOrUnchangedRecord(locationDto.getId());
		copyDtoToRecord(locationDto, locationRec);
		return locationRec;
	}

	private void copyDtoToRecord(Location locationDto, LocationRecord locationRec) {
		BeanMappingProvider.INSTANCE.getMapper().map(locationDto, locationRec);
	}

	private LocationRecord getEmptyOrUnchangedRecord(Integer id) {
		LocationRecord locationRec;
		if (id == null || id == 0) {
			locationRec = new LocationRecord();
		} else {
			locationRec = LocationDao.INSTANCE.getById(id);
		}
		return locationRec;
	}

	private Location updateRec(LocationRecord locationRec) {
		locationRec.setLastupdate(new Timestamp(new Date().getTime()));

		getGeoData(locationRec);

		LocationDao.INSTANCE.store(locationRec);
		return Location.getInstance(locationRec);
	}

	private void getGeoData(LocationRecord locationRec) {
		try {

			String address = locationRec.getAddress() + "," + (locationRec.getZip() != null ? locationRec.getZip() : "") + " "
					+ locationRec.getCity() + "," + locationRec.getCountry();

			final Geocoder geocoder = new Geocoder();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);

			if (geocoderResponse.getResults().size() != 1) {
				log.error("Failed to get one result for " + address + " got " + geocoderResponse.getResults());
			}

			for (GeocoderResult gr : geocoderResponse.getResults()) {
				LatLng latLng = gr.getGeometry().getLocation();
				locationRec.setGeoLat(latLng.getLat().doubleValue());
				locationRec.setGeoLng(latLng.getLng().doubleValue());

			}
		} catch (IOException e) {
			log.error("Error accessing Geocoder API", e);
		}
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") int id) {
		LocationDao.INSTANCE.delete(id);
	}

}
