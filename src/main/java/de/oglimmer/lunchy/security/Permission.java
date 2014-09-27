package de.oglimmer.lunchy.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Permission {
	USER(0), CONFIRMED_USER(1), ADMIN(2);
	@Getter
	private int val;

	public static Permission fromVal(int val) {
		for (Permission per : values()) {
			if (per.getVal() == val) {
				return per;
			}
		}
		return null;
	}
}