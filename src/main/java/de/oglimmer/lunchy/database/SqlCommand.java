package de.oglimmer.lunchy.database;

import lombok.Value;

@Value
public class SqlCommand {

	public static final SqlCommand[] EMPTY = new SqlCommand[0];

	private String sql;
	private Object params;

}
