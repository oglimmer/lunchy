package de.oglimmer.lunchy.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.SelectSeekStep1;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.Pictures;
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes;

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
	public List<Record> getPictures(Timestamp from, int fkCommunity, int numberOfItems) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return new RetrievePictureLogic(create, fkCommunity).querySince(from, 3, 5);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<Record> getPictures(int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return new RetrievePictureLogic(create, fkCommunity).queryTwoOfLatest(15);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<Record> getPictures(int fkCommunity, Integer startPos, Integer numberOfRecords, Integer userId) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			if (startPos == null) {
				startPos = 0;
			}
			if (numberOfRecords == null) {
				numberOfRecords = 8;
			}
			return new RetrievePictureLogic(create, fkCommunity).queryLatest(startPos, numberOfRecords, userId);
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

		private SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> createPictureQuery() {
			SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> pictureSelect = create
					.select(DSL.val("P").as("type"), Location.LOCATION.OFFICIAL_NAME, Location.LOCATION.CITY,
							Users.USERS.DISPLAYNAME.as("user"), DSL.val("N").as("update_Type"), Location.LOCATION.ID,
							Pictures.PICTURES.CREATED_ON, Pictures.PICTURES.ID.as("picture_Id")).from(Location.LOCATION)
					.join(Pictures.PICTURES, JoinType.JOIN).on(Location.LOCATION.ID.equal(Pictures.PICTURES.FK_LOCATION))
					.join(Users.USERS, JoinType.JOIN).on(Pictures.PICTURES.FK_USER.equal(Users.USERS.ID))
					.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));
			return pictureSelect;
		}

		private SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> createUserQuery() {
			SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> usersSelect = create
					.select(DSL.val("U").as("type"), DSL.val("").as("official_Name"), DSL.val("").as("city"),
							Users.USERS.DISPLAYNAME.as("user"), DSL.val("N").as("update_Type"), Users.USERS.ID, Users.USERS.CREATED_ON,
							DSL.val(0).as("picture_Id")).from(Users.USERS).where(Users.USERS.FK_COMMUNITY.equal(fkCommunity));
			return usersSelect;
		}

		private SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> createReviewQuery() {
			SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> reviewsSelect = create
					.select(DSL.val("R").as("type"),
							Location.LOCATION.OFFICIAL_NAME,
							Location.LOCATION.CITY,
							Users.USERS.DISPLAYNAME.as("user"),
							DSL.decode().value(Reviews.REVIEWS.CREATED_ON).when(Reviews.REVIEWS.LAST_UPDATE, "N").otherwise("U")
									.as("update_Type"), Location.LOCATION.ID, Reviews.REVIEWS.LAST_UPDATE, DSL.val(0).as("picture_Id"))
					.from(Location.LOCATION).join(Reviews.REVIEWS, JoinType.JOIN)
					.on(Location.LOCATION.ID.equal(Reviews.REVIEWS.FK_LOCATION)).join(Users.USERS, JoinType.JOIN)
					.on(Reviews.REVIEWS.FK_USER.equal(Users.USERS.ID)).where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));
			return reviewsSelect;
		}

		private SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> createLocationQuery() {
			SelectConditionStep<Record8<String, String, String, String, String, Integer, Timestamp, Integer>> locationSelect = create
					.select(DSL.val("L").as("type"),
							Location.LOCATION.OFFICIAL_NAME,
							Location.LOCATION.CITY,
							DSL.val("").as("user"),
							DSL.decode().value(Location.LOCATION.CREATED_ON).when(Location.LOCATION.LAST_UPDATE, "N").otherwise("U")
									.as("update_Type"), Location.LOCATION.ID, Location.LOCATION.LAST_UPDATE, DSL.val(0).as("picture_Id"))
					.from(Location.LOCATION).where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));
			return locationSelect;
		}
	}

	@AllArgsConstructor
	class RetrievePictureLogic {

		private DSLContext create;
		private int fkCommunity;

		public List<Record> queryTwoOfLatest(int numberOfPicturesToConsider) {
			List<Record> result = new ArrayList<>();
			Record first = addPic(numberOfPicturesToConsider, result, null);
			if (first != null) {
				addPic(numberOfPicturesToConsider, result, first.getValue(Location.LOCATION.ID));
			}
			return result;
		}

		public List<Record> queryLatest(int startPos, int limit, Integer userId) {
			return query(0, startPos, limit, null, null, userId);
		}

		private Record addPic(int numberOfPictures, List<Record> result, Integer forbiddenFkLocation) {
			int minimumUpVote = 1;
			Record rec = query(minimumUpVote, numberOfPictures, forbiddenFkLocation);
			if (rec == null) {
				minimumUpVote = 0;
				rec = query(minimumUpVote, numberOfPictures, forbiddenFkLocation);
			}
			if (rec != null) {
				result.add(rec);
			}
			return rec;
		}

		public List<Record> querySince(Timestamp from, int minNumberOfPictures, int maxNumberOfItems) {
			Condition cond = Pictures.PICTURES.CREATED_ON.greaterThan(from);
			List<Record> result = query(1, cond);
			if (result.size() < minNumberOfPictures) {
				result = query(0, cond);
			}
			if (result.size() > maxNumberOfItems) {
				reduceListSize(maxNumberOfItems, result);
			}
			return result;
		}

		private void reduceListSize(int maxNumberOfItems, List<Record> result) {
			while (result.size() > maxNumberOfItems) {
				result.remove((int) (Math.random() * result.size()));
			}
		}

		private Record query(int voteLimit, Integer maxRows, Integer notFromLocation) {
			Result<Record> recList = query(voteLimit, null, maxRows, notFromLocation, null, null);
			if (recList.isNotEmpty()) {
				return recList.get((int) (Math.random() * recList.size()));
			}
			return null;
		}

		private Result<Record> query(int voteLimit, Condition cond) {
			return query(voteLimit, null, null, null, cond, null);
		}

		private Result<Record> query(int voteLimit, Integer startPos, Integer maxRows, Integer notFromLocation, Condition cond,
				Integer userId) {
			if (userId == null) {
				userId = -1;
			}
			return limit(startPos, maxRows, orderBy(where(voteLimit, notFromLocation, cond, from(select(), userId)))).fetch();
		}

		private SelectSelectStep<Record> select() {
			return create.select().select(Pictures.PICTURES.ID.as("picture_Id"), Location.LOCATION.ID, Location.LOCATION.OFFICIAL_NAME,
					Location.LOCATION.CITY, Users.USERS.DISPLAYNAME, Pictures.PICTURES.FILENAME, Pictures.PICTURES.CAPTION,
					UsersPicturesVotes.USERS_PICTURES_VOTES.CREATED_ON.as("vote_Created_On"));
		}

		private SelectOnConditionStep<Record> from(SelectSelectStep<Record> select, int userId) {
			return select
					.from(Location.LOCATION)
					.join(Pictures.PICTURES, JoinType.JOIN)
					.on(Location.LOCATION.ID.equal(Pictures.PICTURES.FK_LOCATION))
					.join(Users.USERS, JoinType.JOIN)
					.on(Pictures.PICTURES.FK_USER.equal(Users.USERS.ID))
					.leftOuterJoin(UsersPicturesVotes.USERS_PICTURES_VOTES)
					.on(UsersPicturesVotes.USERS_PICTURES_VOTES.FK_USER.equal(userId).and(
							UsersPicturesVotes.USERS_PICTURES_VOTES.FK_PICTURE.equal(Pictures.PICTURES.ID)));
		}

		private SelectConditionStep<Record> where(int voteLimit, Integer notFromLocation, Condition cond,
				SelectOnConditionStep<Record> tables) {
			SelectConditionStep<Record> where = tables.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity)).and(
					Pictures.PICTURES.UP_VOTES.greaterOrEqual(voteLimit));

			if (notFromLocation != null) {
				where = where.and(Pictures.PICTURES.FK_LOCATION.notEqual(notFromLocation));
			}

			if (cond != null) {
				where = where.and(cond);
			}
			return where;
		}

		private SelectSeekStep1<Record, Timestamp> orderBy(SelectConditionStep<Record> where) {
			return where.orderBy(Pictures.PICTURES.CREATED_ON.desc());
		}

		private ResultQuery<Record> limit(Integer startPos, Integer maxRows, SelectSeekStep1<Record, Timestamp> selectSeekStep) {
			if (maxRows != null) {
				if (startPos == null) {
					startPos = 0;
				}
				return selectSeekStep.limit(startPos, maxRows);
			} else {
				return selectSeekStep;
			}
		}
	}
}
