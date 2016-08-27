package de.oglimmer.lunchy.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.JsonObject;

import de.oglimmer.utils.AbstractProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailListService extends AbstractProperties {
	public static final EmailListService INSTANCE = new EmailListService();

	protected EmailListService() {
		super("lunchy-emaillist-service", "/emaillist.properties.json");
	}

	public boolean isLocationAvailable(int locationId) {
		return null != getJson().getJsonObject("location." + locationId);
	}

	public String getDescription(int locationId) {
		JsonObject locationJson = getJson().getJsonObject("location." + locationId);
		if (locationJson != null) {
			return locationJson.getString("description");
		}
		return null;
	}

	public boolean isUserEnabled(int locationId, int userId) {
		try {
			JsonObject locationJson = getJson().getJsonObject("location." + locationId);
			if (locationJson != null) {
				String usersFile = locationJson.getString("usersFile");
				if (usersFile != null && new File(usersFile).exists()) {
					try (Stream<String> stream = Files.lines(Paths.get(usersFile))) {
						return stream.anyMatch(s -> s.equals(Integer.toString(userId)));
					}
				}
			}
		} catch (IOException e) {
			log.error("Failed to get EmailListService", e);
		}
		return false;
	}

	public synchronized void add(int locationId, int userId) {
		try {
			JsonObject locationJson = getJson().getJsonObject("location." + locationId);
			if (locationJson != null) {
				String location = locationJson.getString("usersFile");
				try (FileOutputStream fos = new FileOutputStream(location, true)) {
					fos.write((Integer.toString(userId) + "\n").getBytes());
				}
			}
		} catch (IOException e) {
			log.error("Failed to add to EmailListService", e);
		}
	}

	public synchronized void remove(int locationId, int userId) {
		try {
			JsonObject locationJson = getJson().getJsonObject("location." + locationId);
			if (locationJson != null) {
				String location = locationJson.getString("usersFile");
				try (Stream<String> stream = Files.lines(Paths.get(location))) {
					Collection<String> c = stream.collect(Collectors.toList());
					try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(location)))) {
						c.stream().filter(s -> !s.equals(Integer.toString(userId))).forEach(pw::println);
					}
				}
			}
		} catch (IOException e) {
			log.error("Failed to remove from EmailListService", e);
		}
	}

}
