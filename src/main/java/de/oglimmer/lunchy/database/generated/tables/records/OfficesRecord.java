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
public class OfficesRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> implements org.jooq.Record6<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer> {

	private static final long serialVersionUID = 1727998303;

	/**
	 * Setter for <code>oli_lunchy.offices.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.fkCommunity</code>.
	 */
	public void setFkcommunity(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.fkCommunity</code>.
	 */
	public java.lang.Integer getFkcommunity() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.geo_lat</code>.
	 */
	public void setGeoLat(java.lang.Double value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.geo_lat</code>.
	 */
	public java.lang.Double getGeoLat() {
		return (java.lang.Double) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.geo_lng</code>.
	 */
	public void setGeoLng(java.lang.Double value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.geo_lng</code>.
	 */
	public java.lang.Double getGeoLng() {
		return (java.lang.Double) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.zoomfactor</code>.
	 */
	public void setZoomfactor(java.lang.Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.zoomfactor</code>.
	 */
	public java.lang.Integer getZoomfactor() {
		return (java.lang.Integer) getValue(5);
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
	// Record6 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row6) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer> valuesRow() {
		return (org.jooq.Row6) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.FKCOMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.GEO_LAT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field5() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.GEO_LNG;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.ZOOMFACTOR;
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
	public java.lang.String value3() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value4() {
		return getGeoLat();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value5() {
		return getGeoLng();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getZoomfactor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value2(java.lang.Integer value) {
		setFkcommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value3(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value4(java.lang.Double value) {
		setGeoLat(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value5(java.lang.Double value) {
		setGeoLng(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value6(java.lang.Integer value) {
		setZoomfactor(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.String value3, java.lang.Double value4, java.lang.Double value5, java.lang.Integer value6) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached OfficesRecord
	 */
	public OfficesRecord() {
		super(de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES);
	}

	/**
	 * Create a detached, initialised OfficesRecord
	 */
	public OfficesRecord(java.lang.Integer id, java.lang.Integer fkcommunity, java.lang.String name, java.lang.Double geoLat, java.lang.Double geoLng, java.lang.Integer zoomfactor) {
		super(de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES);

		setValue(0, id);
		setValue(1, fkcommunity);
		setValue(2, name);
		setValue(3, geoLat);
		setValue(4, geoLng);
		setValue(5, zoomfactor);
	}
}
