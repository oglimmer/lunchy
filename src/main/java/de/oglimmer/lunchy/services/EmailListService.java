package de.oglimmer.lunchy.services;

import javax.json.JsonObject;

import de.oglimmer.lunchy.database.dao.LocationUsersEmailDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationUsersEmailRecord;
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
		JsonObject locationJson = getJson().getJsonObject("location." + locationId);
		if (locationJson != null) {
			LocationUsersEmailRecord rec = LocationUsersEmailDao.INSTANCE.getByLocationUser(locationId, userId);
			return rec != null;
		}
		return false;
	}

	public synchronized void add(int locationId, int userId) {
		JsonObject locationJson = getJson().getJsonObject("location." + locationId);
		if (locationJson != null) {
			log.debug("Appending {} to {}", locationId, userId);
			LocationUsersEmailRecord rec = new LocationUsersEmailRecord();
			rec.setFkLocation(locationId);
			rec.setFkUser(userId);
			if (locationJson.containsKey("local-name")) {
				rec.setLocalName(locationJson.getString("local-name"));
			}
			LocationUsersEmailDao.INSTANCE.store(rec);
		}
	}

	public synchronized void remove(int locationId, int userId) {
		JsonObject locationJson = getJson().getJsonObject("location." + locationId);
		if (locationJson != null) {
			LocationUsersEmailRecord rec = LocationUsersEmailDao.INSTANCE.getByLocationUser(locationId, userId);
			if (rec != null) {
				LocationUsersEmailDao.INSTANCE.delete(rec.getId());
			}
		}
	}

}
