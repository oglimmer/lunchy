package de.oglimmer.lunchy.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.SneakyThrows;

import org.apache.commons.lang3.RandomStringUtils;

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
		if (prop.getProperty("runtime.password") == null) {
			prop.setProperty("runtime.password", RandomStringUtils.randomAlphanumeric(8));
			System.out.println("Created random runtime.password: " + prop.getProperty("runtime.password"));
		}
	}

	public String getSmtpUser() {
		return prop.getProperty("smtp.user", System.getProperty("lunchy.smtp.user", ""));
	}

	public String getSmtpPassword() {
		return prop.getProperty("smtp.password", System.getProperty("lunchy.smtp.password", ""));
	}

	public String getSmtpHost() {
		return prop.getProperty("smtp.host", System.getProperty("lunchy.smtp.host", "localhost"));
	}

	public int getSmtpPort() {
		return Integer.parseInt(prop.getProperty("smtp.port", System.getProperty("lunchy.smtp.port", "-1")));
	}

	public boolean getSmtpSSL() {
		return Boolean.parseBoolean(prop.getProperty("smtp.ssl", System.getProperty("lunchy.smtp.ssl", "false")));
	}

	public String getSmtpFrom() {
		return prop
				.getProperty("smtp.from", System.getProperty("lunchy.smtp.from", "\"Lunchy-Updates\" <lunchy-updates@junta-online.net>"));
	}

	public String getDbUser() {
		return prop.getProperty("db.user", System.getProperty("lunchy.db.user", "root"));
	}

	public String getDbPassword() {
		return prop.getProperty("db.password", System.getProperty("lunchy.db.password", ""));
	}

	public String getDbServerUrl() {
		return prop.getProperty("db.url", System.getProperty("lunchy.db.url", "jdbc:mysql://127.0.0.1/"));
	}

	public String getDbSchema() {
		return prop.getProperty("db.schema", System.getProperty("lunchy.db.schema", "oli_lunchy"));
	}

	public String getDbDriver() {
		return prop.getProperty("db.driver", System.getProperty("lunchy.db.driver", "com.mysql.jdbc.Driver"));
	}

	public String getTmpPath() {
		return prop.getProperty("app.tmpdir", System.getProperty("java.io.tmpdir"));
	}

	public String getPictureDestinationPath() {
		return prop.getProperty("app.picturedir", System.getProperty("lunchy.picturedir", "FAILED_TO_GET_PICTURE_DIR"));
	}

	public String getBackupDestinationPath() {
		return prop.getProperty("app.backupdir", System.getProperty("lunchy.backupdir", null));
	}

	public boolean isEmailDisabled() {
		return Boolean.parseBoolean(prop.getProperty("email.disabled", System.getProperty("lunchy.email.disabled", "false")));
	}

	public boolean isEmailNotificationEnabled() {
		return Boolean.parseBoolean(prop.getProperty("email.notification", "false"));
	}

	public String getRuntimePassword() {
		return prop.getProperty("runtime.password");
	}

	public long getEmailNotifierUpdateFrequence() {
		return Integer.parseInt(prop.getProperty("email.notification.updateFrequence",
				System.getProperty("lunchy.email.notification.updateFrequence", "300")));
	}

	public String getSecureDomainPattern() {
		return prop.getProperty("secure.domain.pattern", "");
	}
	
	public String getDomain() {
		return prop.getProperty("app.domain", System.getProperty("lunchy.domain", "%s.lunchylunch.com"));
	}

}
