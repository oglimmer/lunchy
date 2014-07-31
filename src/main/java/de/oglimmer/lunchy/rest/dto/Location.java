package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Location {
	private Integer id;
	private String officialname;
	private String streetname;
	private String address;
	private String city;
	private String zip;
	private String country;
	private String comment;
	private Integer turnaroundtime;
	private Timestamp createdon;
	private Timestamp lastupdate;
	private Integer fkuser;
}