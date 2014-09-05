package de.oglimmer.lunchy.rest.dto;

import lombok.Data;

@Data
public class OfficeUpdateInput {

	private String name;
	private Double geoLat;
	private Double geoLng;
	private Integer zoomfactor;
	private String country;

}
