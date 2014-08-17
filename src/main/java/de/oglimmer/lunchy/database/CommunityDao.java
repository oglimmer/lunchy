package de.oglimmer.lunchy.database;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Communities;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;

public enum CommunityDao {
	INSTANCE;

	@SneakyThrows(value = SQLException.class)
	public CommunitiesRecord getByDomain(String domain) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			CommunitiesRecord rec = create.fetchOne(Communities.COMMUNITIES, Communities.COMMUNITIES.DOMAIN.equalIgnoreCase(domain));
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

	@SneakyThrows(value = SQLException.class)
	public CommunitiesRecord getById(int id) {
		try (Connection conn = DBConn.INSTANCE.get()) {

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			CommunitiesRecord rec = create.fetchOne(Communities.COMMUNITIES, Communities.COMMUNITIES.ID.equal(id));
			if (rec != null) {
				rec.attach(null);
			}
			return rec;
		}
	}

}
