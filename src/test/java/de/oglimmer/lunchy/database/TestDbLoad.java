package de.oglimmer.lunchy.database;

import org.jooq.Record;
import org.junit.Test;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.LocationDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.database.dao.PictureDao;
import de.oglimmer.lunchy.database.dao.ReviewDao;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.rest.dto.LocationCreateInput;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.rest.dto.Office;
import de.oglimmer.lunchy.rest.dto.PictureUpdateInput;
import de.oglimmer.lunchy.rest.dto.ReviewUpdateInput;
import de.oglimmer.lunchy.rest.dto.ReviewUpdateResponse;
import de.oglimmer.lunchy.rest.dto.UserAdminResponse;

public abstract class TestDbLoad {

	@Test
	public void testReviewsList() {
		for (Object reviewRec : ReviewDao.INSTANCE.getListByParent(2)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, ReviewUpdateInput.class));
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
		System.out.println(BeanMappingProvider.INSTANCE.map(locationRec, LocationCreateInput.class));
	}

	@Test
	public void testUser() {
		for (Object reviewRec : UserDao.INSTANCE.getListByParent(1)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, UserAdminResponse.class));
		}
	}

	@Test
	public void testOffice() {
		for (Object officeRec : OfficeDao.INSTANCE.getListByParent(1)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(officeRec, Office.class));
		}
	}

	@Test
	public void testPicture() {
		for (Object reviewRec : PictureDao.INSTANCE.getListByParent(46)) {
			System.out.println(BeanMappingProvider.INSTANCE.map(reviewRec, PictureUpdateInput.class));
		}
	}

}
