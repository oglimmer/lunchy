package de.oglimmer.lunchy.database.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.jooq.SQLDialect;

import com.google.gson.JsonObject;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import de.oglimmer.lunchy.services.LunchyProperties;
import de.oglimmer.lunchy.services.MBeanServies;

@Slf4j
public enum DBConn {
	INSTANCE;

	@Getter
	private SQLDialect sqlDialect;
	private String registeredDriver = "uninitialized";

	@SneakyThrows(value = SQLException.class)
	public Connection getUnPooled() {
		setupEnv();
		log.debug("Creating single-use-con for {} with user {}",
				LunchyProperties.INSTANCE.getDbServerUrl() + LunchyProperties.INSTANCE.getDbSchema(), LunchyProperties.INSTANCE.getDbUser());
		return DriverManager.getConnection(LunchyProperties.INSTANCE.getDbServerUrl() + LunchyProperties.INSTANCE.getDbSchema(),
				LunchyProperties.INSTANCE.getDbUser(), LunchyProperties.INSTANCE.getDbPassword());
	}

	@SneakyThrows(value = SQLException.class)
	public Connection get() {
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:lunchyDataStore");
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

	@SneakyThrows(value = { SQLException.class, ClassNotFoundException.class })
	public void setupDriver() {
		setupEnv();
		log.debug("Creating con-pool for {} with user {}",
				LunchyProperties.INSTANCE.getDbServerUrl() + LunchyProperties.INSTANCE.getDbSchema(), LunchyProperties.INSTANCE.getDbUser());

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(LunchyProperties.INSTANCE.getDbServerUrl()
				+ LunchyProperties.INSTANCE.getDbSchema(), LunchyProperties.INSTANCE.getDbUser(), LunchyProperties.INSTANCE.getDbPassword());

		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);

		// BUG in apache-dbcp (https://issues.apache.org/jira/browse/DBCP-412)
		poolableConnectionFactory.setPool(connectionPool);

		Class.forName("org.apache.commons.dbcp2.PoolingDriver");
		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

		driver.registerPool("lunchyDataStore", connectionPool);

	}

	public JsonObject getDriverStats() {
		JsonObject data = new JsonObject();
		MBeanServies.copyAllAttributes("org.apache.commons.pool2:type=GenericObjectPool,name=pool", data);
		return data;
	}

	@SneakyThrows(value = SQLException.class)
	public void shutdownDriver() {
		// close pool
		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		driver.closePool("lunchyDataStore");

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
			DriverManager.deregisterDriver(drivers.nextElement());
		}
	}

}
