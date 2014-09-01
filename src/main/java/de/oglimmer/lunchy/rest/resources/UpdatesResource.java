package de.oglimmer.lunchy.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.beanMapping.DozerAdapter;
import de.oglimmer.lunchy.database.dao.UpdatesDao;
import de.oglimmer.lunchy.rest.dto.UpdatesQuery;
import de.oglimmer.lunchy.services.Community;
import de.oglimmer.lunchy.services.EmailUpdatesNotifier.MailImage;

@Path("updates")
public class UpdatesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public QueryResponse query(@Context HttpServletRequest request) {
		List<UpdatesQuery> updateQueryList = getQueryUpdates(request);
		List<MailImage> picturesList = getPictures(request);
		return new QueryResponse(updateQueryList, picturesList);
	}

	private List<MailImage> getPictures(HttpServletRequest request) {
		List<Record> recordList = UpdatesDao.INSTANCE.getPictures(2, Community.get(request));
		return BeanMappingProvider.INSTANCE.mapListCustomDto(recordList, MailImage.class);
	}

	private List<UpdatesQuery> getQueryUpdates(HttpServletRequest request) {
		List<UpdatesQuery> resultList = new ArrayList<>();
		for (Record update : UpdatesDao.INSTANCE.get(10, Community.get(request))) {
			resultList.add(createResultRow(new DozerAdapter(update)));
		}
		return resultList;
	}

	public UpdatesQuery createResultRow(DozerAdapter updateRec) {
		UpdatesQuery updateQuery = new UpdatesQuery();
		switch (updateRec.getString("type")) {
		case "L":
			createResultRowLocation(updateRec, updateQuery);
			break;
		case "R":
			createResultRowReview(updateRec, updateQuery);
			break;
		case "U":
			createResultRowUser(updateRec, updateQuery);
			break;
		case "P":
			createResultRowPicture(updateRec, updateQuery);
			break;
		default:
			throw new RuntimeException("Illegal type=" + updateRec.getValue("type"));
		}
		return updateQuery;
	}

	private void createResultRowPicture(DozerAdapter update, UpdatesQuery qrr) {
		qrr.setText("New picture for " + update.getValue("officialName") + " in " + update.getValue("city") + " by "
				+ update.getValue("user"));
		qrr.setIcon("glyphicon-picture");
		qrr.setRef("view/" + update.getValue("id"));
	}

	private void createResultRowUser(DozerAdapter update, UpdatesQuery qrr) {
		qrr.setText(update.getValue("user") + " joined");
		qrr.setIcon("glyphicon-user");
	}

	private void createResultRowReview(DozerAdapter update, UpdatesQuery qrr) {
		qrr.setText(setTextForReview(update));
		qrr.setIcon("glyphicon-eye-open");
		qrr.setRef("view/" + update.getValue("id"));
	}

	private void createResultRowLocation(DozerAdapter update, UpdatesQuery qrr) {
		qrr.setText(setTextForLocation(update));
		qrr.setIcon("glyphicon-tower");
		qrr.setRef("view/" + update.getValue("id"));
	}

	private String setTextForReview(DozerAdapter update) {
		if ("N".equals(update.getValue("updateType"))) {
			return "New review for " + update.getValue("officialName") + " in " + update.getValue("city") + " by "
					+ update.getValue("user");
		} else {
			return update.getValue("user") + " updated the review for " + update.getValue("officialName") + " in "
					+ update.getValue("city");
		}
	}

	private String setTextForLocation(DozerAdapter update) {
		if ("N".equals(update.getValue("updateType"))) {
			return "New location " + update.getValue("officialName") + " in " + update.getValue("city");
		} else {
			return update.getValue("officialName") + " in " + update.getValue("city") + " was updated";
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QueryResponse {
		private List<UpdatesQuery> latestUpdates;
		private List<MailImage> latestPictures;
	}

}
