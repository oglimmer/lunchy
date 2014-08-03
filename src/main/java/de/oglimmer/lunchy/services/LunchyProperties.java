package de.oglimmer.lunchy.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

public enum LunchyProperties {
	INSTANCE;

	private static final String LUNCHY_PROPERTIES = "lunchy.properties";
	private Properties prop = new Properties();

	@SneakyThrows(value = IOException.class)
	private LunchyProperties() {
		if (System.getProperty(LUNCHY_PROPERTIES) != null) {
			try (FileInputStream fis = new FileInputStream(System.getProperty(LUNCHY_PROPERTIES))) {
				prop.load(fis);
				System.out.println("Successfully loaded " + LUNCHY_PROPERTIES + " from " + System.getProperty(LUNCHY_PROPERTIES));
			}
		}
	}

	public String getDbUser() {
		return prop.getProperty("db.user", "root");
	}

	public String getDbPassword() {
		return prop.getProperty("db.password", "");
	}

	public String getDbUrl() {
		return prop.getProperty("db.url", "jdbc:mysql://localhost/oli_lunchy");
	}

}