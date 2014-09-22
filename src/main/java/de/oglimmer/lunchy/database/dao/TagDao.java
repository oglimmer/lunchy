package de.oglimmer.lunchy.database.dao;

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
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SelectConditionStep;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Location;

public enum TagDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public List<String> getAllTags(Integer fkOffice, int fkCommunity) {
		Set<String> set = createSet(fkOffice, fkCommunity);
		List<String> list = convertSetToSortedList(set);
		return list;
	}

	private List<String> convertSetToSortedList(Set<String> set) {
		List<String> list = new ArrayList<>(set);
		Collections.sort(list);
		return list;
	}

	private Set<String> createSet(Integer fkOffice, int fkCommunity) throws SQLException {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			return queryDatabase(fkOffice, fkCommunity, create);
		}
	}

	private Set<String> queryDatabase(Integer fkOffice, int fkCommunity, DSLContext create) {
		Set<String> resultSet = new HashSet<>();
		SelectConditionStep<Record1<String>> where = create.selectDistinct(Location.LOCATION.TAGS).from(Location.LOCATION)
				.where(Location.LOCATION.FK_COMMUNITY.equal(fkCommunity).and(Location.LOCATION.TAGS.notEqual("")));
		if (fkOffice != null) {
			where = where.and(Location.LOCATION.FK_OFFICE.equal(fkOffice));
		}
		Result<Record1<String>> result = where.fetch();
		for (Record1<String> rec : result) {
			processRec(resultSet, rec);
		}
		return resultSet;
	}

	private void processRec(Set<String> resultSet, Record1<String> rec) {
		String tags = rec.getValue(Location.LOCATION.TAGS);
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
