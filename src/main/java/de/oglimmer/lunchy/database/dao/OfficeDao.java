package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import lombok.SneakyThrows;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.connection.DBConn;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;

public enum OfficeDao implements Dao<OfficesRecord> {
	INSTANCE;

	@Override
	public OfficesRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(OFFICES, OFFICES.ID.equal(id).and(OFFICES.FK_COMMUNITY.equal(fkCommunity)));
	}

	@SneakyThrows(value = SQLException.class)
	public int getDefaultOffice(int fkCommunity) {
		try (Connection conn = DBConn.INSTANCE.get()) {
			DSLContext create = DaoBackend.getContext(conn);
			Record1<Integer> result = create.select(Users.USERS.FK_BASE_OFFICE).from(Users.USERS)
					.where(Users.USERS.FK_COMMUNITY.equal(fkCommunity)).groupBy(Users.USERS.FK_BASE_OFFICE).orderBy(DSL.val("count(*)"))
					.limit(1).fetchOne();
			return result != null ? result.value1() : -1;
		}
	}

	public void store(OfficesRecord rec) {
		DB.store(rec);
	}

	@Override
	public List<?> getListByParent(int fkCommunity) {
		return DB.query(OFFICES, OFFICES.FK_COMMUNITY.equal(fkCommunity), OFFICES.NAME.asc(), OfficesRecord.class);
	}

}
