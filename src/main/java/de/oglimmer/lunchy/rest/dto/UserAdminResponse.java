package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserAdminResponse extends UserResponse {

	private String email;
	private Timestamp createdOn;
	private Timestamp lastLogin;
	private Integer permissions;
	private Timestamp passwordResetTimestamp;
	private Timestamp longTimeTimestamp;
	private Integer fkBaseOffice;
	private Integer emailUpdates;

}
