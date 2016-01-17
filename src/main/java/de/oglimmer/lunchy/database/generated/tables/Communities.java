/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables;


import de.oglimmer.lunchy.database.generated.Keys;
import de.oglimmer.lunchy.database.generated.OliLunchy;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;

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
public class Communities extends TableImpl<CommunitiesRecord> {

	private static final long serialVersionUID = 766281747;

	/**
	 * The reference instance of <code>oli_lunchy.communities</code>
	 */
	public static final Communities COMMUNITIES = new Communities();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<CommunitiesRecord> getRecordType() {
		return CommunitiesRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.communities.id</code>.
	 */
	public final TableField<CommunitiesRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.communities.name</code>.
	 */
	public final TableField<CommunitiesRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.communities.domain</code>.
	 */
	public final TableField<CommunitiesRecord, String> DOMAIN = createField("domain", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.communities.admin_Email</code>.
	 */
	public final TableField<CommunitiesRecord, String> ADMIN_EMAIL = createField("admin_Email", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

	/**
	 * Create a <code>oli_lunchy.communities</code> table reference
	 */
	public Communities() {
		this("communities", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.communities</code> table reference
	 */
	public Communities(String alias) {
		this(alias, COMMUNITIES);
	}

	private Communities(String alias, Table<CommunitiesRecord> aliased) {
		this(alias, aliased, null);
	}

	private Communities(String alias, Table<CommunitiesRecord> aliased, Field<?>[] parameters) {
		super(alias, OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<CommunitiesRecord, Integer> getIdentity() {
		return Keys.IDENTITY_COMMUNITIES;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<CommunitiesRecord> getPrimaryKey() {
		return Keys.KEY_COMMUNITIES_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<CommunitiesRecord>> getKeys() {
		return Arrays.<UniqueKey<CommunitiesRecord>>asList(Keys.KEY_COMMUNITIES_PRIMARY, Keys.KEY_COMMUNITIES_UNIQ_DOMAIN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Communities as(String alias) {
		return new Communities(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Communities rename(String name) {
		return new Communities(name, null);
	}
}
