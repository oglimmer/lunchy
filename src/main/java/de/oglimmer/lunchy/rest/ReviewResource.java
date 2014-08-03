package de.oglimmer.lunchy.rest;

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

import de.oglimmer.lunchy.database.ReviewDao;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.rest.dto.Review;

@Path("reviews")
public class ReviewResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Review get(@PathParam("id") int id) {
		ReviewsRecord reviewRec = ReviewDao.INSTANCE.getById(id);
		return Review.getInstance(reviewRec);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Review post(@Context HttpServletRequest request, Review reviewDto) {

		if (reviewDto.getId() == null || reviewDto.getId() == 0) {
			reviewDto.setFkuser((Integer) request.getSession(false).getAttribute("userId"));
			reviewDto.setCreatedon(new Timestamp(new Date().getTime()));
		}
		reviewDto.setLastupdate(new Timestamp(new Date().getTime()));

		ReviewsRecord reviewRec = createRecordInstance(reviewDto);
		ReviewDao.INSTANCE.store(reviewRec);
		Review backLocationDto = Review.getInstance(reviewRec);
		return backLocationDto;
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") int id) {
		ReviewDao.INSTANCE.delete(id);
	}

	private ReviewsRecord createRecordInstance(Review reviewDto) {
		ReviewsRecord reviewRec = new ReviewsRecord();
		BeanMappingProvider.INSTANCE.getMapper().map(reviewDto, reviewRec);
		return reviewRec;
	}
}
