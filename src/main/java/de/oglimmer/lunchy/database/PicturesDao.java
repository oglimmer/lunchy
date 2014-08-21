package de.oglimmer.lunchy.database;

import static de.oglimmer.lunchy.database.DB.DB;

import java.util.List;

import static de.oglimmer.lunchy.database.generated.tables.Pictures.PICTURES;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;

public enum PicturesDao {
	INSTANCE;

	public PicturesRecord getById(int id) {
		return DB.fetchOn(PICTURES, PICTURES.ID.equal(id));
	}

	public List<PicturesRecord> getList(int fklocation) {
		return DB.query(PICTURES, PICTURES.FK_LOCATION.equal(fklocation), PICTURES.CREATED_ON.desc(), PicturesRecord.class);
	}

	public void store(PicturesRecord review) {
		DB.store(review);
	}

	public void delete(int id) {
		DB.delete(PICTURES, PICTURES.ID, id);
	}

}
