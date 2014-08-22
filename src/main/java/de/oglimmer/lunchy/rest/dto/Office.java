package de.oglimmer.lunchy.rest.dto;

import lombok.Data;

@Data
public class Office {

	private Integer id;
	private String name;
	private Double geoLat;
	private Double geoLng;
	private Integer zoomfactor;

}
