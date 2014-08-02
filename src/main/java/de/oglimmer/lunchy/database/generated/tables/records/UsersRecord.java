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
public class UsersRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> implements org.jooq.Record7<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer> {

	private static final long serialVersionUID = -613027174;

	/**
	 * Setter for <code>oli_lunchy.users.id</code>.
	 */
	public void setId(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.id</code>.
	 */
	public java.lang.Integer getId() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.users.email</code>.
	 */
	public void setEmail(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.email</code>.
	 */
	public java.lang.String getEmail() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.users.password</code>.
	 */
	public void setPassword(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.password</code>.
	 */
	public java.lang.String getPassword() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.users.displayname</code>.
	 */
	public void setDisplayname(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.displayname</code>.
	 */
	public java.lang.String getDisplayname() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.users.createdOn</code>.
	 */
	public void setCreatedon(java.sql.Timestamp value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.createdOn</code>.
	 */
	public java.sql.Timestamp getCreatedon() {
		return (java.sql.Timestamp) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.users.lastLogin</code>.
	 */
	public void setLastlogin(java.sql.Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.lastLogin</code>.
	 */
	public java.sql.Timestamp getLastlogin() {
		return (java.sql.Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.users.permissions</code>.
	 */
	public void setPermissions(java.lang.Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.permissions</code>.
	 */
	public java.lang.Integer getPermissions() {
		return (java.lang.Integer) getValue(6);
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
	// Record7 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row7<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row7) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row7<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer> valuesRow() {
		return (org.jooq.Row7) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.EMAIL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.PASSWORD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.DISPLAYNAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field5() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.CREATEDON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.LASTLOGIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.PERMISSIONS;
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
		return getEmail();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getPassword();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getDisplayname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value5() {
		return getCreatedon();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getLastlogin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getPermissions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value1(java.lang.Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value2(java.lang.String value) {
		setEmail(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value3(java.lang.String value) {
		setPassword(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value4(java.lang.String value) {
		setDisplayname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value5(java.sql.Timestamp value) {
		setCreatedon(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value6(java.sql.Timestamp value) {
		setLastlogin(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value7(java.lang.Integer value) {
		setPermissions(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.String value4, java.sql.Timestamp value5, java.sql.Timestamp value6, java.lang.Integer value7) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UsersRecord
	 */
	public UsersRecord() {
		super(de.oglimmer.lunchy.database.generated.tables.Users.USERS);
	}

	/**
	 * Create a detached, initialised UsersRecord
	 */
	public UsersRecord(java.lang.Integer id, java.lang.String email, java.lang.String password, java.lang.String displayname, java.sql.Timestamp createdon, java.sql.Timestamp lastlogin, java.lang.Integer permissions) {
		super(de.oglimmer.lunchy.database.generated.tables.Users.USERS);

		setValue(0, id);
		setValue(1, email);
		setValue(2, password);
		setValue(3, displayname);
		setValue(4, createdon);
		setValue(5, lastlogin);
		setValue(6, permissions);
	}
}
