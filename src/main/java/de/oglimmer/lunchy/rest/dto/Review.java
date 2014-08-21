package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Data
public class Review {
	private Integer id;
	private String creationUser;
	private Integer fkLocation;
	private String comment;
	private Timestamp createdOn;
	private Timestamp lastUpdate;
	private String officialName;
	private Integer rating;
	private String favoriteMeal;
	private Integer travelTime;
	private Integer onSiteTime;

	public static Review getInstance(ReviewsRecord reviewRec) {
		Review reviewDto = new Review();
		BeanMappingProvider.INSTANCE.getMapper().map(reviewRec, reviewDto);

		UsersRecord user = UserDao.INSTANCE.getById(reviewRec.getFkUser(), reviewRec.getFkCommunity());
		reviewDto.setCreationUser(user.getDisplayname());

		return reviewDto;
	}
}
