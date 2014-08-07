package de.oglimmer.lunchy.database.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import lombok.SneakyThrows;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import de.oglimmer.lunchy.services.LunchyProperties;

public enum DBConn {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public Connection get() {
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:lunchyDataStore");
	}

	@SneakyThrows(value = { IllegalAccessException.class, InstantiationException.class, SQLException.class, ClassNotFoundException.class })
	public void setupDriver() {

		Class.forName("com.mysql.jdbc.Driver").newInstance();

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(LunchyProperties.INSTANCE.getDbUrl(),
				LunchyProperties.INSTANCE.getDbUser(), LunchyProperties.INSTANCE.getDbPassword());

		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);

		// BUG in apache-dbcp (https://issues.apache.org/jira/browse/DBCP-412)
		poolableConnectionFactory.setPool(connectionPool);

		Class.forName("org.apache.commons.dbcp2.PoolingDriver");
		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

		driver.registerPool("lunchyDataStore", connectionPool);

	}

	@SneakyThrows(value = SQLException.class)
	public void printDriverStats() {
		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		ObjectPool<? extends Connection> connectionPool = driver.getConnectionPool("lunchyDataStore");

		System.out.println("NumActive: " + connectionPool.getNumActive());
		System.out.println("NumIdle: " + connectionPool.getNumIdle());
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
