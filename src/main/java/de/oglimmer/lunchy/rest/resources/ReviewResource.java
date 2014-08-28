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
import de.oglimmer.lunchy.database.dao.ReviewDao;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.SecurityProvider;
import de.oglimmer.lunchy.rest.dto.ReviewCreateInput;
import de.oglimmer.lunchy.rest.dto.ReviewResponse;
import de.oglimmer.lunchy.rest.dto.ReviewUpdateInput;
import de.oglimmer.lunchy.rest.dto.ReviewUpdateResponse;
import de.oglimmer.lunchy.services.Community;

@Path("reviews")
public class ReviewResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response get(@Context HttpServletRequest request, @PathParam("id") int id) {
		return get(request, id, ReviewResponse.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response update(@Context HttpServletRequest request, @PathParam("id") int id, ReviewUpdateInput reviewDto) {
		ReviewsRecord reviewRec = ReviewDao.INSTANCE.getById(id, Community.get(request));
		copyDtoToRec(reviewDto, reviewRec);
		if (reviewRec.getFkUser() != SessionProvider.INSTANCE.getLoggedInUserId(request)) {
			throw new RuntimeException("Wrong user");
		}
		return store(reviewRec);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Context HttpServletRequest request, ReviewCreateInput reviewDto) {
		ReviewsRecord reviewRec = new ReviewsRecord();
		copyDtoToRec(reviewDto, reviewRec);

		reviewRec.setFkCommunity(Community.get(request));
		reviewRec.setFkUser(SessionProvider.INSTANCE.getLoggedInUserId(request));
		reviewRec.setCreatedOn(new Timestamp(new Date().getTime()));

		return store(reviewRec);
	}

	@DELETE
	@Path("{id}")
	public void delete(@Context HttpServletRequest request, @PathParam("id") int id) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		ReviewDao.INSTANCE.delete(id, Community.get(request));
	}

	private void copyDtoToRec(ReviewUpdateInput reviewDto, ReviewsRecord reviewRec) {
		BeanMappingProvider.INSTANCE.map(reviewDto, reviewRec);
	}

	private Response store(ReviewsRecord reviewRec) {
		try {
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
}
