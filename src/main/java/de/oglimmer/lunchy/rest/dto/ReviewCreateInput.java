package de.oglimmer.lunchy.rest.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReviewCreateInput extends ReviewUpdateInput {

	private Integer fkLocation;

}
