package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.BeanMappingProvider;

@Data
public class Picture {

	private Integer id;
	private String filename;
	private String caption;
	private Timestamp createdon;
	private String creationUser;
	private Integer fklocation;

	public static Picture getInstance(PicturesRecord reviewRec) {
		Picture pictureDto = new Picture();
		BeanMappingProvider.INSTANCE.getMapper().map(reviewRec, pictureDto);
		UsersRecord user = UserDao.INSTANCE.getById(reviewRec.getFkuser(), reviewRec.getFkcommunity());
		pictureDto.setCreationUser(user.getDisplayname());
		return pictureDto;
	}

}
