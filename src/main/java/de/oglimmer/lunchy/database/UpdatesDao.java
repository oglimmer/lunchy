package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;

public enum UpdatesDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public List<ResultParam> get(int numberOfItems) {
		List<ResultParam> list = new ArrayList<>(numberOfItems);
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = create
					.fetch("select concat(if (createdOn=lastUpdate , 'New location ' , 'Updated location '),officialName,' in ', city), concat('view/',id), lastUpdate from location "
							+ "union "
							+ "select concat(if (reviews.createdOn=reviews.lastUpdate , 'New review ' , 'Updated review '),officialName,' in ', city,' from ',displayname), concat('view/',location.id), reviews.lastUpdate from reviews join location on location.id=reviews.fklocation join users on reviews.fkuser=users.id "
							+ "union "
							+ "select concat('New user ',displayname), '', createdOn from users "
							+ "order by 3 desc " + "limit " + numberOfItems);

			for (Record rec : result) {
				list.add(new ResultParam(rec.getValue(0, String.class), rec.getValue(1, String.class)));
			}
		}
		return list;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ResultParam {

		private String text;
		private String ref;

	}

}
