/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Location extends org.jooq.impl.TableImpl<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord> {

	private static final long serialVersionUID = 848083024;

	/**
	 * The singleton instance of <code>oli_lunchy.location</code>
	 */
	public static final de.oglimmer.lunchy.database.generated.tables.Location LOCATION = new de.oglimmer.lunchy.database.generated.tables.Location();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord> getRecordType() {
		return de.oglimmer.lunchy.database.generated.tables.records.LocationRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.location.id</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.officialname</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> OFFICIALNAME = createField("officialname", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.streetname</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> STREETNAME = createField("streetname", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.location.address</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> ADDRESS = createField("address", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.city</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> CITY = createField("city", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.zip</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> ZIP = createField("zip", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.country</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.url</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> URL = createField("url", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.location.comment</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.String> COMMENT = createField("comment", org.jooq.impl.SQLDataType.CLOB.length(65535), this, "");

	/**
	 * The column <code>oli_lunchy.location.turnAroundTime</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.Integer> TURNAROUNDTIME = createField("turnAroundTime", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.createdOn</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.sql.Timestamp> CREATEDON = createField("createdOn", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.lastUpdate</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.sql.Timestamp> LASTUPDATE = createField("lastUpdate", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.fkUser</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.Integer> FKUSER = createField("fkUser", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.geo_lat</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.Double> GEO_LAT = createField("geo_lat", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>oli_lunchy.location.geo_lng</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.Double> GEO_LNG = createField("geo_lng", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * Create a <code>oli_lunchy.location</code> table reference
	 */
	public Location() {
		this("location", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.location</code> table reference
	 */
	public Location(java.lang.String alias) {
		this(alias, de.oglimmer.lunchy.database.generated.tables.Location.LOCATION);
	}

	private Location(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord> aliased) {
		this(alias, aliased, null);
	}

	private Location(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, de.oglimmer.lunchy.database.generated.OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord, java.lang.Integer> getIdentity() {
		return de.oglimmer.lunchy.database.generated.Keys.IDENTITY_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord> getPrimaryKey() {
		return de.oglimmer.lunchy.database.generated.Keys.KEY_LOCATION_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.LocationRecord>>asList(de.oglimmer.lunchy.database.generated.Keys.KEY_LOCATION_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public de.oglimmer.lunchy.database.generated.tables.Location as(java.lang.String alias) {
		return new de.oglimmer.lunchy.database.generated.tables.Location(alias, this);
	}

	/**
	 * Rename this table
	 */
	public de.oglimmer.lunchy.database.generated.tables.Location rename(java.lang.String name) {
		return new de.oglimmer.lunchy.database.generated.tables.Location(name, null);
	}
}
