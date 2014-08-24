package de.oglimmer.lunchy.rest.resources;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.LocationDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.database.dao.PicturesDao;
import de.oglimmer.lunchy.database.dao.ReviewDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.rest.SecurityProvider;
import de.oglimmer.lunchy.rest.UserRightException;
import de.oglimmer.lunchy.rest.dto.Location;
import de.oglimmer.lunchy.rest.dto.Picture;
import de.oglimmer.lunchy.rest.dto.Review;
import de.oglimmer.lunchy.services.Community;

@Slf4j
@Path("locations")
public class LocationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/reviews")
	public List<Review> queryReviews(@PathParam("id") int id) {
		List<Review> resultList = new ArrayList<>();
		for (ReviewsRecord reviewRec : ReviewDao.INSTANCE.getList(id)) {
			resultList.add(BeanMappingProvider.INSTANCE.map(reviewRec, Review.class));
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/pictures")
	public List<Picture> queryPictures(@PathParam("id") int id) {
		List<Picture> resultList = new ArrayList<>();
		for (PicturesRecord reviewRec : PicturesDao.INSTANCE.getList(id)) {
			resultList.add(BeanMappingProvider.INSTANCE.map(reviewRec, Picture.class));
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/locationStatusForCurrentUser")
	public LocationStatus locationStatusForCurrentUser(@Context HttpServletRequest request, @PathParam("id") int id) {
		Integer userId = (Integer) request.getSession(false).getAttribute("userId");
		Integer reviewId = ReviewDao.INSTANCE.hasUserReview(id, userId);
		LocationStatus result = new LocationStatus();
		if (reviewId != null) {
			result.setHasReview(true);
			result.setFkReview(reviewId);
		}
		LocationRecord locRec = LocationDao.INSTANCE.getById(id, Community.get(request));
		result.setAllowedToEdit(true);
		if (locRec.getFkUser() != userId) {
			try {
				SecurityProvider.INSTANCE.checkConfirmedUser(request);
			} catch (UserRightException e) {
				result.setAllowedToEdit(false);
			}
		}

		return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response get(@Context HttpServletRequest request, @PathParam("id") int id) {
		LocationRecord locationRec = LocationDao.INSTANCE.getById(id, Community.get(request));
		if (locationRec == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(BeanMappingProvider.INSTANCE.map(locationRec, Location.class)).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Location create(@Context HttpServletRequest request, Location locationDto) {
		LocationRecord locationRec = convertDtoToRecord(locationDto, Community.get(request));
		addInitialData(request, locationRec);
		return updateRec(locationRec);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Location update(@Context HttpServletRequest request, @PathParam("id") int id, Location locationDto) {
		if (id != locationDto.getId()) {
			throw new RuntimeException("wrong id");
		}
		LocationRecord locationRec = convertDtoToRecord(locationDto, Community.get(request));
		if (locationRec.getFkUser() != request.getSession(false).getAttribute("userId")) {
			SecurityProvider.INSTANCE.checkConfirmedUser(request);
		}
		addInitialData(request, locationRec);
		return updateRec(locationRec);
	}

	private void addInitialData(HttpServletRequest request, LocationRecord locationRec) {
		if (locationRec.getId() == null || locationRec.getId() == 0) {
			locationRec.setFkCommunity(Community.get(request));
			locationRec.setFkUser((Integer) request.getSession(false).getAttribute("userId"));
			locationRec.setCreatedOn(new Timestamp(new Date().getTime()));
			OfficesRecord office = OfficeDao.INSTANCE.getById(locationRec.getFkOffice(), Community.get(request));
			locationRec.setCountry(office.getCountry());
		}
	}

	private LocationRecord convertDtoToRecord(Location locationDto, int fkCommunity) {
		LocationRecord locationRec = getEmptyOrUnchangedRecord(locationDto.getId(), fkCommunity);
		copyDtoToRecord(locationDto, locationRec);
		return locationRec;
	}

	private void copyDtoToRecord(Location locationDto, LocationRecord locationRec) {
		// HACK:make sure turnAroundTime is not overwritten
		locationDto.setTurnAroundTime(locationRec.getTurnAroundTime());

		BeanMappingProvider.INSTANCE.map(locationDto, locationRec);
	}

	private LocationRecord getEmptyOrUnchangedRecord(Integer id, int fkCommunity) {
		LocationRecord locationRec;
		if (id == null || id == 0) {
			locationRec = new LocationRecord();
		} else {
			locationRec = LocationDao.INSTANCE.getById(id, fkCommunity);
		}
		return locationRec;
	}

	private Location updateRec(LocationRecord locationRec) {
		locationRec.setLastUpdate(new Timestamp(new Date().getTime()));

		getGeoData(locationRec);

		LocationDao.INSTANCE.store(locationRec);
		return BeanMappingProvider.INSTANCE.map(locationRec, Location.class);
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
	public void delete(@Context HttpServletRequest request, @PathParam("id") int id) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		LocationDao.INSTANCE.delete(id, Community.get(request));
	}

	@Data
	public static class LocationStatus {
		private boolean hasReview;
		private boolean allowedToEdit;
		private Integer fkReview;
	}
}
