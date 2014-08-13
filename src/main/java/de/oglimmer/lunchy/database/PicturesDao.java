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
import de.oglimmer.lunchy.database.generated.tables.Pictures;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;

public enum PicturesDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public PicturesRecord getById(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			PicturesRecord rec = create.fetchOne(Pictures.PICTURES, Pictures.PICTURES.ID.equal(id));
			rec.attach(null);
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<PicturesRecord> getList(int fklocation) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<Record> result = create.select().from(Pictures.PICTURES).where(Pictures.PICTURES.FKLOCATION.equal(fklocation))
					.orderBy(Pictures.PICTURES.CREATEDON.desc()).fetch();

			List<PicturesRecord> resultList = new ArrayList<>();
			for (Record rawRec : result) {
				PicturesRecord rec = (PicturesRecord) rawRec;
				rec.attach(null);
				resultList.add(rec);
			}

			return resultList;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void store(PicturesRecord review) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			review.attach(create.configuration());
			review.store();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void delete(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			PicturesRecord rec = create.fetchOne(Pictures.PICTURES, Pictures.PICTURES.ID.equal(id));
			rec.delete();
		}
	}

}
