package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.RestDto;

@Data
@RestDto
public class MailImage {
	private String displayname;
	private String officialName;
	private String city;
	private String caption;
	private String filename;
	private String id;
	private String pictureId;
	private Timestamp voteCreatedOn;
}