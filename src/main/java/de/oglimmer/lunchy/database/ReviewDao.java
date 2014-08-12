package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
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
			ReviewsRecord rec = create.fetchOne(Reviews.REVIEWS, Reviews.REVIEWS.ID.equal(id));
			rec.attach(null);
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public List<ReviewsRecord> getList(int fklocation) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Result<Record> result = create.select().from(Reviews.REVIEWS).where(Reviews.REVIEWS.FKLOCATION.equal(fklocation))
					.orderBy(Reviews.REVIEWS.LASTUPDATE.desc()).fetch();

			List<ReviewsRecord> resultList = new ArrayList<>();
			for (Record rawRec : result) {
				ReviewsRecord rec = (ReviewsRecord) rawRec;
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

			ReviewsRecord rec = create.fetchOne(Reviews.REVIEWS, Reviews.REVIEWS.ID.equal(id));
			rec.delete();
		}
	}

	@SneakyThrows(value = SQLException.class)
	public Integer hasUserReview(Integer fklocation, Integer fkUser) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			Record1<Integer> result = create.select(Reviews.REVIEWS.ID).from(Reviews.REVIEWS)
					.where(Reviews.REVIEWS.FKLOCATION.equal(fklocation).and(Reviews.REVIEWS.FKUSER.equal(fkUser))).fetchOne();

			return result != null ? result.value1() : null;
		}
	}

}
