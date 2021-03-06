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

	@Override
	public ReviewsRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(REVIEWS, REVIEWS.ID.equal(id).and(REVIEWS.FK_COMMUNITY.equal(fkCommunity)));
	}

	public void store(final ReviewsRecord review) {
		SqlExecCallback updateTurnAroundOnParentLocation = new SqlExecCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void exec(DSLContext context) {
				context.update(Location.LOCATION).set(Location.LOCATION.TURN_AROUND_TIME, getSubSelectToCalcAvgTurnAroundTime(context))
						.where(Location.LOCATION.ID.equal(review.getFkLocation())).execute();
				// HACK! the line above will set TURN_AROUND_TIME=0 if nobody set any times. As 0 doesn't make any sense, let's update them to null
				context.update(Location.LOCATION).set(Location.LOCATION.TURN_AROUND_TIME, (Integer) null)
						.where(Location.LOCATION.TURN_AROUND_TIME.equal(0)).execute();
				
			}

			@SuppressWarnings("rawtypes")
			private Select getSubSelectToCalcAvgTurnAroundTime(DSLContext context) {
				//select ifnull(avg(travel_Time),0)+ifnull(avg(on_Site_Time),0) from reviews where fk_location=?
				return context
						.select(DSL.isnull(DSL.avg(Reviews.REVIEWS.ON_SITE_TIME), 0)
								.add(DSL.isnull(DSL.avg(Reviews.REVIEWS.TRAVEL_TIME), 0)))
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

	@Override
	public List<?> getListByParent(int fklocation) {
		return DB.query(REVIEWS, REVIEWS.FK_LOCATION.equal(fklocation), REVIEWS.LAST_UPDATE.desc(), ReviewsRecord.class);
	}

}
