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
public class PicturesRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord> implements org.jooq.Record8<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = 957987298;

	/**
	 * Setter for <code>oli_lunchy.pictures.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.fk_Location</code>.
	 */
	public void setFkLocation(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.fk_Location</code>.
	 */
	public java.lang.Integer getFkLocation() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.fk_User</code>.
	 */
	public void setFkUser(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.fk_User</code>.
	 */
	public java.lang.Integer getFkUser() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.filename</code>.
	 */
	public void setFilename(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.filename</code>.
	 */
	public java.lang.String getFilename() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.caption</code>.
	 */
	public void setCaption(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.caption</code>.
	 */
	public java.lang.String getCaption() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.created_On</code>.
	 */
	public void setCreatedOn(java.sql.Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.created_On</code>.
	 */
	public java.sql.Timestamp getCreatedOn() {
		return (java.sql.Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.fk_Community</code>.
	 */
	public void setFkCommunity(java.lang.Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.fk_Community</code>.
	 */
	public java.lang.Integer getFkCommunity() {
		return (java.lang.Integer) getValue(6);
	}

	/**
	 * Setter for <code>oli_lunchy.pictures.up_Votes</code>.
	 */
	public void setUpVotes(java.lang.Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>oli_lunchy.pictures.up_Votes</code>.
	 */
	public java.lang.Integer getUpVotes() {
		return (java.lang.Integer) getValue(7);
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
	// Record8 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row8<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row8) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row8<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row8) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.FK_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.FK_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.FILENAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.CAPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.CREATED_ON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.FK_COMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES.UP_VOTES;
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
		return getFkLocation();
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
	public java.lang.String value4() {
		return getFilename();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getCaption();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getCreatedOn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getFkCommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getUpVotes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value2(java.lang.Integer value) {
		setFkLocation(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value3(java.lang.Integer value) {
		setFkUser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value4(java.lang.String value) {
		setFilename(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value5(java.lang.String value) {
		setCaption(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value6(java.sql.Timestamp value) {
		setCreatedOn(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value7(java.lang.Integer value) {
		setFkCommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord value8(java.lang.Integer value) {
		setUpVotes(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PicturesRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.String value4, java.lang.String value5, java.sql.Timestamp value6, java.lang.Integer value7, java.lang.Integer value8) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached PicturesRecord
	 */
	public PicturesRecord() {
		super(de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES);
	}

	/**
	 * Create a detached, initialised PicturesRecord
	 */
	public PicturesRecord(java.lang.Integer id, java.lang.Integer fkLocation, java.lang.Integer fkUser, java.lang.String filename, java.lang.String caption, java.sql.Timestamp createdOn, java.lang.Integer fkCommunity, java.lang.Integer upVotes) {
		super(de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES);

		setValue(0, id);
		setValue(1, fkLocation);
		setValue(2, fkUser);
		setValue(3, filename);
		setValue(4, caption);
		setValue(5, createdOn);
		setValue(6, fkCommunity);
		setValue(7, upVotes);
	}
}
