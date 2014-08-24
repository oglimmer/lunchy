package de.oglimmer.lunchy.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.oglimmer.lunchy.database.connection.DBConn;

public class TestDbLoadHsql extends TestDbLoad {

	@BeforeClass
	public static void warmUp() throws LiquibaseException, SQLException, IOException, ClassNotFoundException {
		System.setProperty("lunchy.db.driver", "org.hsqldb.jdbc.JDBCDriver");
		System.setProperty("lunchy.db.url", "jdbc:hsqldb:mem:mymemdb");
		// System.setProperty("lunchy.db.url", "jdbc:hsqldb:hsql://localhost/");
		// System.setProperty("lunchy.db.schema", "xdb");
		System.setProperty("lunchy.db.user", "sa");
		DBConn.INSTANCE.setupDriver();
		try (Connection con = DBConn.INSTANCE.get()) {
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(con));
			Liquibase liquibaseDD = new Liquibase("all_tables.xml", new ClassLoaderResourceAccessor(), database);
			liquibaseDD.update("junit-warmup");

			// Liquibase liquibaseDM = new Liquibase("all_data.xml", new ClassLoaderResourceAccessor(), database);
			// liquibaseDM.update("junit-warmup");
		}
		RndDataGenerator dataGen = new RndDataGenerator();
		dataGen.genCommunities();
	}

	@AfterClass
	public static void tearDown() throws LiquibaseException, SQLException {
		DBConn.INSTANCE.shutdownDriver();
	}

}
