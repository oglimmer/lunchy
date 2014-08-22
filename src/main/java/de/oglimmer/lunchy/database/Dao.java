package de.oglimmer.lunchy.database;

import org.jooq.Record;

public interface Dao<T extends Record> {

	T getById(Integer id, Integer fkCommunity);

}
