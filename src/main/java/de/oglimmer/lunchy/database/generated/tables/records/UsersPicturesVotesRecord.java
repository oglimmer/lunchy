/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersPicturesVotesRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord> implements org.jooq.Record5<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.sql.Timestamp> {

	private static final long serialVersionUID = 260170435;

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.fk_community</code>.
	 */
	public void setFkCommunity(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.fk_community</code>.
	 */
	public java.lang.Integer getFkCommunity() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.fk_user</code>.
	 */
	public void setFkUser(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.fk_user</code>.
	 */
	public java.lang.Integer getFkUser() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.fk_picture</code>.
	 */
	public void setFkPicture(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.fk_picture</code>.
	 */
	public java.lang.Integer getFkPicture() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.created_on</code>.
	 */
	public void setCreatedOn(java.sql.Timestamp value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.created_on</code>.
	 */
	public java.sql.Timestamp getCreatedOn() {
		return (java.sql.Timestamp) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.sql.Timestamp> fieldsRow() {
		return (org.jooq.Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row5<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.sql.Timestamp> valuesRow() {
		return (org.jooq.Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES.FK_COMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES.FK_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES.FK_PICTURE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field5() {
		return de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES.CREATED_ON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value2() {
		return getFkCommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getFkUser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getFkPicture();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value5() {
		return getCreatedOn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value2(java.lang.Integer value) {
		setFkCommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value3(java.lang.Integer value) {
		setFkUser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value4(java.lang.Integer value) {
		setFkPicture(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value5(java.sql.Timestamp value) {
		setCreatedOn(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.Integer value4, java.sql.Timestamp value5) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UsersPicturesVotesRecord
	 */
	public UsersPicturesVotesRecord() {
		super(de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES);
	}

	/**
	 * Create a detached, initialised UsersPicturesVotesRecord
	 */
	public UsersPicturesVotesRecord(java.lang.Integer id, java.lang.Integer fkCommunity, java.lang.Integer fkUser, java.lang.Integer fkPicture, java.sql.Timestamp createdOn) {
		super(de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes.USERS_PICTURES_VOTES);

		setValue(0, id);
		setValue(1, fkCommunity);
		setValue(2, fkUser);
		setValue(3, fkPicture);
		setValue(4, createdOn);
	}
}
