package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
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
			rec.attach(null);
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getUserByEmail(String email) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS, Users.USERS.EMAIL.equalIgnoreCase(email));
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
	public UsersRecord getUserByToken(String token) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS, Users.USERS.PASSWORDRESETTOKEN.equal(token));
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public UsersRecord getByLongTimeToken(String longTimeToken) {		
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS, Users.USERS.LONGTIMETOKEN.equal(longTimeToken));
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

}
