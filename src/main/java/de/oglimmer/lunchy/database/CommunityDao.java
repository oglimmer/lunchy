package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;
import static de.oglimmer.lunchy.database.generated.tables.Communities.COMMUNITIES;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;

public enum CommunityDao {
	INSTANCE;

	public CommunitiesRecord getByDomain(String domain) {
		return DB.fetchOn(COMMUNITIES, COMMUNITIES.DOMAIN.equalIgnoreCase(domain));
	}

	public CommunitiesRecord getById(int id) {
		return DB.fetchOn(COMMUNITIES, COMMUNITIES.ID.equal(id));
	}

}
