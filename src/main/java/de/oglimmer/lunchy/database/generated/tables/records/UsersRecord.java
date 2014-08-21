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
public class UsersRecord extends org.jooq.impl.UpdatableRecordImpl<de.oglimmer.lunchy.database.generated.tables.records.UsersRecord> implements org.jooq.Record13<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.String, java.sql.Timestamp, java.lang.String, java.sql.Timestamp, java.lang.Integer> {

	private static final long serialVersionUID = -1873135037;

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
	 * Setter for <code>oli_lunchy.users.fk_Community</code>.
	 */
	public void setFkCommunity(java.lang.Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.fk_Community</code>.
	 */
	public java.lang.Integer getFkCommunity() {
		return (java.lang.Integer) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.users.email</code>.
	 */
	public void setEmail(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.email</code>.
	 */
	public java.lang.String getEmail() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.users.password</code>.
	 */
	public void setPassword(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.password</code>.
	 */
	public java.lang.String getPassword() {
		return (java.lang.String) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.users.displayname</code>.
	 */
	public void setDisplayname(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.displayname</code>.
	 */
	public java.lang.String getDisplayname() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.users.created_On</code>.
	 */
	public void setCreatedOn(java.sql.Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.created_On</code>.
	 */
	public java.sql.Timestamp getCreatedOn() {
		return (java.sql.Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.users.last_Login</code>.
	 */
	public void setLastLogin(java.sql.Timestamp value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.last_Login</code>.
	 */
	public java.sql.Timestamp getLastLogin() {
		return (java.sql.Timestamp) getValue(6);
	}

	/**
	 * Setter for <code>oli_lunchy.users.permissions</code>.
	 */
	public void setPermissions(java.lang.Integer value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.permissions</code>.
	 */
	public java.lang.Integer getPermissions() {
		return (java.lang.Integer) getValue(7);
	}

	/**
	 * Setter for <code>oli_lunchy.users.password_Reset_Token</code>.
	 */
	public void setPasswordResetToken(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.password_Reset_Token</code>.
	 */
	public java.lang.String getPasswordResetToken() {
		return (java.lang.String) getValue(8);
	}

	/**
	 * Setter for <code>oli_lunchy.users.password_Reset_Timestamp</code>.
	 */
	public void setPasswordResetTimestamp(java.sql.Timestamp value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.password_Reset_Timestamp</code>.
	 */
	public java.sql.Timestamp getPasswordResetTimestamp() {
		return (java.sql.Timestamp) getValue(9);
	}

	/**
	 * Setter for <code>oli_lunchy.users.long_Time_Token</code>.
	 */
	public void setLongTimeToken(java.lang.String value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.long_Time_Token</code>.
	 */
	public java.lang.String getLongTimeToken() {
		return (java.lang.String) getValue(10);
	}

	/**
	 * Setter for <code>oli_lunchy.users.long_Time_Timestamp</code>.
	 */
	public void setLongTimeTimestamp(java.sql.Timestamp value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.long_Time_Timestamp</code>.
	 */
	public java.sql.Timestamp getLongTimeTimestamp() {
		return (java.sql.Timestamp) getValue(11);
	}

	/**
	 * Setter for <code>oli_lunchy.users.fk_Base_Office</code>.
	 */
	public void setFkBaseOffice(java.lang.Integer value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>oli_lunchy.users.fk_Base_Office</code>.
	 */
	public java.lang.Integer getFkBaseOffice() {
		return (java.lang.Integer) getValue(12);
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
	// Record13 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row13<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.String, java.sql.Timestamp, java.lang.String, java.sql.Timestamp, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row13) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row13<java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer, java.lang.String, java.sql.Timestamp, java.lang.String, java.sql.Timestamp, java.lang.Integer> valuesRow() {
		return (org.jooq.Row13) super.valuesRow();
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
	public org.jooq.Field<java.lang.Integer> field2() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.FK_COMMUNITY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.EMAIL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.PASSWORD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.DISPLAYNAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.CREATED_ON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field7() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.LAST_LOGIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field8() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.PERMISSIONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.PASSWORD_RESET_TOKEN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field10() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.PASSWORD_RESET_TIMESTAMP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field11() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.LONG_TIME_TOKEN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field12() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.LONG_TIME_TIMESTAMP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field13() {
		return de.oglimmer.lunchy.database.generated.tables.Users.USERS.FK_BASE_OFFICE;
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
	public java.lang.Integer value2() {
		return getFkCommunity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getEmail();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getPassword();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getDisplayname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getCreatedOn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value7() {
		return getLastLogin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value8() {
		return getPermissions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getPasswordResetToken();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value10() {
		return getPasswordResetTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value11() {
		return getLongTimeToken();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value12() {
		return getLongTimeTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value13() {
		return getFkBaseOffice();
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
	public UsersRecord value2(java.lang.Integer value) {
		setFkCommunity(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value3(java.lang.String value) {
		setEmail(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value4(java.lang.String value) {
		setPassword(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value5(java.lang.String value) {
		setDisplayname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value6(java.sql.Timestamp value) {
		setCreatedOn(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value7(java.sql.Timestamp value) {
		setLastLogin(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value8(java.lang.Integer value) {
		setPermissions(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value9(java.lang.String value) {
		setPasswordResetToken(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value10(java.sql.Timestamp value) {
		setPasswordResetTimestamp(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value11(java.lang.String value) {
		setLongTimeToken(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value12(java.sql.Timestamp value) {
		setLongTimeTimestamp(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord value13(java.lang.Integer value) {
		setFkBaseOffice(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRecord values(java.lang.Integer value1, java.lang.Integer value2, java.lang.String value3, java.lang.String value4, java.lang.String value5, java.sql.Timestamp value6, java.sql.Timestamp value7, java.lang.Integer value8, java.lang.String value9, java.sql.Timestamp value10, java.lang.String value11, java.sql.Timestamp value12, java.lang.Integer value13) {
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
	public UsersRecord(java.lang.Integer id, java.lang.Integer fkCommunity, java.lang.String email, java.lang.String password, java.lang.String displayname, java.sql.Timestamp createdOn, java.sql.Timestamp lastLogin, java.lang.Integer permissions, java.lang.String passwordResetToken, java.sql.Timestamp passwordResetTimestamp, java.lang.String longTimeToken, java.sql.Timestamp longTimeTimestamp, java.lang.Integer fkBaseOffice) {
		super(de.oglimmer.lunchy.database.generated.tables.Users.USERS);

		setValue(0, id);
		setValue(1, fkCommunity);
		setValue(2, email);
		setValue(3, password);
		setValue(4, displayname);
		setValue(5, createdOn);
		setValue(6, lastLogin);
		setValue(7, permissions);
		setValue(8, passwordResetToken);
		setValue(9, passwordResetTimestamp);
		setValue(10, longTimeToken);
		setValue(11, longTimeTimestamp);
		setValue(12, fkBaseOffice);
	}
}
