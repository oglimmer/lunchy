package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.SneakyThrows;

import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;

public enum TagDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public List<String> getAllTags(int fkCommunity) {
		Set<String> set = new HashSet<>();
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = create.fetch("select distinct tags from location where fkCommunity=? and tags != ''", fkCommunity);
			for (Record rec : result) {
				String tags = rec.getValue("tags", String.class);
				if (tags.contains(",")) {
					for (String tag : tags.split(",")) {
						set.add(StringUtils.capitalize(tag));
					}
				} else {
					set.add(StringUtils.capitalize(tags));
				}
			}
		}
		List<String> list = new ArrayList<>(set);
		Collections.sort(list);
		return list;
	}
}
