package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import de.oglimmer.lunchy.beanMapping.ForeignKey;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationResponse extends LocationCreateInput {

	private Integer id;
	private Integer turnAroundTime;
	private Timestamp createdOn;
	private Timestamp lastUpdate;
	private Double geoLat;
	private Double geoLng;
	private boolean geoMovedManually;

	@ForeignKey(dao = "user", refColumnLabel = "displayname", refColumnName = "fkUser")
	private String creationUser;

}
