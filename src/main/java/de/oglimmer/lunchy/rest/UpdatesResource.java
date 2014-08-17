package de.oglimmer.lunchy.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.Data;
import de.oglimmer.lunchy.database.UpdatesDao;
import de.oglimmer.lunchy.database.UpdatesDao.ResultParam;

@Path("updates")
public class UpdatesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueryResultRow> query(@Context HttpServletRequest request) {
		List<QueryResultRow> resultList = new ArrayList<>();
		for (ResultParam update : UpdatesDao.INSTANCE.get(10)) {
			resultList.add(createResultRow(update));
		}
		return resultList;
	}

	private QueryResultRow createResultRow(ResultParam update) {
		QueryResultRow qrr = new QueryResultRow();
		switch (update.getType()) {
		case "L":
			qrr.setText(setTextForLocation(update));
			qrr.setIcon("glyphicon-tower");
			qrr.setRef("view/" + update.getId());
			break;
		case "R":
			qrr.setText(setTextForReview(update));
			qrr.setIcon("glyphicon-eye-open");
			qrr.setRef("view/" + update.getId());
			break;
		case "U":
			qrr.setText(update.getUser() + " joined");
			qrr.setIcon("glyphicon-user");
			break;
		case "P":
			qrr.setText("New picture for " + update.getOfficialName() + " in " + update.getCity() + " by " + update.getUser());
			qrr.setIcon("glyphicon-picture");
			qrr.setRef("view/" + update.getId());
			break;
		default:
			throw new RuntimeException("Illegal type=" + update.getType());
		}
		return qrr;
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

	@Data
	public static class QueryResultRow {
		private String text;
		private String ref;
		private String icon;
	}
}
