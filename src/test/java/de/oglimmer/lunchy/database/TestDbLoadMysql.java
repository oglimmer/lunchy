package de.oglimmer.lunchy.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

import de.oglimmer.lunchy.database.connection.DBConn;

@Ignore
public class TestDbLoadMysql extends TestDbLoad {

	@BeforeClass
	public static void warmUp() throws LiquibaseException, SQLException, IOException {
		System.setProperty("lunchy.db.url", "jdbc:mysql://127.0.0.1/");
		System.setProperty("lunchy.db.schema", "");
		try (Connection con = DBConn.INSTANCE.getUnPooled()) {
			try (Statement stat = con.createStatement()) {
				stat.execute("create database junittest");
			}
		}
		System.setProperty("lunchy.db.schema", "junittest");
		DBConn.INSTANCE.setupDriver();
		try (Connection con = DBConn.INSTANCE.get()) {
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(con));
			Liquibase liquibaseDD = new Liquibase("all_tables.xml", new ClassLoaderResourceAccessor(), database);
			liquibaseDD.update("junit-warmup");

			// Liquibase liquibaseDM = new Liquibase("all_data.xml", new ClassLoaderResourceAccessor(), database);
			// liquibaseDM.update("junit-warmup");
		}

		RndDataGenerator dataGen = new RndDataGenerator(0);
		dataGen.genCommunities();
		final CountDownLatch cdl = new CountDownLatch(4);
		ExecutorService execSer = Executors.newFixedThreadPool(4);
		for (int i = 1; i < 5; i++) {
			final int base = i;
			execSer.execute(new Runnable() {
				@Override
				public void run() {
					RndDataGenerator dataGen = new RndDataGenerator(base * RndDataGenerator.NUM_COMMUNITIES);
					dataGen.genCommunities();
					cdl.countDown();
				}
			});
		}
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown() throws LiquibaseException, SQLException {
		try (Connection con = DBConn.INSTANCE.get()) {
			try (Statement stat = con.createStatement()) {
				stat.execute("drop database junittest");
			}
		}
		DBConn.INSTANCE.shutdownDriver();
	}

}
