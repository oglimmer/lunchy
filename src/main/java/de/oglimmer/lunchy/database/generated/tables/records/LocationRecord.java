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
public class LocationRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord> implements org.jooq.Record16<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.Double, java.lang.Double, java.lang.String> {

	private static final long serialVersionUID = 556933551;

	/**
	 * Setter for <code>oli_lunchy.location.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.location.officialname</code>.
	 */
	public void setOfficialname(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.officialname</code>.
	 */
	public java.lang.String getOfficialname() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.location.streetname</code>.
	 */
	public void setStreetname(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.streetname</code>.
	 */
	public java.lang.String getStreetname() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.location.address</code>.
	 */
	public void setAddress(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.address</code>.
	 */
	public java.lang.String getAddress() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.location.city</code>.
	 */
	public void setCity(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.city</code>.
	 */
	public java.lang.String getCity() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.location.zip</code>.
	 */
	public void setZip(java.lang.String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.zip</code>.
	 */
	public java.lang.String getZip() {
		return (java.lang.String) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.location.country</code>.
	 */
	public void setCountry(java.lang.String value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.country</code>.
	 */
	public java.lang.String getCountry() {
		return (java.lang.String) getValue(6);
	}

	/**
	 * Setter for <code>oli_lunchy.location.url</code>.
	 */
	public void setUrl(java.lang.String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.url</code>.
	 */
	public java.lang.String getUrl() {
		return (java.lang.String) getValue(7);
	}

	/**
	 * Setter for <code>oli_lunchy.location.comment</code>.
	 */
	public void setComment(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.comment</code>.
	 */
	public java.lang.String getComment() {
		return (java.lang.String) getValue(8);
	}

	/**
	 * Setter for <code>oli_lunchy.location.turnAroundTime</code>.
	 */
	public void setTurnaroundtime(java.lang.Integer value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.turnAroundTime</code>.
	 */
	public java.lang.Integer getTurnaroundtime() {
		return (java.lang.Integer) getValue(9);
	}

	/**
	 * Setter for <code>oli_lunchy.location.createdOn</code>.
	 */
	public void setCreatedon(java.sql.Timestamp value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.createdOn</code>.
	 */
	public java.sql.Timestamp getCreatedon() {
		return (java.sql.Timestamp) getValue(10);
	}

	/**
	 * Setter for <code>oli_lunchy.location.lastUpdate</code>.
	 */
	public void setLastupdate(java.sql.Timestamp value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.lastUpdate</code>.
	 */
	public java.sql.Timestamp getLastupdate() {
		return (java.sql.Timestamp) getValue(11);
	}

	/**
	 * Setter for <code>oli_lunchy.location.fkUser</code>.
	 */
	public void setFkuser(java.lang.Integer value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.fkUser</code>.
	 */
	public java.lang.Integer getFkuser() {
		return (java.lang.Integer) getValue(12);
	}

	/**
	 * Setter for <code>oli_lunchy.location.geo_lat</code>.
	 */
	public void setGeoLat(java.lang.Double value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.geo_lat</code>.
	 */
	public java.lang.Double getGeoLat() {
		return (java.lang.Double) getValue(13);
	}

	/**
	 * Setter for <code>oli_lunchy.location.geo_lng</code>.
	 */
	public void setGeoLng(java.lang.Double value) {
		setValue(14, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.geo_lng</code>.
	 */
	public java.lang.Double getGeoLng() {
		return (java.lang.Double) getValue(14);
	}

	/**
	 * Setter for <code>oli_lunchy.location.tags</code>.
	 */
	public void setTags(java.lang.String value) {
		setValue(15, value);
	}

	/**
	 * Getter for <code>oli_lunchy.location.tags</code>.
	 */
	public java.lang.String getTags() {
		return (java.lang.String) getValue(15);
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
	// Record16 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row16<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.Double, java.lang.Double, java.lang.String> fieldsRow() {
		return (org.jooq.Row16) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row16<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.Double, java.lang.Double, java.lang.String> valuesRow() {
		return (org.jooq.Row16) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.OFFICIALNAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.STREETNAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.ADDRESS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.CITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field6() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.ZIP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field7() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.COUNTRY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field8() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.URL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.COMMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field10() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.TURNAROUNDTIME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field11() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.CREATEDON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field12() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.LASTUPDATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field13() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.FKUSER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field14() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.GEO_LAT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Double> field15() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.GEO_LNG;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field16() {
		return de.oglimmer.lunchy.database.generated.tables.Location.LOCATION.TAGS;
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
		return getOfficialname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getStreetname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getAddress();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getCity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value6() {
		return getZip();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value7() {
		return getCountry();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value8() {
		return getUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getComment();
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
	public java.sql.Timestamp value11() {
		return getCreatedon();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value12() {
		return getLastupdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value13() {
		return getFkuser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value14() {
		return getGeoLat();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Double value15() {
		return getGeoLng();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value16() {
		return getTags();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value2(java.lang.String value) {
		setOfficialname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value3(java.lang.String value) {
		setStreetname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value4(java.lang.String value) {
		setAddress(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value5(java.lang.String value) {
		setCity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value6(java.lang.String value) {
		setZip(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value7(java.lang.String value) {
		setCountry(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value8(java.lang.String value) {
		setUrl(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value9(java.lang.String value) {
		setComment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value10(java.lang.Integer value) {
		setTurnaroundtime(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value11(java.sql.Timestamp value) {
		setCreatedon(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value12(java.sql.Timestamp value) {
		setLastupdate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value13(java.lang.Integer value) {
		setFkuser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value14(java.lang.Double value) {
		setGeoLat(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value15(java.lang.Double value) {
		setGeoLng(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord value16(java.lang.String value) {
		setTags(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LocationRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.String value4, java.lang.String value5, java.lang.String value6, java.lang.String value7, java.lang.String value8, java.lang.String value9, java.lang.Integer value10, java.sql.Timestamp value11, java.sql.Timestamp value12, java.lang.Integer value13, java.lang.Double value14, java.lang.Double value15, java.lang.String value16) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached LocationRecord
	 */
	public LocationRecord() {
		super(de.oglimmer.lunchy.database.generated.tables.Location.LOCATION);
	}

	/**
	 * Create a detached, initialised LocationRecord
	 */
	public LocationRecord(java.lang.Integer id, java.lang.String officialname, java.lang.String streetname, java.lang.String address, java.lang.String city, java.lang.String zip, java.lang.String country, java.lang.String url, java.lang.String comment, java.lang.Integer turnaroundtime, java.sql.Timestamp createdon, java.sql.Timestamp lastupdate, java.lang.Integer fkuser, java.lang.Double geoLat, java.lang.Double geoLng, java.lang.String tags) {
		super(de.oglimmer.lunchy.database.generated.tables.Location.LOCATION);

		setValue(0, id);
		setValue(1, officialname);
		setValue(2, streetname);
		setValue(3, address);
		setValue(4, city);
		setValue(5, zip);
		setValue(6, country);
		setValue(7, url);
		setValue(8, comment);
		setValue(9, turnaroundtime);
		setValue(10, createdon);
		setValue(11, lastupdate);
		setValue(12, fkuser);
		setValue(13, geoLat);
		setValue(14, geoLng);
		setValue(15, tags);
	}
}
