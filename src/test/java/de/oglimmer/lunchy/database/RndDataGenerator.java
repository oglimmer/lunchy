package de.oglimmer.lunchy.database;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.jooq.Record;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.dao.LocationDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.database.dao.PictureDao;
import de.oglimmer.lunchy.database.dao.ReviewDao;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.dao.UserPictureVoteDao;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.LocationQuery;
import de.oglimmer.lunchy.services.DateCalcService;

public class RndDataGenerator {

	private static final int NUM_PICTURE = 3;
	private static final int EXTRA_REVIEWS = 500;// 15000
	private static final int NUM_LOCATION = 5;
	private static final int NUM_USERS = 2;// 20
	private static final int NUM_OFFICES = 5;// 50
	public static final int NUM_COMMUNITIES = 1;
	private int baseId;

	public RndDataGenerator(int baseId) {
		this.baseId = baseId;
	}

	public void genCommunities() {
		for (int i = baseId + 1; i <= baseId + NUM_COMMUNITIES; i++) {
			CommunitiesRecord rec = new CommunitiesRecord();
			rec.setId(i);
			rec.setDomain(RandomStringUtils.randomAlphabetic(20));
			rec.setName(RandomStringUtils.randomAlphabetic(20));
			rec.setAdminEmail(RandomStringUtils.randomAlphabetic(20));
			CommunityDao.INSTANCE.store(rec);
			genOffices(rec.getId());
		}
	}

	public void genOffices(int fkCommunity) {
		for (int i = 0; i < NUM_OFFICES; i++) {
			OfficesRecord rec = new OfficesRecord();
			rec.setFkCommunity(fkCommunity);
			rec.setCountry(RandomStringUtils.randomAlphabetic(20));
			rec.setGeoLat(Math.random() * 100);
			rec.setGeoLng(Math.random() * 100);
			rec.setName(RandomStringUtils.randomAlphabetic(20));
			rec.setZoomfactor((int) (Math.random() * 20));
			OfficeDao.INSTANCE.store(rec);
			genUsers(fkCommunity, rec.getId());
			genReview(fkCommunity, rec.getId());
		}
	}

	public void genUsers(int fkCommunity, int fkBaseOffice) {
		for (int i = 0; i < NUM_USERS; i++) {
			UsersRecord user = new UsersRecord();
			user.setFkBaseOffice(fkBaseOffice);
			user.setFkCommunity(fkCommunity);
			user.setCreatedOn(new Timestamp(new Date().getTime()));
			user.setDisplayname(RandomStringUtils.randomAlphabetic(20));
			user.setEmail(RandomStringUtils.randomAlphabetic(20));
			user.setLastLogin(new Timestamp(new Date().getTime()));
			user.setEmailUpdates(0);
			user.setLastEmailUpdate(new Timestamp(new Date().getTime()));
			user.setNextEmailUpdate(DateCalcService.getNever());
			user.setPassword("not-set");
			user.setPermissions(1);
			UserDao.INSTANCE.store(user);
			genLocation(fkCommunity, fkBaseOffice, user.getId());
		}
	}

	public void genLocation(int fkCommunity, int fkBaseOffice, int fkCreator) {
		for (int i = 0; i < NUM_LOCATION; i++) {
			LocationRecord loc = new LocationRecord();
			loc.setAddress(RandomStringUtils.randomAlphabetic(20));
			loc.setCity(RandomStringUtils.randomAlphabetic(20));
			loc.setComment(RandomStringUtils.randomAlphabetic(20));
			loc.setCountry(RandomStringUtils.randomAlphabetic(20));
			loc.setCreatedOn(new Timestamp(new Date().getTime()));
			loc.setFkCommunity(fkCommunity);
			loc.setFkOffice(fkBaseOffice);
			loc.setFkUser(fkCreator);
			loc.setGeoLat(Math.random() * 100);
			loc.setGeoLng(Math.random() * 100);
			loc.setLastUpdate(new Timestamp(new Date().getTime()));
			loc.setOfficialName(RandomStringUtils.randomAlphabetic(20));
			loc.setStreetName(RandomStringUtils.randomAlphabetic(20));
			loc.setTags(genTags(10));
			loc.setUrl(RandomStringUtils.randomAlphabetic(20));
			loc.setZip(RandomStringUtils.randomAlphabetic(6));
			loc.setGeoMovedManually((byte) 0);
			LocationDao.INSTANCE.store(loc);
			genReview(fkCommunity, fkCreator, loc.getId());
			genPictures(fkCommunity, fkCreator, loc.getId());
		}
	}

	@SuppressWarnings("unchecked")
	public void genReview(int fkCommunity, int fkOffice) {
		List<Record> list = LocationDao.INSTANCE.getList(0, fkOffice);
		List<UsersRecord> users = (List<UsersRecord>) UserDao.INSTANCE.getListByParent(fkCommunity);
		for (int i = 0; i < EXTRA_REVIEWS; i++) {
			LocationQuery lq = LocationQuery.getInstance(list.get((int) (list.size() * Math.random())), fkCommunity);
			try {
				genReview(fkCommunity, users.get((int) (users.size() * Math.random())).getId(), lq.getId());
			} catch (Exception e) {
			}
		}
	}

	public void genReview(int fkCommunity, int fkCreator, int fkLocation) {
		ReviewsRecord rec = new ReviewsRecord();
		rec.setComment(RandomStringUtils.randomAlphabetic(20));
		rec.setCreatedOn(new Timestamp(new Date().getTime()));
		rec.setFavoriteMeal(RandomStringUtils.randomAlphabetic(20));
		rec.setFkCommunity(fkCommunity);
		rec.setFkLocation(fkLocation);
		rec.setFkUser(fkCreator);
		rec.setLastUpdate(new Timestamp(new Date().getTime()));
		rec.setOnSiteTime((int) (Math.random() * 100));
		rec.setRating((int) (Math.random() * 5) + 1);
		rec.setTravelTime((int) (Math.random() * 100));
		ReviewDao.INSTANCE.store(rec);
	}

	public void genPictures(int fkCommunity, int fkCreator, int fkLocation) {
		for (int i = 0; i < NUM_PICTURE; i++) {
			PicturesRecord pic = new PicturesRecord();
			pic.setCaption(RandomStringUtils.randomAlphabetic(20));
			pic.setCreatedOn(new Timestamp(new Date().getTime()));
			pic.setFilename("default.jpg");
			pic.setFkCommunity(fkCommunity);
			pic.setFkLocation(fkLocation);
			pic.setFkUser(fkCreator);
			pic.setUpVotes(0);
			PictureDao.INSTANCE.store(pic);
			getPicVote(fkCommunity, fkCreator, pic.getId());
		}
	}

	public void getPicVote(int fkCommunity, int fkCreator, int fkPicture) {
		UsersPicturesVotesRecord rec = new UsersPicturesVotesRecord();
		rec.setCreatedOn(DateCalcService.getNow());
		rec.setFkCommunity(fkCommunity);
		rec.setFkPicture(fkPicture);
		rec.setFkUser(fkCreator);
		UserPictureVoteDao.INSTANCE.store(rec);
	}

	private static final String[] TAGS = { "Foo", "Bar", "Chicken", "All the good stuff", "Even More", "Other stuff", "Whatever",
			"Bad stuff", "Worse stuff", "whatever" };

	private String genTags(int number) {
		StringBuilder buff = new StringBuilder();
		for (int i = 0; i < number; i++) {
			if (i > 0) {
				buff.append(",");
			}
			buff.append(TAGS[(int) (Math.random() * TAGS.length)]);
		}
		return buff.toString();
	}

}
