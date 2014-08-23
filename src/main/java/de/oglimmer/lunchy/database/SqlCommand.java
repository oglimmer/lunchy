package de.oglimmer.lunchy.database;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SqlCommand {

	public static final SqlCommand[] EMPTY = new SqlCommand[0];

	private String sql;
	private Object[] params;

	public SqlCommand(String sql, Object... params) {
		this.sql = sql;
		this.params = params;
	}

}
