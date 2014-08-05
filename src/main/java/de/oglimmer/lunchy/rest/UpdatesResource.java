package de.oglimmer.lunchy.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.SneakyThrows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.lunchy.database.UpdatesDao;
import de.oglimmer.lunchy.database.UpdatesDao.ResultParam;

@Path("/updates")
public class UpdatesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SneakyThrows(value = JSONException.class)
	public String query(@Context HttpServletRequest request) {
		JSONArray resultArray = new JSONArray();
		for (ResultParam update : UpdatesDao.INSTANCE.get(10)) {
			resultArray.put(createJson(update));
		}
		return resultArray.toString();
	}

	private JSONObject createJson(ResultParam update) throws JSONException {
		JSONObject jsonObj = new JSONObject();
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
		default:
			throw new RuntimeException("Illegal type=" + update.getType());
		}
		jsonObj.put("text", text);
		jsonObj.put("ref", ref);
		jsonObj.put("icon", icon);
		return jsonObj;
	}

	private String setTextForReview(ResultParam update) {
		if ("I".equals(update.getUpdatetype())) {
			return "New review for " + update.getOfficialName() + " in " + update.getCity() + " by " + update.getUser();
		} else {
			return update.getUser() + " updated the review for " + update.getOfficialName() + " in " + update.getCity();
		}
	}

	private String setTextForLocation(ResultParam update) {
		if ("I".equals(update.getUpdatetype())) {
			return "New location " + update.getOfficialName() + " in " + update.getCity();
		} else {
			return update.getOfficialName() + " in " + update.getCity() + " was updated";
		}
	}
}
