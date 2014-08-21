package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;
import static de.oglimmer.lunchy.database.generated.tables.Offices.OFFICES;

import java.util.List;

import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;

public enum OfficeDao {
	INSTANCE;

	public OfficesRecord getById(int id) {
		return DB.fetchOn(OFFICES, OFFICES.ID.equal(id));
	}

	public int getDefaultOffice(int fkCommunity) {
		Integer fkBaseOffice = DB.getInt(
				"select fk_Base_Office from users where fk_Community=? group by fk_Base_Office order by count(*) desc limit 1", fkCommunity);
		return fkBaseOffice != null ? fkBaseOffice : -1;
	}

	public List<OfficesRecord> query(int fkCommunity) {
		return DB.query(OFFICES, OFFICES.FK_COMMUNITY.equal(fkCommunity), OFFICES.NAME.asc(), OfficesRecord.class);
	}

}
