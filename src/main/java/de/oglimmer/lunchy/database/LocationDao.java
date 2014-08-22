package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;
import static de.oglimmer.lunchy.database.generated.tables.Location.LOCATION;

import java.util.List;

import org.jooq.Record;

import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.rest.dto.LocationQuery;

public enum LocationDao implements Dao<LocationRecord> {
	INSTANCE;

	public LocationRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(LOCATION, LOCATION.ID.equal(id).and(LOCATION.FK_COMMUNITY.equal(fkCommunity)));
	}

	public void store(LocationRecord location) {
		DB.store(location);
	}

	public void delete(int id) {
		DB.delete(LOCATION, LOCATION.ID, id);
	}

	public List<Record> getList(Integer fkUser, int fkOffice) {
		return DB.query(LocationQuery.buildSql(fkUser, fkOffice));
	}
}
