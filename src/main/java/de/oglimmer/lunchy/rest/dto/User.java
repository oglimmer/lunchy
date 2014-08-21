package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

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

	public static User getInstance(UsersRecord userRec) {
		User userDto = new User();
		BeanMappingProvider.INSTANCE.getMapper().map(userRec, userDto);
		return userDto;
	}

}
