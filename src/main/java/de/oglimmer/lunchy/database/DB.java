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
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;

enum DB {
	DB;

	@SneakyThrows(value = SQLException.class)
	public <R extends Record> R fetchOn(Table<R> table, Condition condition) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			R rec = create.fetchOne(table, condition);
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

	public <R extends UpdatableRecord<R>> void store(UpdatableRecord<R> record) {
		store(record, SqlCommand.EMPTY);
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends UpdatableRecord<R>> void store(UpdatableRecord<R> record, SqlCommand... sqlCommands) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			record.attach(create.configuration());
			record.store();
			for (SqlCommand sqlCmd : sqlCommands) {
				create.execute(sqlCmd.getSql(), sqlCmd.getParams());
			}
		}
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends Record> void delete(Table<R> table, Field<Integer> field, int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			create.delete(table).where(field.equal(id)).execute();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public int getInt(String sql, Object... params) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record rec = create.fetchOne(sql, params);
			return rec.getValue(0, Integer.class);
		}
	}

	@SneakyThrows(value = SQLException.class)
	public <R extends Record> List<R> query(Table<R> table, Condition condition, SortField<?> orderedBy, Class<R> clazz) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
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
	public List<Record> query(String sql) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = create.fetch(sql);
			List<Record> resultList = new ArrayList<>();
			for (Record rec : result) {
				rec.attach(null);
				resultList.add(rec);
			}
			return resultList;
		}
	}

}
