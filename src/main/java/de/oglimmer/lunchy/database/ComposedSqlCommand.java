package de.oglimmer.lunchy.database;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComposedSqlCommand extends SqlCommand {

	private List<String> subCommands = new ArrayList<>();

	public ComposedSqlCommand(String sql, Object... params) {
		super(sql, params);
	}

	public void add(SqlCommand subCommand) {
		assert !(subCommand instanceof ComposedSqlCommand);
		StringBuilder buff = new StringBuilder();
		// to detect a ? at the first or last position => add a space
		String sql = " " + subCommand.getSql() + " ";
		String[] parts = sql.split("\\?");
		if (parts.length > 1) {
			assert parts.length - 1 == subCommand.getParams().length;
			for (int i = 0; i < parts.length - 1; i++) {
				buff.append(parts[i]);
				addParameter(subCommand.getParams()[i], buff);
			}
		}
		buff.append(parts[parts.length - 1]);
		subCommands.add(buff.toString());
	}

	private void addParameter(Object obj, StringBuilder buff) {
		assert !(obj instanceof Date);
		if (obj instanceof String) {
			buff.append("'");
			buff.append(obj);
			buff.append("'");
		} else {
			buff.append(obj);
		}
	}

	@Override
	public String getSql() {
		List<String> subSql = new ArrayList<>();
		for (String subCmd : subCommands) {
			subSql.add(subCmd);
		}
		return MessageFormat.format(super.getSql(), subSql.toArray());
	}

}
