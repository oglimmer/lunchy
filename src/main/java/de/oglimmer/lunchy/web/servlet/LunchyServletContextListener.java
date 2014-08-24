package de.oglimmer.lunchy.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.services.Email;
import de.oglimmer.lunchy.services.EmailUpdatesNotifier;
import de.oglimmer.lunchy.services.LunchyVersion;

@WebListener
public class LunchyServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LunchyVersion.INSTANCE.init(sce.getServletContext());
		DBConn.INSTANCE.setupDriver();
		EmailUpdatesNotifier.INSTANCE.startUp();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Email.INSTANCE.shutdown();
		DBConn.INSTANCE.shutdownDriver();
		EmailUpdatesNotifier.INSTANCE.shutdown();
	}

}
