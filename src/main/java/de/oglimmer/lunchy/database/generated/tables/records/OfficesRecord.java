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
public class OfficesRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double> {

	private static final long serialVersionUID = 1783287536;

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
	 * Setter for <code>oli_lunchy.offices.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.geo_lat</code>.
	 */
	public void setGeoLat(java.lang.Double value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.geo_lat</code>.
	 */
	public java.lang.Double getGeoLat() {
		return (java.lang.Double) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.offices.geo_lng</code>.
	 */
	public void setGeoLng(java.lang.Double value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.offices.geo_lng</code>.
	 */
	public java.lang.Double getGeoLng() {
		return (java.lang.Double) getValue(3);
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
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
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
	public org.jooq.Field<java.lang.String> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.GEO_LAT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES.GEO_LNG;
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
	public java.lang.String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value3() {
		return getGeoLat();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value4() {
		return getGeoLng();
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
	public OfficesRecord value2(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value3(java.lang.Double value) {
		setGeoLat(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord value4(java.lang.Double value) {
		setGeoLng(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OfficesRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.Double value3, java.lang.Double value4) {
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
	public OfficesRecord(java.lang.Integer id, java.lang.String name, java.lang.Double geoLat, java.lang.Double geoLng) {
		super(de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES);

		setValue(0, id);
		setValue(1, name);
		setValue(2, geoLat);
		setValue(3, geoLng);
	}
}