package de.oglimmer.lunchy.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.oglimmer.lunchy.database.connection.DBConn;

@WebListener
public class LunchyServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DBConn.INSTANCE.setupDriver();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		DBConn.INSTANCE.shutdownDriver();
	}

}
