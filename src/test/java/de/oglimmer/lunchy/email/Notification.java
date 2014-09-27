package de.oglimmer.lunchy.email;

import org.junit.Test;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.services.DateCalcService;

public class Notification {

	@Test
	public void html() {
		DBConn.INSTANCE.setupDriver();

		UsersRecord rec = UserDao.INSTANCE.getById(1);

		rec.setLastEmailUpdate(DateCalcService.getOneWeekAgo());

		String html = NotificationEmailText.getHtml(rec, EmailUpdatesNotifier.INSTANCE.buildUpdates(rec),
				EmailUpdatesNotifier.INSTANCE.buildPictures(rec));

		System.out.println(html);

	}

}
