package de.oglimmer.lunchy.services;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.SneakyThrows;

public enum LunchyProperties {
	INSTANCE;

	private static final boolean DEBUG = false;

	private static final String LUNCHY_PROPERTIES = "lunchy.properties";
	private JsonObject json = Json.createObjectBuilder().build();

	@SneakyThrows(value = IOException.class)
	private LunchyProperties() {
		String sourceLocation = System.getProperty(LUNCHY_PROPERTIES);
		if (sourceLocation != null) {
			if (sourceLocation.startsWith("memory:")) {
				try (InputStream is = new ByteArrayInputStream(
						sourceLocation.substring("memory:".length()).getBytes(StandardCharsets.UTF_8))) {
					readJson(sourceLocation, is);
				}
			} else {
				try (InputStream fis = new FileInputStream(sourceLocation)) {
					readJson(sourceLocation, fis);
				}
			}
		}
		if (json.getString("runtime.password", null) == null) {
			json = jsonObjectToBuilder(json).add("runtime.password", RandomStringUtils.randomAlphanumeric(8)).build();
			System.out.println("Created random runtime.password: " + json.getString("runtime.password"));
		}
		if (DEBUG) {
			System.out.println("Used config: " + prettyPrint(json));
		}
	}

	private String prettyPrint(JsonObject json) {
		Map<String, Object> properties = new HashMap<>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonWriterFactory writerFactory = Json.createWriterFactory(properties);

		StringWriter sw = new StringWriter();
		try (JsonWriter jsonWriter = writerFactory.createWriter(sw)) {
			jsonWriter.writeObject(json);

		}
		return sw.toString();
	}

	private void readJson(String sourceLocation, InputStream is) {
		try (JsonReader rdr = Json.createReader(is)) {
			json = rdr.readObject();
			System.out.println("Successfully loaded " + LUNCHY_PROPERTIES + " from " + sourceLocation);
		}
	}

	private JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
		JsonObjectBuilder job = Json.createObjectBuilder();
		for (Entry<String, JsonValue> entry : jo.entrySet()) {
			job.add(entry.getKey(), entry.getValue());
		}
		return job;
	}

	public String getSmtpUser() {
		return json.getString("smtp.user", System.getProperty("lunchy.smtp.user", ""));
	}

	public String getSmtpPassword() {
		return json.getString("smtp.password", System.getProperty("lunchy.smtp.password", ""));
	}

	public String getSmtpHost() {
		return json.getString("smtp.host", System.getProperty("lunchy.smtp.host", "localhost"));
	}

	public int getSmtpPort() {
		return json.getInt("smtp.port", Integer.parseInt(System.getProperty("lunchy.smtp.port", "-1")));
	}

	public boolean getSmtpSSL() {
		return json.getBoolean("smtp.ssl", Boolean.parseBoolean(System.getProperty("lunchy.smtp.ssl", "false")));
	}

	public String getSmtpFrom() {
		return json.getString("smtp.from",
				System.getProperty("lunchy.smtp.from", "\"Lunchy-Updates\" <lunchy-updates@junta-online.net>"));
	}

	public String getDbUser() {
		return json.getString("db.user", System.getProperty("lunchy.db.user", "root"));
	}

	public String getDbPassword() {
		return json.getString("db.password", System.getProperty("lunchy.db.password", ""));
	}

	public String getDbServerUrl() {
		return json.getString("db.url", System.getProperty("lunchy.db.url", "jdbc:mysql://127.0.0.1/"));
	}

	public String getDbSchema() {
		return json.getString("db.schema", System.getProperty("lunchy.db.schema", "oli_lunchy"));
	}

	public String getDbDriver() {
		return json.getString("db.driver", System.getProperty("lunchy.db.driver", "com.mysql.jdbc.Driver"));
	}

	public String getTmpPath() {
		return json.getString("app.tmpdir", System.getProperty("java.io.tmpdir"));
	}

	public String getPictureDestinationPath() {
		return json.getString("app.picturedir", System.getProperty("lunchy.picturedir", "FAILED_TO_GET_PICTURE_DIR"));
	}

	public String getBackupDestinationPath() {
		return json.getString("app.backupdir", System.getProperty("lunchy.backupdir", null));
	}

	public boolean isEmailDisabled() {
		return json.getBoolean("email.disabled",
				Boolean.parseBoolean(System.getProperty("lunchy.email.disabled", "false")));
	}

	public boolean isEmailNotificationEnabled() {
		return json.getBoolean("email.notification", false);
	}

	public String getRuntimePassword() {
		return json.getString("runtime.password");
	}

	public long getEmailNotifierUpdateFrequence() {
		return json.getInt("email.notification.updateFrequence",
				Integer.parseInt(System.getProperty("lunchy.email.notification.updateFrequence", "300")));
	}

	public String getSecureDomainPattern() {
		return json.getString("secure.domain.pattern", "");
	}

	public String getDomain() {
		return json.getString("app.domain", System.getProperty("lunchy.domain", "%s.lunchylunch.com"));
	}

	public String getIsBot() {
		JsonArray array = json.getJsonArray("app.bots");
		if (array != null) {
			return getRegExFromJsonArray(array);
		} else {
			return BotDetectionService.DEFAULT_BOT_REGEX;
		}
	}

	public String getIsNoBot() {
		JsonArray array = json.getJsonArray("app.no-bots");
		if (array != null) {
			return getRegExFromJsonArray(array);
		} else {
			return BotDetectionService.DEFAULT_NO_BOT_REGEX;
		}
	}

	private String getRegExFromJsonArray(JsonArray array) {
		if (array.isEmpty()) {
			return "";
		}
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < array.size(); i++) {
			String s = array.getString(i);
			if (buff.length() > 0) {
				buff.append("|");
			}
			buff.append(RegExService.INSTANCE.escape(s));
		}
		return ".*(" + buff.toString() + ").*";
	}

}
