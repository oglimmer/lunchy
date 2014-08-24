package de.oglimmer.lunchy.database;

import org.jooq.Record;
import org.junit.Test;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.Location;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.rest.dto.Office;
import de.oglimmer.lunchy.rest.dto.Picture;
import de.oglimmer.lunchy.rest.dto.Review;
import de.oglimmer.lunchy.rest.dto.ReviewUpdateResponse;
import de.oglimmer.lunchy.rest.dto.User;

public abstract class TestDbLoad {

	@Test
	public void testReviewsList() {
		for (ReviewsRecord reviewRec : ReviewDao.INSTANCE.getList(2)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, Review.class));
		}
	}

	@Test
	public void testReviews() {
		ReviewsRecord reviewRec = ReviewDao.INSTANCE.getById(1, 1);
		System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, ReviewUpdateResponse.class));
	}

	@Test
	public void testLocationListUser() {
		for (Record rec : LocationDao.INSTANCE.getList(1, 1)) {
			System.out.println(LocationQuery.getInstance(rec, 1));
		}
	}

	@Test
	public void testLocationListNoUser() {
		for (Record rec : LocationDao.INSTANCE.getList(null, 1)) {
			System.out.println(LocationQuery.getInstance(rec, 1));
		}
	}

	@Test
	public void testLocation() {
		LocationRecord locationRec = LocationDao.INSTANCE.getById(1, 1);
		System.out.println(BeanMappingProvider.INSTANCE.map(locationRec, Location.class));
	}

	@Test
	public void testUser() {
		for (UsersRecord reviewRec : UserDao.INSTANCE.query(1)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, User.class));
		}
	}

	@Test
	public void testOffice() {
		for (OfficesRecord officeRec : OfficeDao.INSTANCE.query(1)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(officeRec, Office.class));
		}
	}

	@Test
	public void testPicture() {
		for (PicturesRecord reviewRec : PicturesDao.INSTANCE.getList(46)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, Picture.class));
		}
	}

}
