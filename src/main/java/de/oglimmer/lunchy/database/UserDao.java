package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.SneakyThrows;

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
	public UsersRecord getUserByEmail(String email) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<? extends Record> result = create.select().from(Users.USERS)
					.where(Users.USERS.EMAIL.equalIgnoreCase(email)).fetch();

			for (UsersRecord rec : (Result<UsersRecord>) result) {
				rec.attach(null);
				return rec;
			}
		}
		return null;
	}

	@SneakyThrows(value = SQLException.class)
	public void store(UsersRecord user) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			user.attach(create.configuration());
			user.store();
		}
	}

}
