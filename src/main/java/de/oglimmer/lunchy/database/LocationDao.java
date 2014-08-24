package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;
import static de.oglimmer.lunchy.database.generated.tables.Location.LOCATION;

import java.util.Arrays;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;

public enum LocationDao implements Dao<LocationRecord> {
	INSTANCE;

	public LocationRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(LOCATION, LOCATION.ID.equal(id).and(LOCATION.FK_COMMUNITY.equal(fkCommunity)));
	}

	public void store(LocationRecord location) {
		DB.store(location);
	}

	public void delete(int id, int fkCommunity) {
		DB.delete(LOCATION, LOCATION.ID, id, fkCommunity);
	}

	/**
	 * @param fkUser
	 * @param fkOffice
	 * @return items are transparently convertible to {@link de.oglimmer.lunchy.rest.dto.LocationQuery}
	 */
	public List<Record> getList(final Integer fkUser, final int fkOffice) {
		return DB.query(new SqlResultCallback() {
			@Override
			public Result<?> fetch(DSLContext create) {

				SelectSelectStep<?> select = create.select(Arrays.asList(Location.LOCATION.fields())).select(
						DSL.field("count(*)", Integer.class).as("number_Of_Reviews"),
						DSL.max(Reviews.REVIEWS.LAST_UPDATE).as("last_Rating"), DSL.avg(Reviews.REVIEWS.RATING).as("avg_Rating"));

				if (fkUser != null) {
					select.select(create.selectCount().from(Reviews.REVIEWS)
							.where(LOCATION.ID.equal(Reviews.REVIEWS.FK_LOCATION).and(Reviews.REVIEWS.FK_USER.equal(fkUser)))
							.asField("reviewed"));
				} else {
					select.select(DSL.val("0").as("reviewed"));
				}

				return select.from(LOCATION).join(Reviews.REVIEWS).on(LOCATION.ID.equal(Reviews.REVIEWS.FK_LOCATION))
						.where(LOCATION.FK_OFFICE.equal(fkOffice)).groupBy(LOCATION.ID).fetch();
			}
		});
	}
}
