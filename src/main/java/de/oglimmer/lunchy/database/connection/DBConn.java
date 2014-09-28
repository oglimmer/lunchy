package de.oglimmer.lunchy.database.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.jooq.SQLDialect;

import com.google.gson.JsonObject;
import com.mchange.v2.c3p0.DataSources;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import de.oglimmer.lunchy.services.LunchyProperties;
import de.oglimmer.lunchy.services.MBeanService;

@Slf4j
public enum DBConn {
	INSTANCE;

	@Getter
	private SQLDialect sqlDialect;
	private String registeredDriver = "uninitialized";
	private DataSource pooledDS;

	@SneakyThrows(value = SQLException.class)
	public Connection getUnPooled() {
		setupEnv();
		log.debug("Creating single-use-con for {} with user {}", LunchyProperties.INSTANCE.getDbServerUrl()
				+ LunchyProperties.INSTANCE.getDbSchema(), LunchyProperties.INSTANCE.getDbUser());
		return DriverManager.getConnection(LunchyProperties.INSTANCE.getDbServerUrl() + LunchyProperties.INSTANCE.getDbSchema(),
				LunchyProperties.INSTANCE.getDbUser(), LunchyProperties.INSTANCE.getDbPassword());
	}

	@SneakyThrows(value = SQLException.class)
	public Connection get() {
		return pooledDS.getConnection();
	}

	private void setupEnv() {
		if (!registeredDriver.equals(LunchyProperties.INSTANCE.getDbDriver())) {
			registeredDriver = LunchyProperties.INSTANCE.getDbDriver();
			try {
				Class.forName(LunchyProperties.INSTANCE.getDbDriver());
			} catch (ClassNotFoundException e) {
				log.error("Failed to load jdbc driver", e);
			}
			switch (LunchyProperties.INSTANCE.getDbDriver()) {
			case "com.mysql.jdbc.Driver":
				sqlDialect = SQLDialect.MYSQL;
				break;
			case "org.hsqldb.jdbc.JDBCDriver":
				sqlDialect = SQLDialect.HSQLDB;
				break;
			default:
				throw new RuntimeException("Unsupported jdbc-driver:" + LunchyProperties.INSTANCE.getDbDriver());
			}
		}
	}

	@SneakyThrows(value = { SQLException.class })
	public void setupDriver() {
		setupEnv();
		log.debug("Creating con-pool for {} with user {}",
				LunchyProperties.INSTANCE.getDbServerUrl() + LunchyProperties.INSTANCE.getDbSchema(),
				LunchyProperties.INSTANCE.getDbUser());

		DataSource unpooledDS = DataSources.unpooledDataSource(LunchyProperties.INSTANCE.getDbServerUrl()
				+ LunchyProperties.INSTANCE.getDbSchema(), LunchyProperties.INSTANCE.getDbUser(),
				LunchyProperties.INSTANCE.getDbPassword());

		pooledDS = DataSources.pooledDataSource(unpooledDS, "poolDS");
	}

	public JsonObject getDriverStats() {
		JsonObject data = new JsonObject();
		MBeanService.copyAllAttributes(MBeanService.getPoolDSJMXName(), data);
		return data;
	}

	@SneakyThrows(value = SQLException.class)
	public void shutdownDriver() {
		log.debug("Stopping DBConn...");
		// close pool
		DataSources.destroy(pooledDS);

		// mysql thread
		try {
			AbandonedConnectionCleanupThread.shutdown();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// dont care
		}

		// JdbcDriver
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			log.debug("de-register: {}", drivers);
			DriverManager.deregisterDriver(drivers.nextElement());
		}
	}

}
