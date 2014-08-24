package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Record7;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectConditionStep;
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
			DSLContext create = DB.getContext(conn);
			return queryDB(numberOfItems, fkCommunity, create);
		}
	}

	private Result<Record> queryDB(int numberOfItems, int fkCommunity, DSLContext create) {

		SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> locationSelect = create
				.select(DSL.val("L").as("type"),
						Location.LOCATION.OFFICIAL_NAME,
						Location.LOCATION.CITY,
						DSL.val("").as("user"),
						DSL.decode().value(Location.LOCATION.CREATED_ON).when(Location.LOCATION.LAST_UPDATE, "N").otherwise("U")
								.as("update_Type"), Location.LOCATION.ID, Location.LOCATION.LAST_UPDATE).from(Location.LOCATION)
				.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));

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

		SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> usersSelect = create
				.select(DSL.val("U").as("type"), DSL.val("").as("official_Name"), DSL.val("").as("city"),
						Users.USERS.DISPLAYNAME.as("user"), DSL.val("N").as("update_Type"), Users.USERS.ID, Users.USERS.CREATED_ON)
				.from(Users.USERS).where(Users.USERS.FK_COMMUNITY.equal(fkCommunity));

		SelectConditionStep<Record7<String, String, String, String, String, Integer, Timestamp>> pictureSelect = create
				.select(DSL.val("P").as("type"), Location.LOCATION.OFFICIAL_NAME, Location.LOCATION.CITY,
						Users.USERS.DISPLAYNAME.as("user"), DSL.val("N").as("update_Type"), Location.LOCATION.ID,
						Pictures.PICTURES.CREATED_ON).from(Location.LOCATION).join(Pictures.PICTURES, JoinType.JOIN)
				.on(Location.LOCATION.ID.equal(Pictures.PICTURES.FK_LOCATION)).join(Users.USERS, JoinType.JOIN)
				.on(Pictures.PICTURES.FK_USER.equal(Users.USERS.ID)).where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity));

		Select<Record> overAll = create.select().from(locationSelect.union(reviewsSelect).union(usersSelect).union(pictureSelect))
				.orderBy(DSL.val("last_Update").as("last_Update").desc()).limit(numberOfItems);

		return overAll.fetch();

	}

}
