/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated.tables.records;


import de.oglimmer.lunchy.database.generated.tables.Databasechangelog;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record14;
import org.jooq.Row14;
import org.jooq.impl.TableRecordImpl;


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
public class DatabasechangelogRecord extends TableRecordImpl<DatabasechangelogRecord> implements Record14<String, String, String, Timestamp, Integer, String, String, String, String, String, String, String, String, String> {

	private static final long serialVersionUID = 937866405;

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.ID</code>.
	 */
	public void setId(String value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.ID</code>.
	 */
	public String getId() {
		return (String) getValue(0);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.AUTHOR</code>.
	 */
	public void setAuthor(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.AUTHOR</code>.
	 */
	public String getAuthor() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.FILENAME</code>.
	 */
	public void setFilename(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.FILENAME</code>.
	 */
	public String getFilename() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.DATEEXECUTED</code>.
	 */
	public void setDateexecuted(Timestamp value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.DATEEXECUTED</code>.
	 */
	public Timestamp getDateexecuted() {
		return (Timestamp) getValue(3);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.ORDEREXECUTED</code>.
	 */
	public void setOrderexecuted(Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.ORDEREXECUTED</code>.
	 */
	public Integer getOrderexecuted() {
		return (Integer) getValue(4);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.EXECTYPE</code>.
	 */
	public void setExectype(String value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.EXECTYPE</code>.
	 */
	public String getExectype() {
		return (String) getValue(5);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.MD5SUM</code>.
	 */
	public void setMd5sum(String value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.MD5SUM</code>.
	 */
	public String getMd5sum() {
		return (String) getValue(6);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.DESCRIPTION</code>.
	 */
	public void setDescription(String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.DESCRIPTION</code>.
	 */
	public String getDescription() {
		return (String) getValue(7);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.COMMENTS</code>.
	 */
	public void setComments(String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.COMMENTS</code>.
	 */
	public String getComments() {
		return (String) getValue(8);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.TAG</code>.
	 */
	public void setTag(String value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.TAG</code>.
	 */
	public String getTag() {
		return (String) getValue(9);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.LIQUIBASE</code>.
	 */
	public void setLiquibase(String value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.LIQUIBASE</code>.
	 */
	public String getLiquibase() {
		return (String) getValue(10);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.CONTEXTS</code>.
	 */
	public void setContexts(String value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.CONTEXTS</code>.
	 */
	public String getContexts() {
		return (String) getValue(11);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.LABELS</code>.
	 */
	public void setLabels(String value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.LABELS</code>.
	 */
	public String getLabels() {
		return (String) getValue(12);
	}

	/**
	 * Setter for <code>oli_lunchy.DATABASECHANGELOG.DEPLOYMENT_ID</code>.
	 */
	public void setDeploymentId(String value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>oli_lunchy.DATABASECHANGELOG.DEPLOYMENT_ID</code>.
	 */
	public String getDeploymentId() {
		return (String) getValue(13);
	}

	// -------------------------------------------------------------------------
	// Record14 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row14<String, String, String, Timestamp, Integer, String, String, String, String, String, String, String, String, String> fieldsRow() {
		return (Row14) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row14<String, String, String, Timestamp, Integer, String, String, String, String, String, String, String, String, String> valuesRow() {
		return (Row14) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field1() {
		return Databasechangelog.DATABASECHANGELOG.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Databasechangelog.DATABASECHANGELOG.AUTHOR;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return Databasechangelog.DATABASECHANGELOG.FILENAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field4() {
		return Databasechangelog.DATABASECHANGELOG.DATEEXECUTED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field5() {
		return Databasechangelog.DATABASECHANGELOG.ORDEREXECUTED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field6() {
		return Databasechangelog.DATABASECHANGELOG.EXECTYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field7() {
		return Databasechangelog.DATABASECHANGELOG.MD5SUM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field8() {
		return Databasechangelog.DATABASECHANGELOG.DESCRIPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field9() {
		return Databasechangelog.DATABASECHANGELOG.COMMENTS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field10() {
		return Databasechangelog.DATABASECHANGELOG.TAG;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field11() {
		return Databasechangelog.DATABASECHANGELOG.LIQUIBASE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field12() {
		return Databasechangelog.DATABASECHANGELOG.CONTEXTS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field13() {
		return Databasechangelog.DATABASECHANGELOG.LABELS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field14() {
		return Databasechangelog.DATABASECHANGELOG.DEPLOYMENT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getAuthor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getFilename();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value4() {
		return getDateexecuted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value5() {
		return getOrderexecuted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value6() {
		return getExectype();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value7() {
		return getMd5sum();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value8() {
		return getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value9() {
		return getComments();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value10() {
		return getTag();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value11() {
		return getLiquibase();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value12() {
		return getContexts();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value13() {
		return getLabels();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value14() {
		return getDeploymentId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value1(String value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value2(String value) {
		setAuthor(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value3(String value) {
		setFilename(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value4(Timestamp value) {
		setDateexecuted(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value5(Integer value) {
		setOrderexecuted(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value6(String value) {
		setExectype(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value7(String value) {
		setMd5sum(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value8(String value) {
		setDescription(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value9(String value) {
		setComments(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value10(String value) {
		setTag(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value11(String value) {
		setLiquibase(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value12(String value) {
		setContexts(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value13(String value) {
		setLabels(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord value14(String value) {
		setDeploymentId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DatabasechangelogRecord values(String value1, String value2, String value3, Timestamp value4, Integer value5, String value6, String value7, String value8, String value9, String value10, String value11, String value12, String value13, String value14) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		value8(value8);
		value9(value9);
		value10(value10);
		value11(value11);
		value12(value12);
		value13(value13);
		value14(value14);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached DatabasechangelogRecord
	 */
	public DatabasechangelogRecord() {
		super(Databasechangelog.DATABASECHANGELOG);
	}

	/**
	 * Create a detached, initialised DatabasechangelogRecord
	 */
	public DatabasechangelogRecord(String id, String author, String filename, Timestamp dateexecuted, Integer orderexecuted, String exectype, String md5sum, String description, String comments, String tag, String liquibase, String contexts, String labels, String deploymentId) {
		super(Databasechangelog.DATABASECHANGELOG);

		setValue(0, id);
		setValue(1, author);
		setValue(2, filename);
		setValue(3, dateexecuted);
		setValue(4, orderexecuted);
		setValue(5, exectype);
		setValue(6, md5sum);
		setValue(7, description);
		setValue(8, comments);
		setValue(9, tag);
		setValue(10, liquibase);
		setValue(11, contexts);
		setValue(12, labels);
		setValue(13, deploymentId);
	}
}
