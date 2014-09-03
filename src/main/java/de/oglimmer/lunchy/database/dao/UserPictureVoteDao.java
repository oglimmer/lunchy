package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.SqlResultCallback;
import de.oglimmer.lunchy.database.generated.tables.Pictures;
import de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord;

public enum UserPictureVoteDao implements Dao<UsersPicturesVotesRecord> {
	INSTANCE;

	@Override
	public UsersPicturesVotesRecord getById(Integer id, Integer fkCommunity) {
		return DB
				.fetchOn(USERS_PICTURES_VOTES, USERS_PICTURES_VOTES.ID.equal(id).and(USERS_PICTURES_VOTES.FK_COMMUNITY.equal(fkCommunity)));
	}

	public void store(UsersPicturesVotesRecord review) {
		DB.store(review);
	}

	public void delete(int id, int fkCommunity) {
		DB.delete(USERS_PICTURES_VOTES, USERS_PICTURES_VOTES.ID, id, fkCommunity);
	}

	@Override
	public List<UsersPicturesVotesRecord> getListByParent(int fkUser) {
		return DB.query(USERS_PICTURES_VOTES, USERS_PICTURES_VOTES.FK_USER.equal(fkUser), USERS_PICTURES_VOTES.CREATED_ON.desc(),
				UsersPicturesVotesRecord.class);
	}

	public UsersPicturesVotesRecord getByParents(Integer fkPicture, Integer fkUser) {
		return DB.fetchOn(USERS_PICTURES_VOTES,
				USERS_PICTURES_VOTES.FK_PICTURE.equal(fkPicture).and(USERS_PICTURES_VOTES.FK_USER.equal(fkUser)));
	}

	public List<Record> getListByParent(final int fkLocation, final int fkUser) {
		List<Record> list = DB.query(new SqlResultCallback() {

			@Override
			public Result<?> fetch(DSLContext context) {
				return context
						.selectFrom(USERS_PICTURES_VOTES)
						.where(USERS_PICTURES_VOTES.FK_USER.equal(fkUser).and(
								USERS_PICTURES_VOTES.FK_PICTURE.in(DSL.select(Pictures.PICTURES.ID).from(Pictures.PICTURES)
										.where(Pictures.PICTURES.FK_LOCATION.equal(fkLocation))))).fetch();
			}
		});
		return list;
	}

}
