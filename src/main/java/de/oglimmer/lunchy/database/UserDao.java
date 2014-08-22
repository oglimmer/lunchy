package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;
import static de.oglimmer.lunchy.database.generated.tables.Users.USERS;

import java.util.List;

import org.jooq.Condition;

import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

public enum UserDao implements Dao<UsersRecord> {
	INSTANCE;

	/**
	 * Method does not filter by fkCommunity!
	 * 
	 * @param id
	 * @return
	 */
	public UsersRecord getById(Integer id) {
		return DB.fetchOn(USERS, USERS.ID.equal(id));
	}

	public UsersRecord getById(Integer id, Integer fkCommunity) {
		return getBy(USERS.ID.equal(id), fkCommunity);
	}

	public UsersRecord getUserByEmail(String email, int fkCommunity) {
		return getBy(USERS.EMAIL.equalIgnoreCase(email), fkCommunity);
	}

	public UsersRecord getUserByToken(String token, int fkCommunity) {
		return getBy(USERS.PASSWORD_RESET_TOKEN.equal(token), fkCommunity);
	}

	public UsersRecord getByLongTimeToken(String longTimeToken, int fkCommunity) {
		return getBy(USERS.LONG_TIME_TOKEN.equal(longTimeToken), fkCommunity);
	}

	private UsersRecord getBy(Condition cond, int fkCommunity) {
		return DB.fetchOn(USERS, cond.and(USERS.FK_COMMUNITY.equal(fkCommunity)));
	}

	public void store(UsersRecord user) {
		DB.store(user);
	}

	public List<UsersRecord> query(int fkCommunity) {
		return DB.query(USERS, USERS.FK_COMMUNITY.equal(fkCommunity), USERS.EMAIL.asc(), UsersRecord.class);
	}
}
