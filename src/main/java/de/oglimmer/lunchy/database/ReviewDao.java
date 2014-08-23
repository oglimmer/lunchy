package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;
import static de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS;

import java.util.List;

import org.jooq.Record;

import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;

public enum ReviewDao implements Dao<ReviewsRecord> {
	INSTANCE;

	public ReviewsRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(REVIEWS, REVIEWS.ID.equal(id).and(REVIEWS.FK_COMMUNITY.equal(fkCommunity)));
	}

	public List<ReviewsRecord> getList(int fklocation) {
		return DB.query(REVIEWS, REVIEWS.FK_LOCATION.equal(fklocation), REVIEWS.LAST_UPDATE.desc(), ReviewsRecord.class);
	}

	public void store(ReviewsRecord review) {
		SqlCommand updateTurnAroundOnParentLocation = new SqlCommand(
				"update location set turn_Around_Time=(select avg(IFNULL(on_Site_Time,0)+IFNULL(travel_Time,0)) "
						+ "from reviews where reviews.fk_Location = location.id) where id=?", review.getFkLocation());

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
