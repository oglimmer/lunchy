package de.oglimmer.lunchy.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public enum DateCalculation {
	INSTANCE;

	public Calendar findNever() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2038, 0, 19, 0, 0, 0);// largest date in mysql 2038-01-19
		return cal;
	}

	public Calendar findNextMonday() {
		Calendar cal = GregorianCalendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		cal.add(Calendar.DAY_OF_YEAR, 7);

		return cal;
	}

	public Timestamp getOneWeekAgo() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		return new Timestamp(cal.getTime().getTime());
	}

	public Timestamp getNever() {
		return new Timestamp(findNever().getTime().getTime());
	}

	public Timestamp getNextMonday() {
		return new Timestamp(findNextMonday().getTime().getTime());
	}
}
