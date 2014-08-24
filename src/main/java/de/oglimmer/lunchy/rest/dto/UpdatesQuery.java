package de.oglimmer.lunchy.rest.dto;

import lombok.Data;

@Data
public class UpdatesQuery {
	private String text;
	private String ref;
	private String icon;
}