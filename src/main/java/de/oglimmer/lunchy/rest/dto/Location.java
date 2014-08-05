package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.BeanMappingProvider;

@Data
public class Location {
	private Integer id;
	private String officialname;
	private String streetname;
	private String address;
	private String city;
	private String zip;
	private String country;
	private String url;
	private String comment;
	private Integer turnaroundtime;
	private Timestamp createdon;
	private Timestamp lastupdate;
	private String creationUser;
	private Double geoLat;
	private Double geoLng;

	public static Location getInstance(LocationRecord locationRec) {
		Location locationDto = new Location();
		BeanMappingProvider.INSTANCE.getMapper().map(locationRec, locationDto);

		UsersRecord user = UserDao.INSTANCE.getById(locationRec.getFkuser());
		locationDto.setCreationUser(user.getDisplayname());

		return locationDto;
	}
}