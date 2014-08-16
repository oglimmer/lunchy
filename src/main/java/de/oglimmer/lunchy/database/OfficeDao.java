package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Offices;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;

public enum OfficeDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public OfficesRecord getById(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			OfficesRecord rec = create.fetchOne(Offices.OFFICES, Offices.OFFICES.ID.equal(id));
			rec.attach(null);
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<OfficesRecord> query(int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<Record> result = create.select().from(Offices.OFFICES).where(Offices.OFFICES.FKCOMMUNITY.equal(fkCommunity))
					.orderBy(Offices.OFFICES.NAME).fetch();

			List<OfficesRecord> resultList = new ArrayList<>();
			for (Record rawRec : result) {
				OfficesRecord rec = (OfficesRecord) rawRec;
				rec.attach(null);
				resultList.add(rec);
			}

			return resultList;
		}
	}

}
