/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated;


import de.oglimmer.lunchy.database.generated.tables.Communities;
import de.oglimmer.lunchy.database.generated.tables.Databasechangeloglock;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.LocationUsersEmail;
import de.oglimmer.lunchy.database.generated.tables.Offices;
import de.oglimmer.lunchy.database.generated.tables.Pictures;
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.UsageStatistics;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.DatabasechangeloglockRecord;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;
import de.oglimmer.lunchy.database.generated.tables.records.LocationUsersEmailRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsageStatisticsRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>oli_lunchy</code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<CommunitiesRecord, Integer> IDENTITY_COMMUNITIES = Identities0.IDENTITY_COMMUNITIES;
	public static final Identity<LocationRecord, Integer> IDENTITY_LOCATION = Identities0.IDENTITY_LOCATION;
	public static final Identity<LocationUsersEmailRecord, Integer> IDENTITY_LOCATION_USERS_EMAIL = Identities0.IDENTITY_LOCATION_USERS_EMAIL;
	public static final Identity<OfficesRecord, Integer> IDENTITY_OFFICES = Identities0.IDENTITY_OFFICES;
	public static final Identity<PicturesRecord, Integer> IDENTITY_PICTURES = Identities0.IDENTITY_PICTURES;
	public static final Identity<ReviewsRecord, Integer> IDENTITY_REVIEWS = Identities0.IDENTITY_REVIEWS;
	public static final Identity<UsageStatisticsRecord, Integer> IDENTITY_USAGE_STATISTICS = Identities0.IDENTITY_USAGE_STATISTICS;
	public static final Identity<UsersRecord, Integer> IDENTITY_USERS = Identities0.IDENTITY_USERS;
	public static final Identity<UsersPicturesVotesRecord, Integer> IDENTITY_USERS_PICTURES_VOTES = Identities0.IDENTITY_USERS_PICTURES_VOTES;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<CommunitiesRecord> KEY_COMMUNITIES_PRIMARY = UniqueKeys0.KEY_COMMUNITIES_PRIMARY;
	public static final UniqueKey<CommunitiesRecord> KEY_COMMUNITIES_UNIQ_DOMAIN = UniqueKeys0.KEY_COMMUNITIES_UNIQ_DOMAIN;
	public static final UniqueKey<DatabasechangeloglockRecord> KEY_DATABASECHANGELOGLOCK_PRIMARY = UniqueKeys0.KEY_DATABASECHANGELOGLOCK_PRIMARY;
	public static final UniqueKey<LocationRecord> KEY_LOCATION_PRIMARY = UniqueKeys0.KEY_LOCATION_PRIMARY;
	public static final UniqueKey<LocationUsersEmailRecord> KEY_LOCATION_USERS_EMAIL_PRIMARY = UniqueKeys0.KEY_LOCATION_USERS_EMAIL_PRIMARY;
	public static final UniqueKey<OfficesRecord> KEY_OFFICES_PRIMARY = UniqueKeys0.KEY_OFFICES_PRIMARY;
	public static final UniqueKey<PicturesRecord> KEY_PICTURES_PRIMARY = UniqueKeys0.KEY_PICTURES_PRIMARY;
	public static final UniqueKey<ReviewsRecord> KEY_REVIEWS_PRIMARY = UniqueKeys0.KEY_REVIEWS_PRIMARY;
	public static final UniqueKey<ReviewsRecord> KEY_REVIEWS_FKUSER = UniqueKeys0.KEY_REVIEWS_FKUSER;
	public static final UniqueKey<UsageStatisticsRecord> KEY_USAGE_STATISTICS_PRIMARY = UniqueKeys0.KEY_USAGE_STATISTICS_PRIMARY;
	public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = UniqueKeys0.KEY_USERS_PRIMARY;
	public static final UniqueKey<UsersPicturesVotesRecord> KEY_USERS_PICTURES_VOTES_PRIMARY = UniqueKeys0.KEY_USERS_PICTURES_VOTES_PRIMARY;
	public static final UniqueKey<UsersPicturesVotesRecord> KEY_USERS_PICTURES_VOTES_UNIQUE_USER_PICTURE_VOTE = UniqueKeys0.KEY_USERS_PICTURES_VOTES_UNIQUE_USER_PICTURE_VOTE;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<LocationRecord, UsersRecord> FK_LOC_USR = ForeignKeys0.FK_LOC_USR;
	public static final ForeignKey<LocationRecord, OfficesRecord> FK_LOC_OFF = ForeignKeys0.FK_LOC_OFF;
	public static final ForeignKey<LocationRecord, CommunitiesRecord> FK_LOC_COM = ForeignKeys0.FK_LOC_COM;
	public static final ForeignKey<LocationUsersEmailRecord, LocationRecord> FK_LOCATION_USERS_EMAIL_LOCATION = ForeignKeys0.FK_LOCATION_USERS_EMAIL_LOCATION;
	public static final ForeignKey<LocationUsersEmailRecord, UsersRecord> FK_LOCATION_USERS_EMAIL_USER = ForeignKeys0.FK_LOCATION_USERS_EMAIL_USER;
	public static final ForeignKey<OfficesRecord, CommunitiesRecord> FK_OFF_COM = ForeignKeys0.FK_OFF_COM;
	public static final ForeignKey<PicturesRecord, LocationRecord> FK_PIC_LOC = ForeignKeys0.FK_PIC_LOC;
	public static final ForeignKey<PicturesRecord, UsersRecord> FK_PIC_USR = ForeignKeys0.FK_PIC_USR;
	public static final ForeignKey<PicturesRecord, CommunitiesRecord> FK_PIC_COM = ForeignKeys0.FK_PIC_COM;
	public static final ForeignKey<ReviewsRecord, UsersRecord> FK_REV_USR = ForeignKeys0.FK_REV_USR;
	public static final ForeignKey<ReviewsRecord, LocationRecord> FK_REV_LOC = ForeignKeys0.FK_REV_LOC;
	public static final ForeignKey<ReviewsRecord, CommunitiesRecord> FK_REV_COM = ForeignKeys0.FK_REV_COM;
	public static final ForeignKey<UsersRecord, OfficesRecord> FK_USR_OFF = ForeignKeys0.FK_USR_OFF;
	public static final ForeignKey<UsersRecord, CommunitiesRecord> FK_USR_COM = ForeignKeys0.FK_USR_COM;
	public static final ForeignKey<UsersPicturesVotesRecord, CommunitiesRecord> FK_USERS_PICTURES_VOTES_COMMUNITY = ForeignKeys0.FK_USERS_PICTURES_VOTES_COMMUNITY;
	public static final ForeignKey<UsersPicturesVotesRecord, UsersRecord> FK_USERS_PICTURES_VOTES_USERS = ForeignKeys0.FK_USERS_PICTURES_VOTES_USERS;
	public static final ForeignKey<UsersPicturesVotesRecord, PicturesRecord> FK_USERS_PICTURES_VOTES_PICTURES = ForeignKeys0.FK_USERS_PICTURES_VOTES_PICTURES;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<CommunitiesRecord, Integer> IDENTITY_COMMUNITIES = createIdentity(Communities.COMMUNITIES, Communities.COMMUNITIES.ID);
		public static Identity<LocationRecord, Integer> IDENTITY_LOCATION = createIdentity(Location.LOCATION, Location.LOCATION.ID);
		public static Identity<LocationUsersEmailRecord, Integer> IDENTITY_LOCATION_USERS_EMAIL = createIdentity(LocationUsersEmail.LOCATION_USERS_EMAIL, LocationUsersEmail.LOCATION_USERS_EMAIL.ID);
		public static Identity<OfficesRecord, Integer> IDENTITY_OFFICES = createIdentity(Offices.OFFICES, Offices.OFFICES.ID);
		public static Identity<PicturesRecord, Integer> IDENTITY_PICTURES = createIdentity(Pictures.PICTURES, Pictures.PICTURES.ID);
		public static Identity<ReviewsRecord, Integer> IDENTITY_REVIEWS = createIdentity(Reviews.REVIEWS, Reviews.REVIEWS.ID);
		public static Identity<UsageStatisticsRecord, Integer> IDENTITY_USAGE_STATISTICS = createIdentity(UsageStatistics.USAGE_STATISTICS, UsageStatistics.USAGE_STATISTICS.ID);
		public static Identity<UsersRecord, Integer> IDENTITY_USERS = createIdentity(Users.USERS, Users.USERS.ID);
		public static Identity<UsersPicturesVotesRecord, Integer> IDENTITY_USERS_PICTURES_VOTES = createIdentity(UsersPicturesVotes.USERS_PICTURES_VOTES, UsersPicturesVotes.USERS_PICTURES_VOTES.ID);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<CommunitiesRecord> KEY_COMMUNITIES_PRIMARY = createUniqueKey(Communities.COMMUNITIES, Communities.COMMUNITIES.ID);
		public static final UniqueKey<CommunitiesRecord> KEY_COMMUNITIES_UNIQ_DOMAIN = createUniqueKey(Communities.COMMUNITIES, Communities.COMMUNITIES.DOMAIN);
		public static final UniqueKey<DatabasechangeloglockRecord> KEY_DATABASECHANGELOGLOCK_PRIMARY = createUniqueKey(Databasechangeloglock.DATABASECHANGELOGLOCK, Databasechangeloglock.DATABASECHANGELOGLOCK.ID);
		public static final UniqueKey<LocationRecord> KEY_LOCATION_PRIMARY = createUniqueKey(Location.LOCATION, Location.LOCATION.ID);
		public static final UniqueKey<LocationUsersEmailRecord> KEY_LOCATION_USERS_EMAIL_PRIMARY = createUniqueKey(LocationUsersEmail.LOCATION_USERS_EMAIL, LocationUsersEmail.LOCATION_USERS_EMAIL.ID);
		public static final UniqueKey<OfficesRecord> KEY_OFFICES_PRIMARY = createUniqueKey(Offices.OFFICES, Offices.OFFICES.ID);
		public static final UniqueKey<PicturesRecord> KEY_PICTURES_PRIMARY = createUniqueKey(Pictures.PICTURES, Pictures.PICTURES.ID);
		public static final UniqueKey<ReviewsRecord> KEY_REVIEWS_PRIMARY = createUniqueKey(Reviews.REVIEWS, Reviews.REVIEWS.ID);
		public static final UniqueKey<ReviewsRecord> KEY_REVIEWS_FKUSER = createUniqueKey(Reviews.REVIEWS, Reviews.REVIEWS.FK_USER, Reviews.REVIEWS.FK_LOCATION);
		public static final UniqueKey<UsageStatisticsRecord> KEY_USAGE_STATISTICS_PRIMARY = createUniqueKey(UsageStatistics.USAGE_STATISTICS, UsageStatistics.USAGE_STATISTICS.ID);
		public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = createUniqueKey(Users.USERS, Users.USERS.ID);
		public static final UniqueKey<UsersPicturesVotesRecord> KEY_USERS_PICTURES_VOTES_PRIMARY = createUniqueKey(UsersPicturesVotes.USERS_PICTURES_VOTES, UsersPicturesVotes.USERS_PICTURES_VOTES.ID);
		public static final UniqueKey<UsersPicturesVotesRecord> KEY_USERS_PICTURES_VOTES_UNIQUE_USER_PICTURE_VOTE = createUniqueKey(UsersPicturesVotes.USERS_PICTURES_VOTES, UsersPicturesVotes.USERS_PICTURES_VOTES.FK_USER, UsersPicturesVotes.USERS_PICTURES_VOTES.FK_PICTURE);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<LocationRecord, UsersRecord> FK_LOC_USR = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY, Location.LOCATION, Location.LOCATION.FK_USER);
		public static final ForeignKey<LocationRecord, OfficesRecord> FK_LOC_OFF = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_OFFICES_PRIMARY, Location.LOCATION, Location.LOCATION.FK_OFFICE);
		public static final ForeignKey<LocationRecord, CommunitiesRecord> FK_LOC_COM = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_COMMUNITIES_PRIMARY, Location.LOCATION, Location.LOCATION.FK_COMMUNITY);
		public static final ForeignKey<LocationUsersEmailRecord, LocationRecord> FK_LOCATION_USERS_EMAIL_LOCATION = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_LOCATION_PRIMARY, LocationUsersEmail.LOCATION_USERS_EMAIL, LocationUsersEmail.LOCATION_USERS_EMAIL.FK_LOCATION);
		public static final ForeignKey<LocationUsersEmailRecord, UsersRecord> FK_LOCATION_USERS_EMAIL_USER = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY, LocationUsersEmail.LOCATION_USERS_EMAIL, LocationUsersEmail.LOCATION_USERS_EMAIL.FK_USER);
		public static final ForeignKey<OfficesRecord, CommunitiesRecord> FK_OFF_COM = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_COMMUNITIES_PRIMARY, Offices.OFFICES, Offices.OFFICES.FK_COMMUNITY);
		public static final ForeignKey<PicturesRecord, LocationRecord> FK_PIC_LOC = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_LOCATION_PRIMARY, Pictures.PICTURES, Pictures.PICTURES.FK_LOCATION);
		public static final ForeignKey<PicturesRecord, UsersRecord> FK_PIC_USR = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY, Pictures.PICTURES, Pictures.PICTURES.FK_USER);
		public static final ForeignKey<PicturesRecord, CommunitiesRecord> FK_PIC_COM = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_COMMUNITIES_PRIMARY, Pictures.PICTURES, Pictures.PICTURES.FK_COMMUNITY);
		public static final ForeignKey<ReviewsRecord, UsersRecord> FK_REV_USR = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY, Reviews.REVIEWS, Reviews.REVIEWS.FK_USER);
		public static final ForeignKey<ReviewsRecord, LocationRecord> FK_REV_LOC = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_LOCATION_PRIMARY, Reviews.REVIEWS, Reviews.REVIEWS.FK_LOCATION);
		public static final ForeignKey<ReviewsRecord, CommunitiesRecord> FK_REV_COM = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_COMMUNITIES_PRIMARY, Reviews.REVIEWS, Reviews.REVIEWS.FK_COMMUNITY);
		public static final ForeignKey<UsersRecord, OfficesRecord> FK_USR_OFF = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_OFFICES_PRIMARY, Users.USERS, Users.USERS.FK_BASE_OFFICE);
		public static final ForeignKey<UsersRecord, CommunitiesRecord> FK_USR_COM = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_COMMUNITIES_PRIMARY, Users.USERS, Users.USERS.FK_COMMUNITY);
		public static final ForeignKey<UsersPicturesVotesRecord, CommunitiesRecord> FK_USERS_PICTURES_VOTES_COMMUNITY = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_COMMUNITIES_PRIMARY, UsersPicturesVotes.USERS_PICTURES_VOTES, UsersPicturesVotes.USERS_PICTURES_VOTES.FK_COMMUNITY);
		public static final ForeignKey<UsersPicturesVotesRecord, UsersRecord> FK_USERS_PICTURES_VOTES_USERS = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY, UsersPicturesVotes.USERS_PICTURES_VOTES, UsersPicturesVotes.USERS_PICTURES_VOTES.FK_USER);
		public static final ForeignKey<UsersPicturesVotesRecord, PicturesRecord> FK_USERS_PICTURES_VOTES_PICTURES = createForeignKey(de.oglimmer.lunchy.database.generated.Keys.KEY_PICTURES_PRIMARY, UsersPicturesVotes.USERS_PICTURES_VOTES, UsersPicturesVotes.USERS_PICTURES_VOTES.FK_PICTURE);
	}
}
