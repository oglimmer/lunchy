package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;

import de.oglimmer.lunchy.database.generated.tables.records.UsageStatisticsRecord;

public enum UsageDao {
	INSTANCE;

	public void store(UsageStatisticsRecord usage) {
		DB.store(usage);
	}

}
