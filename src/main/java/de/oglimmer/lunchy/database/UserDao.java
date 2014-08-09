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
		return getBy(Users.USERS.ID.equal(id));
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getUserByEmail(String email) {
		return getBy(Users.USERS.EMAIL.equalIgnoreCase(email));
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getUserByToken(String token) {
		return getBy(Users.USERS.PASSWORDRESETTOKEN.equal(token));
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getByLongTimeToken(String longTimeToken) {
		return getBy(Users.USERS.LONGTIMETOKEN.equal(longTimeToken));
	}

	private UsersRecord getBy(Condition cond) throws SQLException {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS, cond);
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
	public List<UsersRecord> query() {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<Record> result = create.select().from(Users.USERS).fetch();

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
