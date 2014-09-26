package de.oglimmer.lunchy.rest.resources;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jooq.Record;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.LocationDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.database.dao.PictureDao;
import de.oglimmer.lunchy.database.dao.ReviewDao;
import de.oglimmer.lunchy.database.dao.UserPictureVoteDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord;
import de.oglimmer.lunchy.rest.MapAdapterStringBoolean;
import de.oglimmer.lunchy.rest.Permission;
import de.oglimmer.lunchy.rest.SecurityProvider;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LocationCreateInput;
import de.oglimmer.lunchy.rest.dto.LocationResponse;
import de.oglimmer.lunchy.rest.dto.LocationUpdateInput;
import de.oglimmer.lunchy.rest.dto.PictureResponse;
import de.oglimmer.lunchy.rest.dto.ReviewResponse;
import de.oglimmer.lunchy.services.Community;

@Slf4j
@Path("locations")
public class LocationResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/reviews")
	public List<ReviewResponse> queryReviews(@PathParam("id") int id) {
		return query(id, ReviewResponse.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/pictures")
	public List<PictureResponse> queryPictures(@PathParam("id") int id) {
		return query(id, PictureResponse.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/locationStatusForCurrentUser")
	public LocationStatusResponse locationStatusForCurrentUser(@Context HttpServletRequest request, @PathParam("id") int id) {
		return new LocationStatusForCurrentUserLogic(request, id).status();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response get(@Context HttpServletRequest request, @PathParam("id") int id) {
		return get(request, id, LocationResponse.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LocationResponse create(@Context HttpServletRequest request, LocationCreateInput locationDto) {
		return new CreateUpdateLogic(request, locationDto).create();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public LocationResponse update(@Context HttpServletRequest request, @PathParam("id") int id, LocationUpdateInput locationDto) {
		return new CreateUpdateLogic(request, locationDto).update(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}/updatePosition")
	public void updatePosition(@Context HttpServletRequest request, @PathParam("id") int id, LocationPositionUpdate locationDto) {
		LocationRecord loc = LocationDao.INSTANCE.getById(id, Community.get(request));
		loc.setGeoLat(locationDto.getLat());
		loc.setGeoLng(locationDto.getLng());
		loc.setGeoMovedManually((byte) 1);
		LocationDao.INSTANCE.store(loc);
	}

	@DELETE
	@Path("{id}")
	public void delete(@Context HttpServletRequest request, @PathParam("id") int id) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		LocationDao.INSTANCE.delete(id, Community.get(request));
	}

	@Data
	public static class LocationStatusResponse {
		private boolean hasReview;
		private boolean allowedToEdit;
		private Integer fkReview;
		private List<Integer> pictureVotes;
		@XmlJavaTypeAdapter(MapAdapterStringBoolean.class)
		private Map<String, Boolean> allowedChangeCaption;
	}

	@Data
	public static class LocationPositionUpdate {
		private Double lat;
		private Double lng;
	}

	@RequiredArgsConstructor
	class CreateUpdateLogic {

		final private HttpServletRequest request;
		final private LocationUpdateInput locationDto;
		private boolean keepManuallyUpdatedGeoData;

		public LocationResponse create() {
			LocationRecord locationRec = copyDtoToRecord(new LocationRecord());
			addInitialData(locationRec);
			return updateRec(locationRec);
		}

		public LocationResponse update(Integer id) {
			LocationRecord locationRec = copyDtoToRecord(LocationDao.INSTANCE.getById(id, Community.get(request)));
			if (locationRec.getFkUser() != SessionProvider.INSTANCE.getLoggedInUserId(request)) {
				SecurityProvider.INSTANCE.checkConfirmedUser(request);
			}
			return updateRec(locationRec);
		}

		private void addInitialData(LocationRecord locationRec) {
			locationRec.setFkCommunity(Community.get(request));
			locationRec.setFkUser(SessionProvider.INSTANCE.getLoggedInUserId(request));
			locationRec.setCreatedOn(new Timestamp(new Date().getTime()));
			locationRec.setGeoMovedManually((byte) 0);
			OfficesRecord office = OfficeDao.INSTANCE.getById(locationRec.getFkOffice(), Community.get(request));
			locationRec.setCountry(office.getCountry());
		}

		private LocationRecord copyDtoToRecord(LocationRecord locationRec) {
			if (Byte.valueOf((byte) 1).equals(locationRec.getGeoMovedManually())) {
				if (addressChanged(locationRec)) {
					locationRec.setGeoMovedManually((byte) 0);
				} else {
					keepManuallyUpdatedGeoData = true;
				}
			}
			BeanMappingProvider.INSTANCE.map(locationDto, locationRec);
			return locationRec;
		}

		private boolean addressChanged(LocationRecord locationRec) {
			return !locationDto.getAddress().equals(locationRec.getAddress())
					|| !locationDto.getCity().equals(locationRec.getCity()) || !locationDto.getZip().equals(locationRec.getZip());
		}

		private LocationResponse updateRec(LocationRecord locationRec) {
			locationRec.setLastUpdate(new Timestamp(new Date().getTime()));
			updateGeoData(locationRec);
			LocationDao.INSTANCE.store(locationRec);
			return BeanMappingProvider.INSTANCE.map(locationRec, LocationResponse.class);
		}

		private void updateGeoData(LocationRecord locationRec) {
			if (!keepManuallyUpdatedGeoData) {
				try {
					String address = locationRec.getAddress() + "," + (locationRec.getZip() != null ? locationRec.getZip() : "")
							+ " " + locationRec.getCity() + "," + locationRec.getCountry();

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
		}
	}

	@RequiredArgsConstructor()
	class LocationStatusForCurrentUserLogic {

		final private HttpServletRequest request;
		final private int locationId;
		private LocationStatusResponse result;
		private Integer userId;

		public LocationStatusResponse status() {
			init();
			checkUserReview();
			checkUserCanEdit();
			checkPictureVotes();
			checkAllowedChangeCaption();
			return result;
		}

		private void init() {
			result = new LocationStatusResponse();
			userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
			assert userId != null;
		}

		private void checkAllowedChangeCaption() {
			boolean isAdmin = SecurityProvider.INSTANCE.checkRightOnSession(request, Permission.ADMIN);
			Map<String, Boolean> map = new HashMap<>();
			for (PicturesRecord pic : PictureDao.INSTANCE.getListByParent(locationId)) {
				map.put("pic" + pic.getId(), pic.getFkUser() == userId || isAdmin);
			}
			result.setAllowedChangeCaption(map);
		}

		private void checkPictureVotes() {
			List<Record> list = UserPictureVoteDao.INSTANCE.getListByParent(locationId, userId);
			List<Integer> picVoteList = new ArrayList<>();
			for (Record rec : list) {
				UsersPicturesVotesRecord upvr = (UsersPicturesVotesRecord) rec;
				picVoteList.add(upvr.getFkPicture());
			}
			result.setPictureVotes(picVoteList);
		}

		private void checkUserCanEdit() {
			result.setAllowedToEdit(true);
			LocationRecord locRec = LocationDao.INSTANCE.getById(locationId, Community.get(request));
			if (locRec.getFkUser() != userId) {
				result.setAllowedToEdit(SecurityProvider.INSTANCE.checkRightOnSession(request, Permission.CONFIRMED_USER));
			}
		}

		private void checkUserReview() {
			Integer reviewId = ReviewDao.INSTANCE.hasUserReview(locationId, userId);
			if (reviewId != null) {
				result.setHasReview(true);
				result.setFkReview(reviewId);
			}
		}
	}

}
