package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.Communities.COMMUNITIES;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;

public enum CommunityDao {
	INSTANCE;

	private LoadingCache<String, CommunitiesRecord> userRecordCache = CacheBuilder.newBuilder()
			.expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, CommunitiesRecord>() {
				public CommunitiesRecord load(String domain) {
					return DB.fetchOn(COMMUNITIES, COMMUNITIES.DOMAIN.equalIgnoreCase(domain));
				}
			});

	public CommunitiesRecord getByDomainNoCache(String domain) {
		return DB.fetchOn(COMMUNITIES, COMMUNITIES.DOMAIN.equalIgnoreCase(domain));
	}

	public CommunitiesRecord getByDomain(String domain) {
		try {
			return userRecordCache.get(domain);
		} catch (ExecutionException e) {
			return getByDomainNoCache(domain);
		}
	}

	public CommunitiesRecord getById(int id) {
		return DB.fetchOn(COMMUNITIES, COMMUNITIES.ID.equal(id));
	}

	public void store(CommunitiesRecord community) {
		DB.store(community);
	}

}
