package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;
import static de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES;

import java.util.List;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;

public enum PictureDao implements Dao<PicturesRecord> {
	INSTANCE;

	public PicturesRecord getById(Integer id, Integer fkCommunity) {
		return DB.fetchOn(PICTURES, PICTURES.ID.equal(id).and(PICTURES.FK_COMMUNITY.equal(fkCommunity)));
	}

	public void store(PicturesRecord review) {
		DB.store(review);
	}

	public void delete(int id, int fkCommunity) {
		DB.delete(PICTURES, PICTURES.ID, id, fkCommunity);
	}

	@Override
	public List<?> getListByParent(int fklocation) {
		return DB.query(PICTURES, PICTURES.FK_LOCATION.equal(fklocation), PICTURES.CREATED_ON.desc(), PicturesRecord.class);
	}

}
