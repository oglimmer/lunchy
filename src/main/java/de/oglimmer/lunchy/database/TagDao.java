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
		Set<String> set = createSet(fkCommunity);
		List<String> list = convertSetToSortedList(set);
		return list;
	}

	private List<String> convertSetToSortedList(Set<String> set) {
		List<String> list = new ArrayList<>(set);
		Collections.sort(list);
		return list;
	}

	private Set<String> createSet(int fkCommunity) throws SQLException {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			return queryDatabase(fkCommunity, create);
		}
	}

	private Set<String> queryDatabase(int fkCommunity, DSLContext create) {
		Set<String> resultSet = new HashSet<>();
		Result<Record> result = create.fetch("select distinct tags from location where fk_Community=? and tags != ''", fkCommunity);
		for (Record rec : result) {
			processRec(resultSet, rec);
		}
		return resultSet;
	}

	private void processRec(Set<String> resultSet, Record rec) {
		String tags = rec.getValue("tags", String.class);
		if (hasMultipleTags(tags)) {
			processMultipleTags(resultSet, tags);
		} else {
			addSingleTag(resultSet, tags);
		}
	}

	private void processMultipleTags(Set<String> resultSet, String tags) {
		for (String tag : tags.split(",")) {
			addSingleTag(resultSet, tag);
		}
	}

	private void addSingleTag(Set<String> resultSet, String tags) {
		resultSet.add(StringUtils.capitalize(tags));
	}

	private boolean hasMultipleTags(String tags) {
		return tags.contains(",");
	}
}
