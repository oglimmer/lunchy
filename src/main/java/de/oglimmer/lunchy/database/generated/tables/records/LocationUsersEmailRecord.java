/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables.records;


import de.oglimmer.lunchy.database.generated.tables.LocationUsersEmail;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class LocationUsersEmailRecord extends UpdatableRecordImpl<LocationUsersEmailRecord> implements Record4<Integer, Integer, Integer, String> {

	private static final long serialVersionUID = 1438769979;

	/**
	 * Setter for <code>oli_lunchy.location_users_email.id</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location_users_email.id</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.location_users_email.fk_location</code>.
	 */
	public void setFkLocation(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location_users_email.fk_location</code>.
	 */
	public Integer getFkLocation() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.location_users_email.fk_user</code>.
	 */
	public void setFkUser(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location_users_email.fk_user</code>.
	 */
	public Integer getFkUser() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.location_users_email.local_name</code>.
	 */
	public void setLocalName(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location_users_email.local_name</code>.
	 */
	public String getLocalName() {
		return (String) getValue(3);
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
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, Integer, Integer, String> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, Integer, Integer, String> valuesRow() {
		return (Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return LocationUsersEmail.LOCATION_USERS_EMAIL.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return LocationUsersEmail.LOCATION_USERS_EMAIL.FK_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return LocationUsersEmail.LOCATION_USERS_EMAIL.FK_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return LocationUsersEmail.LOCATION_USERS_EMAIL.LOCAL_NAME;
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
		return getFkLocation();
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
	public String value4() {
		return getLocalName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationUsersEmailRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationUsersEmailRecord value2(Integer value) {
		setFkLocation(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationUsersEmailRecord value3(Integer value) {
		setFkUser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationUsersEmailRecord value4(String value) {
		setLocalName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationUsersEmailRecord values(Integer value1, Integer value2, Integer value3, String value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached LocationUsersEmailRecord
	 */
	public LocationUsersEmailRecord() {
		super(LocationUsersEmail.LOCATION_USERS_EMAIL);
	}

	/**
	 * Create a detached, initialised LocationUsersEmailRecord
	 */
	public LocationUsersEmailRecord(Integer id, Integer fkLocation, Integer fkUser, String localName) {
		super(LocationUsersEmail.LOCATION_USERS_EMAIL);

		setValue(0, id);
		setValue(1, fkLocation);
		setValue(2, fkUser);
		setValue(3, localName);
	}
}