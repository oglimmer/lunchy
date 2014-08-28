package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import de.oglimmer.lunchy.beanMapping.ForeignKey;

@Data
@EqualsAndHashCode(callSuper = true)
public class PictureResponse extends PictureCreateInput {

	private Integer id;
	@ForeignKey(dao = "user", refColumnLabel = "displayname", refColumnName = "fkUser")
	private String creationUser;
	private Timestamp createdOn;
	private String filename;

}
