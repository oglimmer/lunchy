package de.oglimmer.lunchy.rest.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import de.oglimmer.lunchy.database.TagDao;
import de.oglimmer.lunchy.services.Community;

@Path("tags")
public class TagsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String query(@Context HttpServletRequest request) {
		List<String> listOfTags = TagDao.INSTANCE.getAllTags(Community.get(request));
		JsonArray jsonResultList = new JsonArray();
		for (String tag : listOfTags) {
			jsonResultList.add(new JsonPrimitive(tag));
		}
		return jsonResultList.toString();
	}

}
