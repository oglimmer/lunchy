package de.oglimmer.lunchy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

public class DB {

	@BeforeClass
	public static void init() {
		DBConn.INSTANCE.setupDriver();
	}

	@AfterClass
	public static void shutdown() {
		DBConn.INSTANCE.shutdownDriver();
	}

	@Test
	public void testDB() throws SQLException {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<? extends Record> result = create.select().from(Users.USERS).fetch();

			for (UsersRecord rec : (Result<UsersRecord>) result) {

				int id = rec.getId();
				String email = rec.getEmail();
				String displayName = rec.getDisplayname();

				System.out.println("ID: " + id + " email: " + email + " name: " + displayName);
			}
		}
	}

	@Test
	public void createUser() throws SQLException {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UsersRecord user = create.newRecord(Users.USERS);
			user.setCreatedOn(new Timestamp(new Date().getTime()));
			user.setDisplayname("Oli");
			user.setEmail("oli@zimpasser.de");
			user.setLastLogin(new Timestamp(new Date().getTime()));
			user.setPassword(BCrypt.hashpw("foo", BCrypt.gensalt()));
			user.setPermissions(0);
			user.store();
		}
	}

}
