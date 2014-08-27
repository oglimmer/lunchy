package de.oglimmer.lunchy.rest.dto;

import lombok.Data;

@Data
public class LocationUpdateInput {

	private String officialName;
	private String streetName;
	private String address;
	private String city;
	private String zip;
	private String country;
	private String url;
	private String comment;
	private Double geoLat;
	private Double geoLng;
	private String tags;

}
