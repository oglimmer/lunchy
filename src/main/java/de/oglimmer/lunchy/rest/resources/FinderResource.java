package de.oglimmer.lunchy.rest.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.oglimmer.lunchy.database.dao.FinderDao;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LocationResponse;
import de.oglimmer.lunchy.services.CommunityService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Path("finder")
public class FinderResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueryResponse> query(@Context HttpServletRequest request,
			@QueryParam(value = "inclTags") String inclTags, @QueryParam(value = "partner") String partner,
			@QueryParam(value = "maxTime") Integer maxTime, @QueryParam(value = "minRating") Integer minRating,
			@QueryParam(value = "selectedOffice") Integer selectedOffice) {

		Set<String> tags = new HashSet<>();
		if (inclTags != null && !inclTags.trim().isEmpty()) {
			tags.addAll(Arrays.asList(inclTags.split(",")));
		}
		Set<String> partners = new HashSet<>();
		if (partner != null && !partner.trim().isEmpty()) {
			partners.addAll(Arrays.asList(partner.split(",")));
		}

		if (maxTime == null) {
			maxTime = Integer.MAX_VALUE;
		}

		return FinderDao.INSTANCE.query(tags, partners, maxTime, minRating, CommunityService.get(request),
				SessionProvider.INSTANCE.getLoggedInUserId(request), selectedOffice);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("random")
	public List<QueryResponse> queryRandom(@Context HttpServletRequest request,
			@QueryParam(value = "inclTags") String inclTags, @QueryParam(value = "partner") String partner,
			@QueryParam(value = "maxTime") Integer maxTime, @QueryParam(value = "minRating") Integer minRating,
			@QueryParam(value = "selectedOffice") Integer selectedOffice) {

		Set<String> tags = new HashSet<>();
		if (inclTags != null && !inclTags.trim().isEmpty()) {
			tags.addAll(Arrays.asList(inclTags.split(",")));
		}
		Set<String> partners = new HashSet<>();
		if (partner != null && !partner.trim().isEmpty()) {
			partners.addAll(Arrays.asList(partner.split(",")));
		}

		if (maxTime == null) {
			maxTime = Integer.MAX_VALUE;
		}

		List<QueryResponse> possibleLoc = FinderDao.INSTANCE.query(tags, partners, maxTime, minRating,
				CommunityService.get(request), SessionProvider.INSTANCE.getLoggedInUserId(request), selectedOffice);

		List<QueryResponseSectionLine> flatPossibleLoc = possibleLoc.stream().flatMap(qr -> qr.sectionLines.stream())
				.collect(Collectors.toList());

		int rand = (int) (Math.random() * flatPossibleLoc.size());

		List<QueryResponse> finalResult = new ArrayList<>();
		List<QueryResponseSectionLine> finalResultSecLine = new ArrayList<>();
		QueryResponseSectionLine finalResultRespSecLine = flatPossibleLoc.get(rand);
		finalResultSecLine.add(finalResultRespSecLine);
		QueryResponse finalResultQueryResp = new QueryResponse(finalResultSecLine,
				getMinScoreForLoc(possibleLoc, finalResultRespSecLine.getLocation()));
		finalResult.add(finalResultQueryResp);
		return finalResult;
	}

	private int getMinScoreForLoc(List<QueryResponse> possibleLoc, LocationResponse location) {
		for (QueryResponse qr : possibleLoc) {
			for (QueryResponseSectionLine qrsl : qr.getSectionLines()) {
				if (qrsl.getLocation() == location) {
					return qr.getMinRating();
				}
			}
		}
		return -1;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QueryResponse {
		private List<QueryResponseSectionLine> sectionLines;
		private int minRating;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class QueryResponseSectionLine {
		private LocationResponse location;
		private List<RatingResponse> ratings;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RatingResponse {
		private String userName;
		private Integer score;
	}
}
