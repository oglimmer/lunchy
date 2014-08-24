package de.oglimmer.lunchy.database;

import org.jooq.DSLContext;

public interface SqlExecCallback {

	void exec(DSLContext context);

}