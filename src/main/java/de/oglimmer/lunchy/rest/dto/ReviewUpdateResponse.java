package de.oglimmer.lunchy.rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import de.oglimmer.lunchy.beanMapping.ForeignKey;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewUpdateResponse extends ReviewResponse {

	@ForeignKey(dao = "location", refColumnLabel = "turnAroundTime", refColumnName = "fkLocation")
	private Integer locationTurnAroundTime;

}
