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

@Path("/updates")
public class UpdatesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@SneakyThrows(value = JSONException.class)
	public String query(@Context HttpServletRequest request) {
		JSONArray arr = new JSONArray();
		for (de.oglimmer.lunchy.database.UpdatesDao.ResultParam o : UpdatesDao.INSTANCE.get(3)) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("text", o.getText());
			jsonObj.put("ref", o.getRef());
			arr.put(jsonObj);
		}
		return arr.toString();
	}

}
