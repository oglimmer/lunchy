/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables;


import de.oglimmer.lunchy.database.generated.Keys;
import de.oglimmer.lunchy.database.generated.OliLunchy;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class Location extends TableImpl<LocationRecord> {

	private static final long serialVersionUID = -1379369693;

	/**
	 * The reference instance of <code>oli_lunchy.location</code>
	 */
	public static final Location LOCATION = new Location();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<LocationRecord> getRecordType() {
		return LocationRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.location.id</code>.
	 */
	public final TableField<LocationRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.official_Name</code>.
	 */
	public final TableField<LocationRecord, String> OFFICIAL_NAME = createField("official_Name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.street_Name</code>.
	 */
	public final TableField<LocationRecord, String> STREET_NAME = createField("street_Name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.location.address</code>.
	 */
	public final TableField<LocationRecord, String> ADDRESS = createField("address", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.city</code>.
	 */
	public final TableField<LocationRecord, String> CITY = createField("city", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.zip</code>.
	 */
	public final TableField<LocationRecord, String> ZIP = createField("zip", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.location.country</code>.
	 */
	public final TableField<LocationRecord, String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.url</code>.
	 */
	public final TableField<LocationRecord, String> URL = createField("url", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.location.comment</code>.
	 */
	public final TableField<LocationRecord, String> COMMENT = createField("comment", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>oli_lunchy.location.turn_Around_Time</code>.
	 */
	public final TableField<LocationRecord, Integer> TURN_AROUND_TIME = createField("turn_Around_Time", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>oli_lunchy.location.created_On</code>.
	 */
	public final TableField<LocationRecord, Timestamp> CREATED_ON = createField("created_On", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.last_Update</code>.
	 */
	public final TableField<LocationRecord, Timestamp> LAST_UPDATE = createField("last_Update", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.location.fk_User</code>.
	 */
	public final TableField<LocationRecord, Integer> FK_USER = createField("fk_User", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.geo_Lat</code>.
	 */
	public final TableField<LocationRecord, Double> GEO_LAT = createField("geo_Lat", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>oli_lunchy.location.geo_Lng</code>.
	 */
	public final TableField<LocationRecord, Double> GEO_LNG = createField("geo_Lng", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>oli_lunchy.location.tags</code>.
	 */
	public final TableField<LocationRecord, String> TAGS = createField("tags", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>oli_lunchy.location.fk_Office</code>.
	 */
	public final TableField<LocationRecord, Integer> FK_OFFICE = createField("fk_Office", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.fk_Community</code>.
	 */
	public final TableField<LocationRecord, Integer> FK_COMMUNITY = createField("fk_Community", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.geo_Moved_Manually</code>.
	 */
	public final TableField<LocationRecord, Byte> GEO_MOVED_MANUALLY = createField("geo_Moved_Manually", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.location.archived</code>.
	 */
	public final TableField<LocationRecord, Integer> ARCHIVED = createField("archived", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>oli_lunchy.location</code> table reference
	 */
	public Location() {
		this("location", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.location</code> table reference
	 */
	public Location(String alias) {
		this(alias, LOCATION);
	}

	private Location(String alias, Table<LocationRecord> aliased) {
		this(alias, aliased, null);
	}

	private Location(String alias, Table<LocationRecord> aliased, Field<?>[] parameters) {
		super(alias, OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<LocationRecord, Integer> getIdentity() {
		return Keys.IDENTITY_LOCATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<LocationRecord> getPrimaryKey() {
		return Keys.KEY_LOCATION_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<LocationRecord>> getKeys() {
		return Arrays.<UniqueKey<LocationRecord>>asList(Keys.KEY_LOCATION_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<LocationRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<LocationRecord, ?>>asList(Keys.FK_LOC_USR, Keys.FK_LOC_OFF, Keys.FK_LOC_COM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Location as(String alias) {
		return new Location(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Location rename(String name) {
		return new Location(name, null);
	}
}
