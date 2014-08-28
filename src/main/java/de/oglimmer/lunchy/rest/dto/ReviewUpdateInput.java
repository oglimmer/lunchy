package de.oglimmer.lunchy.rest.dto;

import lombok.Data;

@Data
public class ReviewUpdateInput {

	private String comment;
	private Integer rating;
	private String favoriteMeal;
	private Integer travelTime;
	private Integer onSiteTime;

}
