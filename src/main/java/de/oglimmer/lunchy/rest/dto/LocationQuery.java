package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.beanMapping.DozerAdapter;
import de.oglimmer.lunchy.beanMapping.RestDto;
import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Data
@EqualsAndHashCode(callSuper = true)
@RestDto
public class LocationQuery extends Location {

	public static String buildSql(Integer fkUser, int fkOffice) {
		String reviewedSubSql = fkUser == null ? "'0'" : "(select count(*) from reviews where location.id=reviews.fk_Location and fk_User="
				+ fkUser + ")";

		String sql = "select location.*, count(*) as number_Of_Reviews, max(reviews.last_Update) as last_Rating, avg(reviews.rating) as avg_Rating, "
				+ reviewedSubSql
				+ " as reviewed from location left JOIN reviews on location.id=reviews.fk_Location where fk_Office="
				+ fkOffice + " group by location.id";
		return sql;
	}

	private Integer numberOfReviews;
	private Float avgRating;
	private Timestamp lastRating;
	private boolean reviewed;

	public static LocationQuery getInstance(Record rawRec, int fkCommunity) {
		LocationQuery dto = BeanMappingProvider.INSTANCE.getMapper().map(new DozerAdapter(rawRec), LocationQuery.class);

		UsersRecord user = UserDao.INSTANCE.getById(rawRec.getValue("fk_User", Integer.class), fkCommunity);
		assert user != null : rawRec;
		dto.setCreationUser(user.getDisplayname());

		return dto;
	}

	// Integer numberOfReviews = lastRating != null ? rawRec.getValue("numberOfReviews", Integer.class) : 0;
	// lq.setNumberOfReviews(numberOfReviews);
	//
	// lq.setReviewed(rawRec.getValue("reviewed", Integer.class) != 0);
}