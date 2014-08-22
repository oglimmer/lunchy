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

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.beanMapping.DozerAdapter;
import de.oglimmer.lunchy.beanMapping.RestDto;
import de.oglimmer.lunchy.database.connection.DBConn;

public enum UpdatesDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public List<ResultParam> get(int numberOfItems, int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = queryDB(numberOfItems, fkCommunity, create);
			return createResultList(result);
		}
	}

	private Result<Record> queryDB(int numberOfItems, int fkCommunity, DSLContext create) {
		String sql = "select 'L' as type, official_Name, city, '' as user, if (created_On=last_Update , 'N' , 'U') as update_Type, id, last_Update from location where fk_Community=? "
				+ "union "
				+ "select 'R' as type, official_Name, city, displayname as user, if (reviews.created_On=reviews.last_Update , 'N' , 'U') as update_Type, location.id, reviews.last_Update from reviews join location on location.id=reviews.fk_location join users on reviews.fk_user=users.id where location.fk_Community=? "
				+ "union "
				+ "select 'U' as type, '' as official_Name, '' as city, displayname as user,'N' as update_Type, id, created_On as last_Update from users where users.fk_Community=? "
				+ "union "
				+ "select 'P' as type, official_Name, city, displayname as user, 'N' as update_Type, location.id, pictures.created_On as last_Update from pictures join location on location.id=pictures.fk_location join users on pictures.fk_user=users.id where location.fk_Community=? "
				+ "order by last_Update desc limit " + numberOfItems;
		return create.fetch(sql, fkCommunity, fkCommunity, fkCommunity, fkCommunity);
	}

	private List<ResultParam> createResultList(Result<Record> result) {
		List<ResultParam> list = new ArrayList<>(result.size());
		for (Record rec : result) {
			ResultParam rp = BeanMappingProvider.INSTANCE.map(new DozerAdapter(rec), ResultParam.class);
			list.add(rp);
		}
		return list;
	}

	@Data
	@RestDto
	public static class ResultParam {
		private String updateType;
		private String user;
		private String type;
		private String officialName;
		private String city;
		private Integer id;
	}

}
