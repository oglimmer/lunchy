package de.oglimmer.lunchy.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.oglimmer.lunchy.database.dao.TagDao;
import de.oglimmer.lunchy.services.CommunityService;

@Path("tags")
public class TagsResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String query(@Context HttpServletRequest request, @QueryParam(value = "selectedOffice") Integer fkOffice) {
		return convertToJson(TagDao.INSTANCE.getAllTags(fkOffice, CommunityService.get(request)));
	}

}
