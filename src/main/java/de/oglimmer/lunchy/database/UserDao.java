package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

public enum UserDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getById(Integer id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS, Users.USERS.ID.equal(id));
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getById(Integer id, int fkCommunity) {
		return getBy(Users.USERS.ID.equal(id), fkCommunity);
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getUserByEmail(String email, int fkCommunity) {
		return getBy(Users.USERS.EMAIL.equalIgnoreCase(email), fkCommunity);
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getUserByToken(String token, int fkCommunity) {
		return getBy(Users.USERS.PASSWORDRESETTOKEN.equal(token), fkCommunity);
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getByLongTimeToken(String longTimeToken, int fkCommunity) {
		return getBy(Users.USERS.LONGTIMETOKEN.equal(longTimeToken), fkCommunity);
	}

	private UsersRecord getBy(Condition cond, int fkCommunity) throws SQLException {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS, cond.and(Users.USERS.FKCOMMUNITY.equal(fkCommunity)));
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void store(UsersRecord user) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			user.attach(create.configuration());
			user.store();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<UsersRecord> query(int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<Record> result = create.select().from(Users.USERS).where(Users.USERS.FKCOMMUNITY.equal(fkCommunity)).fetch();

			List<UsersRecord> resultList = new ArrayList<>();
			for (Record rawRec : result) {
				UsersRecord rec = (UsersRecord) rawRec;
				rec.attach(null);
				resultList.add(rec);
			}

			return resultList;
		}
	}

}
