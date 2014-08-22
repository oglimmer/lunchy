package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.ForeignKey;

@Data
public class Review {

	private Integer id;
	@ForeignKey(dao = "user", refColumnLabel = "displayname", refColumnName = "fkUser")
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

}
