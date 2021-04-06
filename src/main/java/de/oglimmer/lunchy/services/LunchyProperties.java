package de.oglimmer.lunchy.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LunchyProperties implements LunchyReloadableProperties {
	public static final LunchyProperties INSTANCE = new LunchyProperties();

	private static final boolean DEBUG = false;

	private static final String LUNCHY_PROPERTIES = "lunchy.properties";

	private JsonObject json = Json.createObjectBuilder().build();
	private boolean running = true;
	private Thread propertyFileWatcherThread;
	private List<Runnable> reloadables = new ArrayList<>();
	private String sourceLocation;

	private LunchyProperties() {
		init();
	}

	private void init() {
		sourceLocation = System.getProperty(LUNCHY_PROPERTIES);
		if (sourceLocation != null) {
			try {
				if (sourceLocation.startsWith("memory:")) {
					try (InputStream is = new ByteArrayInputStream(
							sourceLocation.substring("memory:".length()).getBytes(StandardCharsets.UTF_8))) {
						readJson(is);
					}
				} else {
					try (InputStream fis = new FileInputStream(sourceLocation)) {
						readJson(fis);
					}
					if (propertyFileWatcherThread == null) {
						propertyFileWatcherThread = new Thread(new PropertyFileWatcher());
						propertyFileWatcherThread.start();
					}
				}
			} catch (IOException e) {
				log.error("Failed to load properties file " + sourceLocation, e);
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

	public LunchyReloadableProperties getReloadable() {
		return this;
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

	private void readJson(InputStream is) {
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
			return BotDetectionService.Constancts.DEFAULT_BOT_REGEX;
		}
	}

	public String getIsNoBot() {
		JsonArray array = json.getJsonArray("app.no-bots");
		if (array != null) {
			return getRegExFromJsonArray(array);
		} else {
			return BotDetectionService.Constancts.DEFAULT_NO_BOT_REGEX;
		}
	}

	public String getIpStackApiKey() {
		return json.getString("app.apiStackApiKey", System.getProperty("lunchy.apiStackApiKey", "no_key"));
	}

	private String getRegExFromJsonArray(JsonArray array) {
		if (array.isEmpty()) {
			return "";
		}
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < array.size(); i++) {
			String element = array.getString(i);
			if (!element.trim().isEmpty()) {
				if (buff.length() > 0) {
					buff.append("|");
				}
				buff.append(RegExService.INSTANCE.escape(element));
			}
		}
		if (buff.length() > 0) {
			return ".*(" + buff.toString() + ").*";
		} else {
			return "";
		}
	}

	public void registerOnReload(Runnable toCall) {
		reloadables.add(toCall);
	}

	void reload() {
		init();
		reloadables.forEach(c -> c.run());
	}

	public void shutdown() {
		running = false;
		propertyFileWatcherThread.interrupt();
	}

	class PropertyFileWatcher implements Runnable {

		public void run() {
			File toWatch = new File(sourceLocation);
			log.info("PropertyFileWatcher started");
			try {
				final Path path = FileSystems.getDefault().getPath(toWatch.getParent());
				try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
					path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
					while (running) {
						final WatchKey wk = watchService.take();
						for (WatchEvent<?> event : wk.pollEvents()) {
							// we only register "ENTRY_MODIFY" so the context is always a Path.
							final Path changed = (Path) event.context();
							if (changed.endsWith(toWatch.getName())) {
								log.debug("{} changed => reload", toWatch.getAbsolutePath());
								reload();
							}
						}
						boolean valid = wk.reset();
						if (!valid) {
							log.warn("The PropertyFileWatcher's key has been unregistered.");
						}
					}
				}
			} catch (InterruptedException e) {
			} catch (Exception e) {
				log.error("PropertyFileWatcher failed", e);
			}
			log.info("PropertyFileWatcher ended");
		}
	}

}
