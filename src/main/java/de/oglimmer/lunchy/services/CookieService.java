package de.oglimmer.lunchy.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public enum CookieService {
	INSTANCE;

	public Cookie getLongTermSessionCookie(HttpServletRequest httpReq) {
		if (httpReq != null && httpReq.getCookies() != null) {
			for (Cookie coo : httpReq.getCookies()) {
				if ("LTS".equals(coo.getName())) {
					return coo;
				}
			}
		}
		return null;
	}

}
