package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class User {

	private Integer id;
	private String email;
	private String displayname;
	private Timestamp createdOn;
	private Timestamp lastLogin;
	private Integer permissions;
	private Timestamp passwordResetTimestamp;
	private Timestamp longTimeTimestamp;
	private Integer fkBaseOffice;

}
