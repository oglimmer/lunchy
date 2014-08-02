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
public class Users extends org.jooq.impl.TableImpl<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> {

	private static final long serialVersionUID = -345402541;

	/**
	 * The singleton instance of <code>oli_lunchy.users</code>
	 */
	public static final de.oglimmer.lunchy.database.generated.tables.Users USERS = new de.oglimmer.lunchy.database.generated.tables.Users();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> getRecordType() {
		return de.oglimmer.lunchy.database.generated.tables.records.UsersRecord.class;
	}

	/**
	 * The column <code>oli_lunchy.users.id</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>oli_lunchy.users.email</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.lang.String> EMAIL = createField("email", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.users.password</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.lang.String> PASSWORD = createField("password", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.users.displayname</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.lang.String> DISPLAYNAME = createField("displayname", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.users.createdOn</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.sql.Timestamp> CREATEDON = createField("createdOn", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.users.lastLogin</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.sql.Timestamp> LASTLOGIN = createField("lastLogin", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>oli_lunchy.users.permissions</code>.
	 */
	public final org.jooq.TableField<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.lang.Integer> PERMISSIONS = createField("permissions", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>oli_lunchy.users</code> table reference
	 */
	public Users() {
		this("users", null);
	}

	/**
	 * Create an aliased <code>oli_lunchy.users</code> table reference
	 */
	public Users(java.lang.String alias) {
		this(alias, de.oglimmer.lunchy.database.generated.tables.Users.USERS);
	}

	private Users(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> aliased) {
		this(alias, aliased, null);
	}

	private Users(java.lang.String alias, org.jooq.Table<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, de.oglimmer.lunchy.database.generated.OliLunchy.OLI_LUNCHY, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord, java.lang.Integer> getIdentity() {
		return de.oglimmer.lunchy.database.generated.Keys.IDENTITY_USERS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> getPrimaryKey() {
		return de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord>>asList(de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_PRIMARY, de.oglimmer.lunchy.database.generated.Keys.KEY_USERS_EMAIL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public de.oglimmer.lunchy.database.generated.tables.Users as(java.lang.String alias) {
		return new de.oglimmer.lunchy.database.generated.tables.Users(alias, this);
	}

	/**
	 * Rename this table
	 */
	public de.oglimmer.lunchy.database.generated.tables.Users rename(java.lang.String name) {
		return new de.oglimmer.lunchy.database.generated.tables.Users(name, null);
	}
}
