package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.oglimmer.lunchy.database.LocationDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;

@Path("locations/{id}")
public class LocationResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public InputParam check(@PathParam("id") int id) {
		return new InputParam(LocationDao.INSTANCE.getById(id));
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputParam {

		private Integer id;
		private String officialname;
		private String streetname;
		private String address;
		private String city;
		private String zip;
		private String country;
		private String comment;
		private Integer turnAroundTime;
		private Timestamp createdOn;
		private Timestamp lastUpdate;
		private Integer fkUser;

		public InputParam(LocationRecord rec) {
			if (rec != null) {
				this.id = rec.getId();
				this.officialname = rec.getOfficialname();
				this.streetname = rec.getStreetname();
				this.address = rec.getAddress();
				this.city = rec.getCity();
				this.zip = rec.getZip();
				this.country = rec.getCountry();
				this.comment = rec.getComment();
				this.turnAroundTime = rec.getTurnaroundtime();
				this.createdOn = rec.getCreatedon();
				this.lastUpdate = rec.getLastupdate();
				this.fkUser = rec.getFkuser();
			}
		}
	}
}
