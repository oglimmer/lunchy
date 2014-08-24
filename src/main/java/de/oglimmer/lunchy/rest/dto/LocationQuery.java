package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.beanMapping.DozerAdapter;
import de.oglimmer.lunchy.beanMapping.RestDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@RestDto
public class LocationQuery extends Location {

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