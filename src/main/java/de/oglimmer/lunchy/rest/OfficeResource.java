package de.oglimmer.lunchy.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.oglimmer.lunchy.database.OfficeDao;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.rest.dto.Office;

@Path("offices")
public class OfficeResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Office> queryReviews() {
		List<Office> resultList = new ArrayList<>();
		for (OfficesRecord officeRec : OfficeDao.INSTANCE.query()) {
			resultList.add(Office.getInstance(officeRec));
		}
		return resultList;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Office get(@PathParam("id") int id) {
		OfficesRecord officeRec = OfficeDao.INSTANCE.getById(id);
		return Office.getInstance(officeRec);
	}
}
