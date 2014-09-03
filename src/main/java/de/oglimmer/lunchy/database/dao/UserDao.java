package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.Users.USERS;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import org.jooq.Condition;
import org.jooq.DSLContext;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Slf4j
public enum UserDao implements Dao<UsersRecord> {
	INSTANCE;

	private LoadingCache<IdCommunityTuple, UsersRecord> userRecordCache;

	private UserDao() {
		userRecordCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<IdCommunityTuple, UsersRecord>() {
					public UsersRecord load(IdCommunityTuple key) {
						return getBy(USERS.ID.equal(key.getId()), key.getFkCommunity());
					}
				});
	}

	/**
	 * Method does not filter by fkCommunity!
	 * 
	 * @param id
	 * @return
	 */	
	public UsersRecord getById(Integer id) {
		return DB.fetchOn(USERS, USERS.ID.equal(id));
	}

	@Override
	public UsersRecord getById(Integer id, Integer fkCommunity) {
		try {
			return userRecordCache.get(new IdCommunityTuple(id, fkCommunity));
		} catch (ExecutionException e) {
			log.error("Failed to get UserRecord from cache", e);
			return getBy(USERS.ID.equal(id), fkCommunity);
		}
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
		userRecordCache.refresh(new IdCommunityTuple(user.getId(), user.getFkCommunity()));
	}

	@Override
	public List<?> getListByParent(int fkCommunity) {
		return DB.query(USERS, USERS.FK_COMMUNITY.equal(fkCommunity), USERS.EMAIL.asc(), UsersRecord.class);
	}

	public void resetCache() {
		userRecordCache.invalidateAll();
	}

	public List<UsersRecord> getReadyForNotification() {
		return DB.query(USERS, USERS.NEXT_EMAIL_UPDATE.lessThan(new Timestamp(new Date().getTime())), USERS.ID.asc(), UsersRecord.class);
	}

	@SneakyThrows(value = SQLException.class)
	public boolean updateLastEmailUpdateTimeStamp(UsersRecord rec) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			int res = create.update(Users.USERS).set(USERS.LAST_EMAIL_UPDATE, new Timestamp(new Date().getTime()))
					.where(USERS.ID.equal(rec.getId()).and(USERS.LAST_EMAIL_UPDATE.eq(rec.getLastEmailUpdate()))).execute();
			return res > 0;
		}
	}

	@Value
	private class IdCommunityTuple {
		private Integer id;
		private Integer fkCommunity;
	}

}
