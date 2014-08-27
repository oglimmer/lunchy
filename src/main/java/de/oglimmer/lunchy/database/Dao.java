package de.oglimmer.lunchy.database;

import java.util.List;

import org.jooq.Record;

public interface Dao<T extends Record> {

	T getById(Integer id, Integer fkCommunity);

	List<?> getListByParent(int fkParent);

}
