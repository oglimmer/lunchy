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
public class Offices extends org.jooq.impl.TableImpl<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> {

	private static final long serialVersionUID = 1259325586;

	/**
	 * The singleton instance of <code>oli_lunchy.offices</code>
	 */
	public static final de.oglimmer.lunchy.database.generated.tables.Offices OFFICES = new de.oglimmer.lunchy.database.generated.tables.Offices();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> getRecordType() {
		return de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.offices.id</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.offices.name</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.offices.geo_lat</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord, java.lang.Double> GEO_LAT = createField("geo_lat", org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.offices.geo_lng</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord, java.lang.Double> GEO_LNG = createField("geo_lng", org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

	/**
	 * Create a <code>oli_lunchy.offices</code> table reference
	 */
	public Offices() {
		this("offices", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.offices</code> table reference
	 */
	public Offices(java.lang.String alias) {
		this(alias, de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES);
	}

	private Offices(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> aliased) {
		this(alias, aliased, null);
	}

	private Offices(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, de.oglimmer.lunchy.database.generated.OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord, java.lang.Integer> getIdentity() {
		return de.oglimmer.lunchy.database.generated.Keys.IDENTITY_OFFICES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord> getPrimaryKey() {
		return de.oglimmer.lunchy.database.generated.Keys.KEY_OFFICES_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord>>asList(de.oglimmer.lunchy.database.generated.Keys.KEY_OFFICES_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public de.oglimmer.lunchy.database.generated.tables.Offices as(java.lang.String alias) {
		return new de.oglimmer.lunchy.database.generated.tables.Offices(alias, this);
	}

	/**
	 * Rename this table
	 */
	public de.oglimmer.lunchy.database.generated.tables.Offices rename(java.lang.String name) {
		return new de.oglimmer.lunchy.database.generated.tables.Offices(name, null);
	}
}
