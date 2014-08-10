package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.jooq.Record;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationQuery extends Location {

	private Integer numberOfReviews;
	private Float avgRating;
	private Timestamp lastRating;
	private boolean reviewed;

	public static LocationQuery getInstance(Record rawRec) {
		LocationQuery lq = new LocationQuery();
		lq.setId(rawRec.getValue("id", Integer.class));
		lq.setOfficialname(rawRec.getValue("officialname", String.class));
		lq.setStreetname(rawRec.getValue("streetname", String.class));
		lq.setAddress(rawRec.getValue("address", String.class));
		lq.setCity(rawRec.getValue("city", String.class));
		lq.setZip(rawRec.getValue("zip", String.class));
		lq.setCountry(rawRec.getValue("country", String.class));
		lq.setUrl(rawRec.getValue("url", String.class));
		lq.setComment(rawRec.getValue("comment", String.class));
		lq.setTurnaroundtime(rawRec.getValue("turnAroundTime", Integer.class));
		lq.setCreatedon(rawRec.getValue("createdOn", Timestamp.class));
		lq.setLastupdate(rawRec.getValue("lastUpdate", Timestamp.class));
		lq.setTags(rawRec.getValue("tags", String.class));

		UsersRecord user = UserDao.INSTANCE.getById(rawRec.getValue("fkUser", Integer.class));
		lq.setCreationUser(user.getDisplayname());

		lq.setGeoLat(rawRec.getValue("geo_lat", Double.class));
		lq.setGeoLng(rawRec.getValue("geo_lng", Double.class));
		Timestamp lastRating = rawRec.getValue("lastRating", Timestamp.class);
		lq.setLastRating(lastRating);
		Integer numberOfReviews = lastRating != null ? rawRec.getValue("numberOfReviews", Integer.class) : 0;
		lq.setNumberOfReviews(numberOfReviews);

		Float avgRating = rawRec.getValue("avgRating", Float.class);
		lq.setAvgRating(avgRating);
		lq.setReviewed(rawRec.getValue("reviewed", Integer.class) != 0);
		return lq;
	}
}