package de.oglimmer.lunchy.services;

public enum RegExService {
	INSTANCE;

	public String escape(String s) {
		// http://stackoverflow.com/questions/14134558/java-regex-list-of-all-special-characters-that-needs-to-be-escaped-in-regex
		return s.toLowerCase().replaceAll("([.?*+^$\\[\\]\\\\(\\){}|-])", "\\\\$1");
	}

}
