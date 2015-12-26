package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.generated.tables.Location.LOCATION;
import static de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS;
import static de.oglimmer.lunchy.database.generated.tables.Users.USERS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.rest.dto.LocationResponse;
import de.oglimmer.lunchy.rest.resources.FinderResource.QueryResponse;
import de.oglimmer.lunchy.rest.resources.FinderResource.QueryResponseSectionLine;
import de.oglimmer.lunchy.rest.resources.FinderResource.RatingResponse;

public enum FinderDao {
	INSTANCE;

	public List<QueryResponse> query(Set<String> tags, Set<String> partners, Integer maxTime, Integer selectedMinRating, int fkCommunity,
			Integer fkCurrentUser, int selectedOffice) {
		Result<LocationRecord> locations = queryLocations(tags, maxTime, fkCommunity, selectedOffice);
		Result<Record> reviews = queryReviews(partners, getLocationId(locations), fkCurrentUser);

		Map<Integer, Map<String, Integer>> reviewsByLocation = groupReviewsByLocation(reviews);

		List<QueryResponse> resultQueryList = new ArrayList<>();

		for (LocationRecord locRec : locations) {

			QueryResponseSectionLine qrsl = new QueryResponseSectionLine();
			qrsl.setRatings(toList(reviewsByLocation.get(locRec.getId())));
			int minRating = getMinRating(qrsl.getRatings());			
			if (hasNotRating1(minRating) && hasMinimumRating(selectedMinRating, minRating)) {

				qrsl.setLocation(BeanMappingProvider.INSTANCE.map(locRec, LocationResponse.class));

				QueryResponse qr = get(resultQueryList, minRating);
				if (qr == null) {
					qr = new QueryResponse();
					qr.setMinRating(minRating);
					qr.setSectionLines(new ArrayList<QueryResponseSectionLine>());
					resultQueryList.add(qr);
				}

				qr.getSectionLines().add(qrsl);
			}
		}

		Collections.sort(resultQueryList, new Comparator<QueryResponse>() {
			@Override
			public int compare(QueryResponse o1, QueryResponse o2) {
				return -1 * Integer.compare(o1.getMinRating(), o2.getMinRating());
			}
		});

		return resultQueryList;
	}

	private boolean hasMinimumRating(Integer selectedMinRating, int minRating) {
		return selectedMinRating == null || minRating0(selectedMinRating, minRating)
				|| minRatingIsHigher(selectedMinRating, minRating);
	}

	private boolean minRating0(Integer selectedMinRating, int minRating) {
		return minRating == 0 && selectedMinRating == 0;
	}

	private boolean minRatingIsHigher(Integer selectedMinRating, int minRating) {
		return minRating >= selectedMinRating && selectedMinRating != 0;
	}

	private boolean hasNotRating1(int minRating) {
		return minRating != 1;
	}

	private List<RatingResponse> toList(Map<String, Integer> reviews) {
		if (reviews == null) {
			return Collections.emptyList();
		}
		List<RatingResponse> reviewList = new ArrayList<>();
		for (Map.Entry<String, Integer> en : reviews.entrySet()) {
			reviewList.add(new RatingResponse(en.getKey(), en.getValue()));
		}
		return reviewList;
	}

	private QueryResponse get(List<QueryResponse> resultQueryList, int minRating) {
		for (QueryResponse qr : resultQueryList) {
			if (qr.getMinRating() == minRating) {
				return qr;
			}
		}
		return null;
	}

	private Map<Integer, Map<String, Integer>> groupReviewsByLocation(Result<Record> reviews) {
		Map<Integer, Map<String, Integer>> uberMap = new HashMap<Integer, Map<String, Integer>>();

		for (Record reviewRec : reviews) {
			int fkLocation = reviewRec.getValue(REVIEWS.FK_LOCATION);
			int rating = reviewRec.getValue(REVIEWS.RATING);
			String displayname = reviewRec.getValue(USERS.DISPLAYNAME);

			Map<String, Integer> locMap = uberMap.get(fkLocation);
			if (locMap == null) {
				locMap = new HashMap<>();
				uberMap.put(fkLocation, locMap);
			}

			locMap.put(displayname, rating);
		}

		return uberMap;
	}

	private int getMinRating(List<RatingResponse> ratings) {
		if (ratings == null || ratings.isEmpty()) {
			return 0;
		}
		int min = Integer.MAX_VALUE;
		for (RatingResponse rating : ratings) {
			if (min > rating.getScore()) {
				min = rating.getScore();
			}
		}
		return min;
	}

	private Set<Integer> getLocationId(Result<LocationRecord> locations) {
		Set<Integer> locationIds = new HashSet<>();
		for (LocationRecord locRec : locations) {
			locationIds.add(locRec.getId());
		}
		return locationIds;
	}

	@SneakyThrows(value = SQLException.class)
	public Result<Record> queryReviews(Set<String> userNamess, Set<Integer> locationIds, Integer fkCurrentUser) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);

			return create
					.select()
					.select(REVIEWS.FK_LOCATION, USERS.DISPLAYNAME, REVIEWS.RATING)
					.from(REVIEWS.join(USERS).on(REVIEWS.FK_USER.equal(USERS.ID)))
					.where(REVIEWS.FK_LOCATION.in(locationIds).and(
							USERS.DISPLAYNAME.in(userNamess).or(REVIEWS.FK_USER.equal(fkCurrentUser)))).fetch();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public Result<LocationRecord> queryLocations(Set<String> tags, Integer maxTime, int fkCommunity, int fkOffice) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);

			SelectWhereStep<LocationRecord> sjs = create.selectFrom(LOCATION);

			Condition cond = DSL.falseCondition();
			for (String tag : tags) {
				cond = cond.or(LOCATION.TAGS.like("%" + tag + "%"));
			}

			Condition turnAroundIsNull = LOCATION.TURN_AROUND_TIME.isNull();
			Condition turnAroundMeetsMaxTime = LOCATION.TURN_AROUND_TIME.lessOrEqual(maxTime);
			Condition turnAround = turnAroundIsNull.or(turnAroundMeetsMaxTime);
			Condition fkCommunityCond = LOCATION.FK_COMMUNITY.equal(fkCommunity);
			Condition fkOfficeCond = LOCATION.FK_OFFICE.equal(fkOffice);
			return sjs.where(cond).and(turnAround.and(fkCommunityCond.and(fkOfficeCond))).fetch();
		}
	}

}
