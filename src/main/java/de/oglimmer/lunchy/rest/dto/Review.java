package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.rest.BeanMappingProvider;

@Data
public class Review {
	private Integer id;
	private Integer fkuser;
	private Integer fklocation;
	private String comment;
	private Timestamp createdon;
	private Timestamp lastupdate;
	private String officialname;
	private Integer rating;
	private String favoritemeal;

	public static Review getInstance(ReviewsRecord reviewRec) {
		Review reviewDto = new Review();
		BeanMappingProvider.INSTANCE.getMapper().map(reviewRec, reviewDto);
		return reviewDto;
	}
}