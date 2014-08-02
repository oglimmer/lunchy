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
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.records.ReviewsRecord;

public enum ReviewDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public ReviewsRecord getById(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			ReviewsRecord rec = create.fetchOne(Reviews.REVIEWS,
					Reviews.REVIEWS.ID.equal(id));
			rec.attach(null);
			return rec;
		}
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows(value = SQLException.class)
	public List<ReviewsRecord> getList() {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<? extends ReviewsRecord> result = (Result<? extends ReviewsRecord>) create
					.select().from(Reviews.REVIEWS).fetch();

			List<ReviewsRecord> resultList = new ArrayList<>();
			for (ReviewsRecord rec : result) {
				rec.attach(null);
				resultList.add(rec);
			}

			return resultList;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public void store(ReviewsRecord review) {
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

			ReviewsRecord rec = create.fetchOne(Reviews.REVIEWS,
					Reviews.REVIEWS.ID.equal(id));
			rec.delete();
		}
	}

}
