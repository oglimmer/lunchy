package de.oglimmer.lunchy.database;

import java.util.List;

import org.jooq.Record;
import org.junit.Ignore;
import org.junit.Test;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.dao.UpdatesDao;
import de.oglimmer.lunchy.services.DateCalcService;

@Ignore
public class UpdatesDaoTest {

	@Test
	public void getPicturesNum() {
		DBConn.INSTANCE.setupDriver();

		List<Record> list = UpdatesDao.INSTANCE.getPictures(1);
		System.out.println(list);

	}

	@Test
	public void getPicturesFrom() {
		DBConn.INSTANCE.setupDriver();

		List<Record> list = UpdatesDao.INSTANCE.getPictures(DateCalcService.getOneWeekAgo(), 1, 5);
		System.out.println(list);

	}

}
