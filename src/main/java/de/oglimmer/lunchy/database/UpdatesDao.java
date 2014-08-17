package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
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
	public List<ResultParam> get(int numberOfItems, int fkCommunity) {
		List<ResultParam> list = new ArrayList<>(numberOfItems);
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = create
					.fetch("select 'L' as type, officialName, city, '' as user, if (createdOn=lastUpdate , 'N' , 'U') as updatetype, id, lastUpdate from location where fkCommunity=? "
							+ "union "
							+ "select 'R' as type, officialName, city, displayname as user, if (reviews.createdOn=reviews.lastUpdate , 'N' , 'U') as updatetype, location.id, reviews.lastUpdate from reviews join location on location.id=reviews.fklocation join users on reviews.fkuser=users.id where location.fkCommunity=? "
							+ "union "
							+ "select 'U' as type, '' as officialName, '' as city, displayname as user,'N' as updatetype, id, createdOn as lastUpdate from users where users.fkCommunity=? "
							+ "union "
							+ "select 'P' as type, officialName, city, displayname as user, 'N' as updatetype, location.id, pictures.createdOn from pictures join location on location.id=pictures.fklocation join users on pictures.fkuser=users.id where location.fkCommunity=? "
							+ "order by lastUpdate desc " + "limit " + numberOfItems, fkCommunity, fkCommunity, fkCommunity, fkCommunity);

			for (Record rec : result) {
				list.add(new ResultParam(rec));
			}
		}
		return list;
	}

	@Data
	public static class ResultParam {

		private String updatetype;
		private String user;
		private String type;
		private String officialName;
		private String city;
		private Integer id;

		private ResultParam(Record rec) {
			id = rec.getValue("id", Integer.class);
			type = rec.getValue("type", String.class);
			updatetype = rec.getValue("updatetype", String.class);
			user = rec.getValue("user", String.class);
			officialName = rec.getValue("officialName", String.class);
			city = rec.getValue("city", String.class);
		}
	}

}
