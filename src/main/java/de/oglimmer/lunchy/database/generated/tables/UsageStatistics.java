/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables;


import de.oglimmer.lunchy.database.generated.Keys;
import de.oglimmer.lunchy.database.generated.OliLunchy;
import de.oglimmer.lunchy.database.generated.tables.records.UsageStatisticsRecord;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
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
public class UsageStatistics extends TableImpl<UsageStatisticsRecord> {

	private static final long serialVersionUID = -837129340;

	/**
	 * The reference instance of <code>oli_lunchy.usage_statistics</code>
	 */
	public static final UsageStatistics USAGE_STATISTICS = new UsageStatistics();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<UsageStatisticsRecord> getRecordType() {
		return UsageStatisticsRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.usage_statistics.id</code>.
	 */
	public final TableField<UsageStatisticsRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.action</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> ACTION = createField("action", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.context</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> CONTEXT = createField("context", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.created_on</code>.
	 */
	public final TableField<UsageStatisticsRecord, Timestamp> CREATED_ON = createField("created_on", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.ip</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> IP = createField("ip", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.user-agent</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> USER_AGENT = createField("user-agent", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.user-id</code>.
	 */
	public final TableField<UsageStatisticsRecord, Integer> USER_ID = createField("user-id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.user-cookie</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> USER_COOKIE = createField("user-cookie", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.domain</code>.
	 */
	public final TableField<UsageStatisticsRecord, Integer> DOMAIN = createField("domain", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.city</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> CITY = createField("city", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.country</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> COUNTRY = createField("country", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>oli_lunchy.usage_statistics.referer</code>.
	 */
	public final TableField<UsageStatisticsRecord, String> REFERER = createField("referer", org.jooq.impl.SQLDataType.VARCHAR.length(2000), this, "");

	/**
	 * Create a <code>oli_lunchy.usage_statistics</code> table reference
	 */
	public UsageStatistics() {
		this("usage_statistics", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.usage_statistics</code> table reference
	 */
	public UsageStatistics(String alias) {
		this(alias, USAGE_STATISTICS);
	}

	private UsageStatistics(String alias, Table<UsageStatisticsRecord> aliased) {
		this(alias, aliased, null);
	}

	private UsageStatistics(String alias, Table<UsageStatisticsRecord> aliased, Field<?>[] parameters) {
		super(alias, OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<UsageStatisticsRecord, Integer> getIdentity() {
		return Keys.IDENTITY_USAGE_STATISTICS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<UsageStatisticsRecord> getPrimaryKey() {
		return Keys.KEY_USAGE_STATISTICS_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<UsageStatisticsRecord>> getKeys() {
		return Arrays.<UniqueKey<UsageStatisticsRecord>>asList(Keys.KEY_USAGE_STATISTICS_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatistics as(String alias) {
		return new UsageStatistics(alias, this);
	}

	/**
	 * Rename this table
	 */
	public UsageStatistics rename(String name) {
		return new UsageStatistics(name, null);
	}
}
