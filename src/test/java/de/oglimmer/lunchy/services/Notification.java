package de.oglimmer.lunchy.services;

import org.junit.Test;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

public class Notification {

	@Test
	public void html() {
		DBConn.INSTANCE.setupDriver();

		UsersRecord rec = UserDao.INSTANCE.getById(1);

		rec.setLastEmailUpdate(DateCalculation.INSTANCE.getOneWeekAgo());

		String html = NotificationEmailText.INSTANCE.getHtml(rec, EmailUpdatesNotifier.INSTANCE.buildUpdates(rec),
				EmailUpdatesNotifier.INSTANCE.buildPictures(rec));

		System.out.println(html);

	}

}
