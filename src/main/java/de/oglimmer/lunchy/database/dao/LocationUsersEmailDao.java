package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.LocationUsersEmail.LOCATION_USERS_EMAIL;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationUsersEmailRecord;

public enum LocationUsersEmailDao implements Dao<LocationUsersEmailRecord> {
	INSTANCE;

	@Override
	public LocationUsersEmailRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(LOCATION_USERS_EMAIL, LOCATION_USERS_EMAIL.ID.equal(id));
	}

	public void store(LocationUsersEmailRecord review) {
		DB.store(review);
	}

	public void delete(int id) {
		DB.delete(LOCATION_USERS_EMAIL, LOCATION_USERS_EMAIL.ID, id, null);
	}

	@Override
	public List<LocationUsersEmailRecord> getListByParent(int fkUser) {
		throw new NotImplementedException();
	}

	public LocationUsersEmailRecord getByLocationUser(int locationId, int userId) {
		return DB.fetchOn(LOCATION_USERS_EMAIL,
				LOCATION_USERS_EMAIL.FK_LOCATION.equal(locationId).and(LOCATION_USERS_EMAIL.FK_USER.eq(userId)));
	}

}
