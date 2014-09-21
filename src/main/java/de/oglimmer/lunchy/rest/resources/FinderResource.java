package de.oglimmer.lunchy.rest.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.oglimmer.lunchy.database.dao.FinderDao;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LocationResponse;
import de.oglimmer.lunchy.services.Community;

@Path("finder")
public class FinderResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<QueryResponse> query(@Context HttpServletRequest request, @QueryParam(value = "inclTags") String inclTags,
			@QueryParam(value = "partner") String partner, @QueryParam(value = "maxTime") Integer maxTime) {
		System.out.println(inclTags);
		System.out.println(partner);
		System.out.println(maxTime);

		Set<String> tags = new HashSet<>();
		if (inclTags != null) {
			tags.addAll(Arrays.asList(inclTags.split(",")));
		}
		Set<String> partners = new HashSet<>();
		if (partner != null) {
			partners.addAll(Arrays.asList(partner.split(",")));
		}

		if (maxTime == null) {
			maxTime = Integer.MAX_VALUE;
		}

		return FinderDao.INSTANCE.query(tags, partners, maxTime, Community.get(request),
				SessionProvider.INSTANCE.getLoggedInUserId(request));
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
