package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.beanMapping.DozerAdapter;
import de.oglimmer.lunchy.beanMapping.RestDto;
import de.oglimmer.lunchy.database.ComposedSqlCommand;
import de.oglimmer.lunchy.database.SqlCommand;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@RestDto
public class LocationQuery extends Location {

	public static SqlCommand buildSql(Integer fkUser, int fkOffice) {
		String sql = "select location.*, count(*) as number_Of_Reviews, max(reviews.last_Update) as last_Rating, avg(reviews.rating) as avg_Rating, {0} as reviewed "
				+ "from location left JOIN reviews on location.id=reviews.fk_Location where fk_Office=? group by location.id";
		ComposedSqlCommand csc = new ComposedSqlCommand(sql, fkOffice);
		if (fkUser != null) {
			csc.add(new SqlCommand("(select count(*) from reviews where location.id=reviews.fk_Location and fk_User=?)", fkUser));
		} else {
			csc.add(new SqlCommand("0"));
		}
		return csc;
	}

	private Integer numberOfReviews;
	private Float avgRating;
	private Timestamp lastRating;
	private boolean reviewed;

	public static LocationQuery getInstance(Record rawRec, int fkCommunity) {
		LocationQuery dto = BeanMappingProvider.INSTANCE.map(new DozerAdapter(rawRec), LocationQuery.class);
		fixNumberOfReviews(rawRec, dto);
		return dto;
	}

	private static void fixNumberOfReviews(Record rawRec, LocationQuery dto) {
		// the outer join will always return one row (may it with one review or without any review)
		// if lastRating is null, there is no rating at all, so set numberOfReviews to 0
		Integer numberOfReviews = dto.getLastRating() != null ? rawRec.getValue("number_Of_Reviews", Integer.class) : 0;
		dto.setNumberOfReviews(numberOfReviews);
	}

}