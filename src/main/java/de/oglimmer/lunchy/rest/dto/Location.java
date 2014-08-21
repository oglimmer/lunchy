package de.oglimmer.lunchy.rest.dto;

import java.sql.Timestamp;

import lombok.Data;
import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Data
public class Location {
	private Integer id;
	private Integer fkOffice;
	private String officialName;
	private String streetName;
	private String address;
	private String city;
	private String zip;
	private String country;
	private String url;
	private String comment;
	private Integer turnAroundTime;
	private Timestamp createdOn;
	private Timestamp lastUpdate;
	private String creationUser;
	private Double geoLat;
	private Double geoLng;
	private String tags;

	public static Location getInstance(LocationRecord locationRec) {
		Location locationDto = new Location();
		BeanMappingProvider.INSTANCE.getMapper().map(locationRec, locationDto);

		UsersRecord user = UserDao.INSTANCE.getById(locationRec.getFkUser(), locationRec.getFkCommunity());
		locationDto.setCreationUser(user.getDisplayname());

		return locationDto;
	}
}