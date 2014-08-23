package de.oglimmer.lunchy.rest.resources;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.ReviewDao;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.rest.SecurityProvider;
import de.oglimmer.lunchy.rest.dto.Review;
import de.oglimmer.lunchy.rest.dto.ReviewUpdateResponse;
import de.oglimmer.lunchy.services.Community;

@Path("reviews")
public class ReviewResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Review get(@Context HttpServletRequest request, @PathParam("id") int id) {
		ReviewsRecord reviewRec = ReviewDao.INSTANCE.getById(id, Community.get(request));
		return BeanMappingProvider.INSTANCE.map(reviewRec, Review.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public ReviewUpdateResponse update(@Context HttpServletRequest request, @PathParam("id") int id, Review reviewDto) {
		ReviewsRecord reviewRec = ReviewDao.INSTANCE.getById(id, Community.get(request));
		BeanMappingProvider.INSTANCE.map(reviewDto, reviewRec);

		reviewRec.setLastUpdate(new Timestamp(new Date().getTime()));

		ReviewDao.INSTANCE.store(reviewRec);
		ReviewUpdateResponse backLocationDto = BeanMappingProvider.INSTANCE.map(reviewRec, ReviewUpdateResponse.class);
		return backLocationDto;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Context HttpServletRequest request, Review reviewDto) {
		try {
			ReviewsRecord reviewRec = createRecordInstance(reviewDto);

			if (reviewRec.getId() == null || reviewRec.getId() == 0) {
				reviewRec.setFkCommunity(Community.get(request));
				reviewRec.setFkUser((Integer) request.getSession(false).getAttribute("userId"));
				reviewRec.setCreatedOn(new Timestamp(new Date().getTime()));
			}
			reviewRec.setLastUpdate(new Timestamp(new Date().getTime()));

			ReviewDao.INSTANCE.store(reviewRec);
			ReviewUpdateResponse backLocationDto = BeanMappingProvider.INSTANCE.map(reviewRec, ReviewUpdateResponse.class);
			return Response.ok(backLocationDto).build();
		} catch (org.jooq.exception.DataAccessException e) {
			if (e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {
				return Response.status(Status.CONFLICT).build();
			} else {
				throw e;
			}
		}
	}

	@DELETE
	@Path("{id}")
	public void delete(@Context HttpServletRequest request, @PathParam("id") int id) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		ReviewDao.INSTANCE.delete(id, Community.get(request));
	}

	private ReviewsRecord createRecordInstance(Review reviewDto) {
		ReviewsRecord reviewRec = new ReviewsRecord();
		BeanMappingProvider.INSTANCE.map(reviewDto, reviewRec);
		return reviewRec;
	}
}
