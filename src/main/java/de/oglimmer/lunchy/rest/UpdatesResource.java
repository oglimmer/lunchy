package de.oglimmer.lunchy.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.oglimmer.lunchy.database.UpdatesDao;
import de.oglimmer.lunchy.database.UpdatesDao.ResultParam;

@Path("/updates")
public class UpdatesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String query(@Context HttpServletRequest request) {
		JsonArray resultArray = new JsonArray();
		for (ResultParam update : UpdatesDao.INSTANCE.get(10)) {
			resultArray.add(createJson(update));
		}
		return resultArray.toString();
	}

	private JsonObject createJson(ResultParam update) {
		JsonObject jsonObj = new JsonObject();
		String text, icon, ref;
		switch (update.getType()) {
		case "L":
			text = setTextForLocation(update);
			icon = "glyphicon-tower";
			ref = "view/" + update.getId();
			break;
		case "R":
			text = setTextForReview(update);
			icon = "glyphicon-eye-open";
			ref = "view/" + update.getId();
			break;
		case "U":
			text = update.getUser() + " joined";
			icon = "glyphicon-user";
			ref = "";
			break;
		case "P":
			text = "New picture for " + update.getOfficialName() + " in " + update.getCity() + " by " + update.getUser();
			icon = "glyphicon-picture";
			ref = "view/" + update.getId();
			break;
		default:
			throw new RuntimeException("Illegal type=" + update.getType());
		}
		jsonObj.addProperty("text", text);
		jsonObj.addProperty("ref", ref);
		jsonObj.addProperty("icon", icon);
		return jsonObj;
	}

	private String setTextForReview(ResultParam update) {
		if ("N".equals(update.getUpdatetype())) {
			return "New review for " + update.getOfficialName() + " in " + update.getCity() + " by " + update.getUser();
		} else {
			return update.getUser() + " updated the review for " + update.getOfficialName() + " in " + update.getCity();
		}
	}

	private String setTextForLocation(ResultParam update) {
		if ("N".equals(update.getUpdatetype())) {
			return "New location " + update.getOfficialName() + " in " + update.getCity();
		} else {
			return update.getOfficialName() + " in " + update.getCity() + " was updated";
		}
	}
}
