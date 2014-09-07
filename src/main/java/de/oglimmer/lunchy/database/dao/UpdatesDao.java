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
import org.jooq.Record7;
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

		public List<Record> queryTwoOfLatest(int numberOfPictures) {
			List<Record> result = new ArrayList<>();
			Record first = addPic(numberOfPictures, result, null);
			if (first != null) {
				addPic(numberOfPictures, result, first.getValue(Location.LOCATION.ID));
			}
			return result;
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
			Result<Record> recList = query(voteLimit, maxRows, notFromLocation, null);
			if (recList.isNotEmpty()) {
				return recList.get((int) (Math.random() * recList.size()));
			}
			return null;
		}

		private Result<Record> query(int voteLimit, Condition cond) {
			return query(voteLimit, null, null, cond);
		}

		private Result<Record> query(int voteLimit, Integer maxRows, Integer notFromLocation, Condition cond) {
			return limit(maxRows, orderBy(where(voteLimit, notFromLocation, cond, from(select())))).fetch();
		}

		private SelectSelectStep<Record> select() {
			return create.select().select(Location.LOCATION.ID, Location.LOCATION.OFFICIAL_NAME, Location.LOCATION.CITY,
					Users.USERS.DISPLAYNAME, Pictures.PICTURES.FILENAME, Pictures.PICTURES.CAPTION);
		}

		private SelectOnConditionStep<Record> from(SelectSelectStep<Record> select) {
			return select.from(Location.LOCATION).join(Pictures.PICTURES, JoinType.JOIN)
					.on(Location.LOCATION.ID.equal(Pictures.PICTURES.FK_LOCATION)).join(Users.USERS, JoinType.JOIN)
					.on(Pictures.PICTURES.FK_USER.equal(Users.USERS.ID));
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

		private ResultQuery<Record> limit(Integer maxRows, SelectSeekStep1<Record, Timestamp> selectSeekStep) {
			if (maxRows != null) {
				return selectSeekStep.limit(maxRows);
			} else {
				return selectSeekStep;
			}
		}
	}
}
