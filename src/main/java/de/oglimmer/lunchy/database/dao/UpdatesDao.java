package de.oglimmer.lunchy.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record7;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.Pictures;
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.Users;

public enum UpdatesDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public List<Record> get(int numberOfItems, int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return queryByItems(numberOfItems, fkCommunity, create);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<Record> get(Timestamp from, int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return queryByDate(from, fkCommunity, create);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<Record> getPictures(Timestamp from, int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return new RetrievePictureLogic(create, fkCommunity).queryPictures(from);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<Record> getPictures(int numberOfItems, int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return new RetrievePictureLogic(create, fkCommunity).queryPictures(numberOfItems);
		}
	}

	private Result<Record> queryByDate(Timestamp from, int fkCommunity, DSLContext create) {
		return new UpdatesQueryLogic(fkCommunity, create).queryDB()
				.where(DSL.val("last_Update", Timestamp.class).as("last_Update").greaterThan(from))
				.orderBy(DSL.val("last_Update").as("last_Update").desc()).fetch();
	}

	private Result<Record> queryByItems(int numberOfItems, int fkCommunity, DSLContext create) {
		return new UpdatesQueryLogic(fkCommunity, create).queryDB().orderBy(DSL.val("last_Update").as("last_Update").desc())
				.limit(numberOfItems).fetch();
	}

	@AllArgsConstructor
	class UpdatesQueryLogic {

		private int fkCommunity;
		private DSLContext create;

		public SelectJoinStep<Record> queryDB() {
			return create.select().from(
					createLocationQuery().union(createReviewQuery()).union(createUserQuery()).union(createPictureQuery()));
		}

		private SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> createPictureQuery() {
			SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> pictureSelect = create
					.select(DSL.val("P").as("type"), Location.LOCATION.OFFICIAL_NAME, Location.LOCATION.CITY,
							Users.USERS.DISPLAYNAME.as("user"), DSL.val("N").as("update_Type"), Location.LOCATION.ID,
							Pictures.PICTURES.CREATED_ON).from(Location.LOCATION).join(Pictures.PICTURES, JoinType.JOIN)
					.on(Location.LOCATION.ID.equal(Pictures.PICTURES.FK_LOCATION)).join(Users.USERS, JoinType.JOIN)
					.on(Pictures.PICTURES.FK_USER.equal(Users.USERS.ID)).where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));
			return pictureSelect;
		}

		private SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> createUserQuery() {
			SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> usersSelect = create
					.select(DSL.val("U").as("type"), DSL.val("").as("official_Name"), DSL.val("").as("city"),
							Users.USERS.DISPLAYNAME.as("user"), DSL.val("N").as("update_Type"), Users.USERS.ID, Users.USERS.CREATED_ON)
					.from(Users.USERS).where(Users.USERS.FK_COMMUNITY.equal(fkCommunity));
			return usersSelect;
		}

		private SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> createReviewQuery() {
			SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> reviewsSelect = create
					.select(DSL.val("R").as("type"),
							Location.LOCATION.OFFICIAL_NAME,
							Location.LOCATION.CITY,
							Users.USERS.DISPLAYNAME.as("user"),
							DSL.decode().value(Reviews.REVIEWS.CREATED_ON).when(Reviews.REVIEWS.LAST_UPDATE, "N").otherwise("U")
									.as("update_Type"), Location.LOCATION.ID, Reviews.REVIEWS.LAST_UPDATE).from(Location.LOCATION)
					.join(Reviews.REVIEWS, JoinType.JOIN).on(Location.LOCATION.ID.equal(Reviews.REVIEWS.FK_LOCATION))
					.join(Users.USERS, JoinType.JOIN).on(Reviews.REVIEWS.FK_USER.equal(Users.USERS.ID))
					.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));
			return reviewsSelect;
		}

		private SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> createLocationQuery() {
			SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> locationSelect = create
					.select(DSL.val("L").as("type"),
							Location.LOCATION.OFFICIAL_NAME,
							Location.LOCATION.CITY,
							DSL.val("").as("user"),
							DSL.decode().value(Location.LOCATION.CREATED_ON).when(Location.LOCATION.LAST_UPDATE, "N").otherwise("U")
									.as("update_Type"), Location.LOCATION.ID, Location.LOCATION.LAST_UPDATE).from(Location.LOCATION)
					.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));
			return locationSelect;
		}
	}

	@AllArgsConstructor
	class RetrievePictureLogic {

		private DSLContext create;
		private int fkCommunity;

		public List<Record> queryPictures(int num) {
			List<LocationIdSize> locationsWithPics = findLocationsWithNewPicturesSince(num * 4);
			locationsWithPics = reduceList(locationsWithPics, num);
			List<Integer> picIds = loadPicIdsForLocationIds(locationsWithPics);
			return query(picIds);
		}

		public List<Record> queryPictures(Timestamp from) {
			List<LocationIdSize> locationsWithPics = findLocationsWithNewPicturesSince(from);
			locationsWithPics = reduceList(locationsWithPics, 3);
			List<Integer> picIds = loadPicIdsForLocationIds(locationsWithPics);
			return query(picIds);
		}

		private Result<Record> query(List<Integer> picIds) {
			return create
					.select()
					.select(Location.LOCATION.ID, Location.LOCATION.OFFICIAL_NAME, Location.LOCATION.CITY, Users.USERS.DISPLAYNAME,
							Pictures.PICTURES.FILENAME, Pictures.PICTURES.CAPTION).from(Location.LOCATION)
					.join(Pictures.PICTURES, JoinType.JOIN).on(Location.LOCATION.ID.equal(Pictures.PICTURES.FK_LOCATION))
					.join(Users.USERS, JoinType.JOIN).on(Pictures.PICTURES.FK_USER.equal(Users.USERS.ID))
					.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity)).and(Pictures.PICTURES.ID.in(picIds)).fetch();
		}

		private List<Integer> loadPicIdsForLocationIds(List<LocationIdSize> locationList) {
			List<Integer> idList = new ArrayList<>();
			for (LocationIdSize loc : locationList) {
				int offset = (int) (Math.random() * loc.getCount());
				Integer id = create.select(Pictures.PICTURES.ID).from(Pictures.PICTURES)
						.where(Pictures.PICTURES.FK_LOCATION.equal(loc.getFkLocation())).limit(offset, 1).fetchOne().value1();
				idList.add(id);
			}
			return idList;
		}

		private <T> List<T> reduceList(List<T> list, int maxSize) {
			List<T> reducedList = new ArrayList<>();
			if (maxSize > list.size()) {
				maxSize = list.size();
			}
			for (int i = 0; i < maxSize; i++) {
				T rndSelectedObj = list.remove((int) (Math.random() * list.size()));
				reducedList.add(rndSelectedObj);
			}
			return reducedList;
		}

		private List<LocationIdSize> findLocationsWithNewPicturesSince(int num) {
			Result<Record> locationsWithPic = create.selectCount().select(Pictures.PICTURES.FK_LOCATION).from(Pictures.PICTURES)
					.where(Pictures.PICTURES.FK_COMMUNITY.equal(fkCommunity)).groupBy(Pictures.PICTURES.FK_LOCATION)
					.orderBy(Pictures.PICTURES.CREATED_ON.desc()).limit(num).fetch();

			return convertToDtoList(locationsWithPic);
		}

		private List<LocationIdSize> findLocationsWithNewPicturesSince(Timestamp from) {
			Result<Record> locationsWithPic = create.selectCount().select(Pictures.PICTURES.FK_LOCATION).from(Pictures.PICTURES)
					.where(Pictures.PICTURES.CREATED_ON.greaterThan(from).and(Pictures.PICTURES.FK_COMMUNITY.equal(fkCommunity)))
					.groupBy(Pictures.PICTURES.FK_LOCATION).fetch();

			return convertToDtoList(locationsWithPic);
		}

		private List<LocationIdSize> convertToDtoList(Result<Record> locationsWithPic) {
			List<LocationIdSize> list = new ArrayList<>();
			for (Record rec : locationsWithPic) {
				list.add(new LocationIdSize(rec.getValue(Pictures.PICTURES.FK_LOCATION), rec.getValue("count", Integer.class)));
			}
			return list;
		}

		@Value
		class LocationIdSize {
			private int fkLocation;
			private int count;
		}
	}
}
