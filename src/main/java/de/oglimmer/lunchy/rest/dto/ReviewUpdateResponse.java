package de.oglimmer.lunchy.rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import de.oglimmer.lunchy.database.LocationDao;
import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.BeanMappingProvider;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReviewUpdateResponse extends Review {

	private Integer locationTurnAroundTime;

	public static ReviewUpdateResponse getInstance(ReviewsRecord reviewRec) {
		ReviewUpdateResponse reviewDto = new ReviewUpdateResponse();
		BeanMappingProvider.INSTANCE.getMapper().map(reviewRec, reviewDto);

		UsersRecord user = UserDao.INSTANCE.getById(reviewRec.getFkuser(), reviewRec.getFkcommunity());
		reviewDto.setCreationUser(user.getDisplayname());

		LocationRecord loc = LocationDao.INSTANCE.getById(reviewRec.getFklocation());
		reviewDto.setLocationTurnAroundTime(loc.getTurnaroundtime());

		return reviewDto;
	}
}
