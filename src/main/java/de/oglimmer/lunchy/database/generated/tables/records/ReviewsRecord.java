/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ReviewsRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord> implements org.jooq.Record10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.String, java.lang.Integer> {

	private static final long serialVersionUID = -1498427441;

	/**
	 * Setter for <code>oli_lunchy.reviews.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.fkCommunity</code>.
	 */
	public void setFkcommunity(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.fkCommunity</code>.
	 */
	public java.lang.Integer getFkcommunity() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.fkUser</code>.
	 */
	public void setFkuser(java.lang.Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.fkUser</code>.
	 */
	public java.lang.Integer getFkuser() {
		return (java.lang.Integer) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.fkLocation</code>.
	 */
	public void setFklocation(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.fkLocation</code>.
	 */
	public java.lang.Integer getFklocation() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.comment</code>.
	 */
	public void setComment(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.comment</code>.
	 */
	public java.lang.String getComment() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.createdOn</code>.
	 */
	public void setCreatedon(java.sql.Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.createdOn</code>.
	 */
	public java.sql.Timestamp getCreatedon() {
		return (java.sql.Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.lastUpdate</code>.
	 */
	public void setLastupdate(java.sql.Timestamp value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.lastUpdate</code>.
	 */
	public java.sql.Timestamp getLastupdate() {
		return (java.sql.Timestamp) getValue(6);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.rating</code>.
	 */
	public void setRating(java.lang.Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.rating</code>.
	 */
	public java.lang.Integer getRating() {
		return (java.lang.Integer) getValue(7);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.favoriteMeal</code>.
	 */
	public void setFavoritemeal(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.favoriteMeal</code>.
	 */
	public java.lang.String getFavoritemeal() {
		return (java.lang.String) getValue(8);
	}

	/**
	 * Setter for <code>oli_lunchy.reviews.turnAroundTime</code>.
	 */
	public void setTurnaroundtime(java.lang.Integer value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>oli_lunchy.reviews.turnAroundTime</code>.
	 */
	public java.lang.Integer getTurnaroundtime() {
		return (java.lang.Integer) getValue(9);
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
	// Record10 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.String, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row10) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row10<java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.String, java.lang.Integer> valuesRow() {
		return (org.jooq.Row10) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.FKCOMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.FKUSER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.FKLOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.COMMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.CREATEDON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field7() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.LASTUPDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.RATING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.FAVORITEMEAL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field10() {
		return de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS.TURNAROUNDTIME;
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
		return getFkcommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value3() {
		return getFkuser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getFklocation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getComment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getCreatedon();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value7() {
		return getLastupdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getRating();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getFavoritemeal();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value10() {
		return getTurnaroundtime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value2(java.lang.Integer value) {
		setFkcommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value3(java.lang.Integer value) {
		setFkuser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value4(java.lang.Integer value) {
		setFklocation(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value5(java.lang.String value) {
		setComment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value6(java.sql.Timestamp value) {
		setCreatedon(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value7(java.sql.Timestamp value) {
		setLastupdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value8(java.lang.Integer value) {
		setRating(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value9(java.lang.String value) {
		setFavoritemeal(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord value10(java.lang.Integer value) {
		setTurnaroundtime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReviewsRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.Integer value3, java.lang.Integer value4, java.lang.String value5, java.sql.Timestamp value6, java.sql.Timestamp value7, java.lang.Integer value8, java.lang.String value9, java.lang.Integer value10) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ReviewsRecord
	 */
	public ReviewsRecord() {
		super(de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS);
	}

	/**
	 * Create a detached, initialised ReviewsRecord
	 */
	public ReviewsRecord(java.lang.Integer id, java.lang.Integer fkcommunity, java.lang.Integer fkuser, java.lang.Integer fklocation, java.lang.String comment, java.sql.Timestamp createdon, java.sql.Timestamp lastupdate, java.lang.Integer rating, java.lang.String favoritemeal, java.lang.Integer turnaroundtime) {
		super(de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS);

		setValue(0, id);
		setValue(1, fkcommunity);
		setValue(2, fkuser);
		setValue(3, fklocation);
		setValue(4, comment);
		setValue(5, createdon);
		setValue(6, lastupdate);
		setValue(7, rating);
		setValue(8, favoritemeal);
		setValue(9, turnaroundtime);
	}
}
