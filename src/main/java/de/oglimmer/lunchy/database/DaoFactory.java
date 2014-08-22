package de.oglimmer.lunchy.database;

import lombok.SneakyThrows;

import org.apache.commons.lang3.StringUtils;

public enum DaoFactory {
	INSTANCE;

	@SneakyThrows(value = { ClassNotFoundException.class, IllegalArgumentException.class, IllegalAccessException.class,
			NoSuchFieldException.class, SecurityException.class })
	@SuppressWarnings("rawtypes")
	public Dao<?> getDao(String dao) {
		Class<? extends Dao> clazz = Class.forName("de.oglimmer.lunchy.database." + StringUtils.capitalize(dao) + "Dao").asSubclass(
				Dao.class);
		return (Dao<?>) clazz.getField("INSTANCE").get(null);
	}
}
