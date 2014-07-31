package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.records.LocationRecord;

public enum LocationDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public LocationRecord getById(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			LocationRecord rec = create.fetchOne(Location.LOCATION,
					Location.LOCATION.ID.equal(id));
			rec.attach(null);
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<LocationRecord> getList() {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<? extends LocationRecord> result = (Result<? extends LocationRecord>) create
					.select().from(Location.LOCATION).fetch();

			List<LocationRecord> resultList = new ArrayList<LocationRecord>();
			for (LocationRecord rec : result) {
				rec.attach(null);
				resultList.add(rec);
			}

			return resultList;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void store(LocationRecord location) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			location.attach(create.configuration());
			location.store();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void delete(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			LocationRecord rec = create.fetchOne(Location.LOCATION,
					Location.LOCATION.ID.equal(id));
			rec.delete();
		}
	}

}
