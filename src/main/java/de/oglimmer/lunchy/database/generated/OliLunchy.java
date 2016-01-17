/**
 * This class is generated by jOOQ
 */
package de.oglimmer.lunchy.database.generated;


import de.oglimmer.lunchy.database.generated.tables.Communities;
import de.oglimmer.lunchy.database.generated.tables.Databasechangelog;
import de.oglimmer.lunchy.database.generated.tables.Databasechangeloglock;
import de.oglimmer.lunchy.database.generated.tables.Location;
import de.oglimmer.lunchy.database.generated.tables.Offices;
import de.oglimmer.lunchy.database.generated.tables.Pictures;
import de.oglimmer.lunchy.database.generated.tables.Reviews;
import de.oglimmer.lunchy.database.generated.tables.UsageStatistics;
import de.oglimmer.lunchy.database.generated.tables.Users;
import de.oglimmer.lunchy.database.generated.tables.UsersPicturesVotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OliLunchy extends SchemaImpl {

	private static final long serialVersionUID = -2043436604;

	/**
	 * The reference instance of <code>oli_lunchy</code>
	 */
	public static final OliLunchy OLI_LUNCHY = new OliLunchy();

	/**
	 * No further instances allowed
	 */
	private OliLunchy() {
		super("oli_lunchy");
	}

	@Override
	public final List<Table<?>> getTables() {
		List result = new ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final List<Table<?>> getTables0() {
		return Arrays.<Table<?>>asList(
			Communities.COMMUNITIES,
			Databasechangelog.DATABASECHANGELOG,
			Databasechangeloglock.DATABASECHANGELOGLOCK,
			Location.LOCATION,
			Offices.OFFICES,
			Pictures.PICTURES,
			Reviews.REVIEWS,
			UsageStatistics.USAGE_STATISTICS,
			Users.USERS,
			UsersPicturesVotes.USERS_PICTURES_VOTES);
	}
}
