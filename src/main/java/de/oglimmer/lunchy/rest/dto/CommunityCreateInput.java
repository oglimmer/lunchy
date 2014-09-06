package de.oglimmer.lunchy.rest.dto;

import lombok.Data;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Data
public class CommunityCreateInput {

	private String spaceName;
	private String domain;
	private String email;
	private String displayname;
	private String password;
	private String officeName;
	private Double geoLat;
	private Double geoLng;
	private Integer zoomfactor;
	private String country;

	public CommunitiesRecord toCommunity() {
		CommunitiesRecord newRec = new CommunitiesRecord();
		newRec.setAdminEmail(email);
		newRec.setDomain(domain);
		newRec.setName(spaceName);
		return newRec;
	}

	public OfficesRecord toOffice() {
		OfficesRecord newRec = new OfficesRecord();
		newRec.setCountry(country);
		newRec.setGeoLat(geoLat);
		newRec.setGeoLng(geoLng);
		newRec.setName(officeName);
		newRec.setZoomfactor(zoomfactor);
		return newRec;
	}

	public UsersRecord toUser() {
		UsersRecord newRec = new UsersRecord();
		newRec.setDisplayname(displayname);
		newRec.setEmail(email);
		return newRec;
	}
}
