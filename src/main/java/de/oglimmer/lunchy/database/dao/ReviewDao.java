package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.SqlExecCallback;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;

public enum ReviewDao implements Dao<ReviewsRecord> {
	INSTANCE;

	public ReviewsRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(REVIEWS, REVIEWS.ID.equal(id).and(REVIEWS.FK_COMMUNITY.equal(fkCommunity)));
	}

	public List<ReviewsRecord> getList(int fklocation) {
		return DB.query(REVIEWS, REVIEWS.FK_LOCATION.equal(fklocation), REVIEWS.LAST_UPDATE.desc(), ReviewsRecord.class);
	}

	public void store(final ReviewsRecord review) {
		SqlExecCallback updateTurnAroundOnParentLocation = new SqlExecCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void exec(DSLContext context) {
				context.update(Location.LOCATION).set(Location.LOCATION.TURN_AROUND_TIME, getSubSelectToCalcAvgTurnAroundTime(context))
						.where(Location.LOCATION.ID.equal(review.getFkLocation())).execute();
			}

			@SuppressWarnings("rawtypes")
			private Select getSubSelectToCalcAvgTurnAroundTime(DSLContext context) {
				return context.select(DSL.avg(DSL.isnull(Reviews.REVIEWS.ON_SITE_TIME, 0).add(DSL.isnull(Reviews.REVIEWS.TRAVEL_TIME, 0))))
						.from(Reviews.REVIEWS).where(Reviews.REVIEWS.FK_LOCATION.equal(Location.LOCATION.ID));
			}
		};
		DB.store(review, updateTurnAroundOnParentLocation);
	}

	public void delete(int id, int fkCommunity) {
		DB.delete(REVIEWS, REVIEWS.ID, id, fkCommunity);
	}

	public Integer hasUserReview(Integer fklocation, Integer fkUser) {
		Record rec = DB.fetchOn(REVIEWS, REVIEWS.FK_LOCATION.equal(fklocation).and(REVIEWS.FK_USER.equal(fkUser)));
		if (rec != null) {
			return rec.getValue(REVIEWS.ID);
		}
		return null;
	}

}
