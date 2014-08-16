package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.BeanMappingProvider;

@Data
public class User {

	private Integer id;
	private String email;
	private String displayname;
	private Timestamp createdon;
	private Timestamp lastlogin;
	private Integer permissions;
	private Timestamp passwordresettimestamp;
	private Timestamp longtimetimestamp;
	private Integer fkbaseoffice;

	public static User getInstance(UsersRecord userRec) {
		User userDto = new User();
		BeanMappingProvider.INSTANCE.getMapper().map(userRec, userDto);
		return userDto;
	}

}
