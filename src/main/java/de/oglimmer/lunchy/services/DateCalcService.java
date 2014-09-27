package de.oglimmer.lunchy.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateCalcService {

	private DateCalcService() {
	}

	public static Calendar findNever() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2038, 0, 19, 0, 0, 0);// largest date in mysql 2038-01-19
		return cal;
	}

	public static Calendar findNextMonday() {
		Calendar cal = GregorianCalendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		cal.add(Calendar.DAY_OF_YEAR, 7);

		return cal;
	}

	public static Timestamp getOneWeekAgo() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		return new Timestamp(cal.getTime().getTime());
	}

	public static Timestamp getNever() {
		return new Timestamp(findNever().getTime().getTime());
	}

	public static Timestamp getNextMonday() {
		return new Timestamp(findNextMonday().getTime().getTime());
	}

	public static boolean youngerThan(Date date, int field, int units) {
		Calendar now = GregorianCalendar.getInstance();
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.add(field, units);
		return now.before(cal);
	}

	public static Timestamp getNow() {
		return new Timestamp(new Date().getTime());
	}
}
