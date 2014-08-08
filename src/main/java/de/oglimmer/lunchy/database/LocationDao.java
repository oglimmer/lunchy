package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.LocationQuery;

public enum LocationDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public LocationRecord getById(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			LocationRecord rec = create.fetchOne(Location.LOCATION, Location.LOCATION.ID.equal(id));
			rec.attach(null);
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<LocationQuery> getList(Integer fkUser) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			String reviewedSubSql = fkUser == null ? "'0'"
					: "(select count(*) from reviews where location.id=reviews.fkLocation and fkUser=" + fkUser + ")";

			Result<Record> result = create
					.fetch("select location.*, count(*) as numberOfReviews, max(reviews.lastUpdate) as lastRating, avg(reviews.rating) as avgRating, "
							+ reviewedSubSql
							+ " as reviewed from location left JOIN reviews on location.id=reviews.fkLocation group by location.id");

			List<LocationQuery> resultList = new ArrayList<>();
			for (Record rawRec : result) {
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

				resultList.add(lq);
			}

			return resultList;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void store(LocationRecord location) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			location.attach(create.configuration());
			location.store();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void delete(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			LocationRecord rec = create.fetchOne(Location.LOCATION, Location.LOCATION.ID.equal(id));
			rec.delete();
		}
	}

}
