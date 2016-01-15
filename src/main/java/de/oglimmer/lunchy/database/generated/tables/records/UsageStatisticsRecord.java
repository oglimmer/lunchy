/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables.records;


import de.oglimmer.lunchy.database.generated.tables.UsageStatistics;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsageStatisticsRecord extends UpdatableRecordImpl<UsageStatisticsRecord> implements Record9<Integer, String, String, Timestamp, String, String, Integer, String, Integer> {

	private static final long serialVersionUID = -206163925;

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.id</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.id</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.action</code>.
	 */
	public void setAction(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.action</code>.
	 */
	public String getAction() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.context</code>.
	 */
	public void setContext(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.context</code>.
	 */
	public String getContext() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.created_On</code>.
	 */
	public void setCreatedOn(Timestamp value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.created_On</code>.
	 */
	public Timestamp getCreatedOn() {
		return (Timestamp) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.ip</code>.
	 */
	public void setIp(String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.ip</code>.
	 */
	public String getIp() {
		return (String) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.user-agent</code>.
	 */
	public void setUserAgent(String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.user-agent</code>.
	 */
	public String getUserAgent() {
		return (String) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.user-id</code>.
	 */
	public void setUserId(Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.user-id</code>.
	 */
	public Integer getUserId() {
		return (Integer) getValue(6);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.user-cookie</code>.
	 */
	public void setUserCookie(String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.user-cookie</code>.
	 */
	public String getUserCookie() {
		return (String) getValue(7);
	}

	/**
	 * Setter for <code>oli_lunchy.usage_statistics.domain</code>.
	 */
	public void setDomain(Integer value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>oli_lunchy.usage_statistics.domain</code>.
	 */
	public Integer getDomain() {
		return (Integer) getValue(8);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record9 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row9<Integer, String, String, Timestamp, String, String, Integer, String, Integer> fieldsRow() {
		return (Row9) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row9<Integer, String, String, Timestamp, String, String, Integer, String, Integer> valuesRow() {
		return (Row9) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return UsageStatistics.USAGE_STATISTICS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return UsageStatistics.USAGE_STATISTICS.ACTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return UsageStatistics.USAGE_STATISTICS.CONTEXT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field4() {
		return UsageStatistics.USAGE_STATISTICS.CREATED_ON;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field5() {
		return UsageStatistics.USAGE_STATISTICS.IP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field6() {
		return UsageStatistics.USAGE_STATISTICS.USER_AGENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field7() {
		return UsageStatistics.USAGE_STATISTICS.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field8() {
		return UsageStatistics.USAGE_STATISTICS.USER_COOKIE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field9() {
		return UsageStatistics.USAGE_STATISTICS.DOMAIN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getAction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getContext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value4() {
		return getCreatedOn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value5() {
		return getIp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value6() {
		return getUserAgent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value7() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value8() {
		return getUserCookie();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value9() {
		return getDomain();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value2(String value) {
		setAction(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value3(String value) {
		setContext(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value4(Timestamp value) {
		setCreatedOn(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value5(String value) {
		setIp(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value6(String value) {
		setUserAgent(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value7(Integer value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value8(String value) {
		setUserCookie(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord value9(Integer value) {
		setDomain(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsageStatisticsRecord values(Integer value1, String value2, String value3, Timestamp value4, String value5, String value6, Integer value7, String value8, Integer value9) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		value8(value8);
		value9(value9);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UsageStatisticsRecord
	 */
	public UsageStatisticsRecord() {
		super(UsageStatistics.USAGE_STATISTICS);
	}

	/**
	 * Create a detached, initialised UsageStatisticsRecord
	 */
	public UsageStatisticsRecord(Integer id, String action, String context, Timestamp createdOn, String ip, String userAgent, Integer userId, String userCookie, Integer domain) {
		super(UsageStatistics.USAGE_STATISTICS);

		setValue(0, id);
		setValue(1, action);
		setValue(2, context);
		setValue(3, createdOn);
		setValue(4, ip);
		setValue(5, userAgent);
		setValue(6, userId);
		setValue(7, userCookie);
		setValue(8, domain);
	}
}
