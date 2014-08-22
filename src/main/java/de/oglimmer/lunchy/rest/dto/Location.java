package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.ForeignKey;

@Data
public class Location {

	private Integer id;
	private Integer fkOffice;
	private String officialName;
	private String streetName;
	private String address;
	private String city;
	private String zip;
	private String country;
	private String url;
	private String comment;
	private Integer turnAroundTime;
	private Timestamp createdOn;
	private Timestamp lastUpdate;
	private Double geoLat;
	private Double geoLng;
	private String tags;

	@ForeignKey(dao = "user", refColumnLabel = "displayname", refColumnName = "fkUser")
	private String creationUser;

}