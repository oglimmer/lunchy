package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Record;
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
			Result<? extends Record> result = create.select()
					.from(Location.LOCATION)
					.where(Location.LOCATION.ID.equal(new Integer(id))).fetch();

			for (LocationRecord rec : (Result<LocationRecord>) result) {
				rec.attach(null);
				return rec;
			}
		}
		return null;
	}

	@SneakyThrows(value = SQLException.class)
	public void store(LocationRecord location) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			location.attach(create.configuration());
			location.store();
		}
	}

}
