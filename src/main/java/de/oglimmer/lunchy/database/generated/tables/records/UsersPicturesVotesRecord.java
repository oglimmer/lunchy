/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables.records;


import de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersPicturesVotesRecord extends UpdatableRecordImpl<UsersPicturesVotesRecord> implements Record5<Integer, Integer, Integer, Integer, Timestamp> {

	private static final long serialVersionUID = 1311493534;

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.id</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.id</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.fk_community</code>.
	 */
	public void setFkCommunity(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.fk_community</code>.
	 */
	public Integer getFkCommunity() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.fk_user</code>.
	 */
	public void setFkUser(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.fk_user</code>.
	 */
	public Integer getFkUser() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.fk_picture</code>.
	 */
	public void setFkPicture(Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.fk_picture</code>.
	 */
	public Integer getFkPicture() {
		return (Integer) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.users_pictures_votes.created_on</code>.
	 */
	public void setCreatedOn(Timestamp value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users_pictures_votes.created_on</code>.
	 */
	public Timestamp getCreatedOn() {
		return (Timestamp) getValue(4);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, Integer, Integer, Integer, Timestamp> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, Integer, Integer, Integer, Timestamp> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return UsersPicturesVotes.USERS_PICTURES_VOTES.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return UsersPicturesVotes.USERS_PICTURES_VOTES.FK_COMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return UsersPicturesVotes.USERS_PICTURES_VOTES.FK_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field4() {
		return UsersPicturesVotes.USERS_PICTURES_VOTES.FK_PICTURE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field5() {
		return UsersPicturesVotes.USERS_PICTURES_VOTES.CREATED_ON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getFkCommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getFkUser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value4() {
		return getFkPicture();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value5() {
		return getCreatedOn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value2(Integer value) {
		setFkCommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value3(Integer value) {
		setFkUser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value4(Integer value) {
		setFkPicture(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord value5(Timestamp value) {
		setCreatedOn(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersPicturesVotesRecord values(Integer value1, Integer value2, Integer value3, Integer value4, Timestamp value5) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UsersPicturesVotesRecord
	 */
	public UsersPicturesVotesRecord() {
		super(UsersPicturesVotes.USERS_PICTURES_VOTES);
	}

	/**
	 * Create a detached, initialised UsersPicturesVotesRecord
	 */
	public UsersPicturesVotesRecord(Integer id, Integer fkCommunity, Integer fkUser, Integer fkPicture, Timestamp createdOn) {
		super(UsersPicturesVotes.USERS_PICTURES_VOTES);

		setValue(0, id);
		setValue(1, fkCommunity);
		setValue(2, fkUser);
		setValue(3, fkPicture);
		setValue(4, createdOn);
	}
}
