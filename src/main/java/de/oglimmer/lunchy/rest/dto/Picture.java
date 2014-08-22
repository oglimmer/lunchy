package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.ForeignKey;

@Data
public class Picture {

	private Integer id;
	private String filename;
	private String caption;
	private Timestamp createdOn;
	@ForeignKey(dao = "user", refColumnLabel = "displayname", refColumnName = "fkUser")
	private String creationUser;
	private Integer fkLocation;

}
