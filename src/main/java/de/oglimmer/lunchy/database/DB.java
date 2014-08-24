package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;

enum DB {
	DB;

	public static DSLContext getContext(Connection conn) {
		Settings settings = new Settings().withRenderSchema(false).withRenderNameStyle(RenderNameStyle.QUOTED);
		return DSL.using(conn, DBConn.INSTANCE.getSqlDialect(), settings);
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends Record> R fetchOn(Table<R> table, Condition condition) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = getContext(conn);
			R rec = create.fetchOne(table, condition);
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

	public <R extends UpdatableRecord<R>> void store(UpdatableRecord<R> record) {
		store(record, new SqlExecCallback[0]);
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends UpdatableRecord<R>> void store(UpdatableRecord<R> record, SqlExecCallback... sqlCommands) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = getContext(conn);
			record.attach(create.configuration());
			record.store();
			for (SqlExecCallback sqlCmd : sqlCommands) {
				sqlCmd.exec(create);
			}
			record.attach(null);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends Record> void delete(Table<R> table, Field<Integer> field, int id, int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = getContext(conn);
			create.delete(table).where(field.equal(id).and("fk_Community=?", fkCommunity)).execute();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public int getInt(String sql, Object... params) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = getContext(conn);
			Record rec = create.fetchOne(sql, params);
			return rec.getValue(0, Integer.class);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends Record> List<R> query(Table<R> table, Condition condition, SortField<?> orderedBy, Class<R> clazz) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = getContext(conn);
			Result<Record> result = create.select().from(table).where(condition).orderBy(orderedBy).fetch();
			List<R> resultList = new ArrayList<>();
			for (Record rec : result) {
				rec.attach(null);
				assert rec.getClass().isAssignableFrom(clazz);
				resultList.add(clazz.cast(rec));
			}
			return resultList;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<Record> query(SqlResultCallback callback) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = getContext(conn);
			Result<?> result = callback.fetch(create);
			List<Record> resultList = new ArrayList<>();
			for (Record rec : result) {
				rec.attach(null);
				resultList.add(rec);
			}
			return resultList;
		}
	}

}
