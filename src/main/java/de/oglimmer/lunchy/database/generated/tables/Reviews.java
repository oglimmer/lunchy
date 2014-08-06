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
public class Reviews extends org.jooq.impl.TableImpl<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord> {

	private static final long serialVersionUID = 1749619803;

	/**
	 * The singleton instance of <code>oli_lunchy.reviews</code>
	 */
	public static final de.oglimmer.lunchy.database.generated.tables.Reviews REVIEWS = new de.oglimmer.lunchy.database.generated.tables.Reviews();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord> getRecordType() {
		return de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.reviews.id</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.reviews.fkUser</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.Integer> FKUSER = createField("fkUser", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.reviews.fkLocation</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.Integer> FKLOCATION = createField("fkLocation", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.reviews.comment</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.String> COMMENT = createField("comment", org.jooq.impl.SQLDataType.CLOB.length(65535), this, "");

	/**
	 * The column <code>oli_lunchy.reviews.createdOn</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.sql.Timestamp> CREATEDON = createField("createdOn", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.reviews.lastUpdate</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.sql.Timestamp> LASTUPDATE = createField("lastUpdate", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.reviews.rating</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.Integer> RATING = createField("rating", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>oli_lunchy.reviews.favoriteMeal</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.String> FAVORITEMEAL = createField("favoriteMeal", org.jooq.impl.SQLDataType.CLOB.length(65535), this, "");

	/**
	 * Create a <code>oli_lunchy.reviews</code> table reference
	 */
	public Reviews() {
		this("reviews", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.reviews</code> table reference
	 */
	public Reviews(java.lang.String alias) {
		this(alias, de.oglimmer.lunchy.database.generated.tables.Reviews.REVIEWS);
	}

	private Reviews(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord> aliased) {
		this(alias, aliased, null);
	}

	private Reviews(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, de.oglimmer.lunchy.database.generated.OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord, java.lang.Integer> getIdentity() {
		return de.oglimmer.lunchy.database.generated.Keys.IDENTITY_REVIEWS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord> getPrimaryKey() {
		return de.oglimmer.lunchy.database.generated.Keys.KEY_REVIEWS_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord>>asList(de.oglimmer.lunchy.database.generated.Keys.KEY_REVIEWS_PRIMARY, de.oglimmer.lunchy.database.generated.Keys.KEY_REVIEWS_FKUSER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public de.oglimmer.lunchy.database.generated.tables.Reviews as(java.lang.String alias) {
		return new de.oglimmer.lunchy.database.generated.tables.Reviews(alias, this);
	}

	/**
	 * Rename this table
	 */
	public de.oglimmer.lunchy.database.generated.tables.Reviews rename(java.lang.String name) {
		return new de.oglimmer.lunchy.database.generated.tables.Reviews(name, null);
	}
}
