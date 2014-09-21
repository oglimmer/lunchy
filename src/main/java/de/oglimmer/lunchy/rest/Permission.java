package de.oglimmer.lunchy.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

public @AllArgsConstructor
enum Permission {
	USER(0), CONFIRMED_USER(1), ADMIN(2);
	@Getter
	private int val;

	static Permission fromVal(int val) {
		for (Permission per : values()) {
			if (per.getVal() == val) {
				return per;
			}
		}
		return null;
	}
}