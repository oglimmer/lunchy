package de.oglimmer.lunchy.database;

import org.jooq.DSLContext;
import org.jooq.Result;

public interface SqlResultCallback {

	Result<?> fetch(DSLContext context);

}