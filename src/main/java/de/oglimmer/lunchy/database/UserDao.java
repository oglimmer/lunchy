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
	public UsersRecord getUserByEmail(String email) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord rec = create.fetchOne(Users.USERS,
					Users.USERS.EMAIL.equalIgnoreCase(email));
			rec.attach(null);
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

}
