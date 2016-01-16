package de.oglimmer.lunchy.database.dao;

import static de.oglimmer.lunchy.database.dao.DaoBackend.DB;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import de.oglimmer.lunchy.database.generated.tables.records.UsageStatisticsRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.rest.resources.helper.LoginCheck;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.CookieService;
import de.oglimmer.lunchy.services.DateCalcService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public enum UsageDao {
	INSTANCE;

	public void store(String action, String context, HttpServletRequest request, LongTimeToken longTimeToken) {
		UsageStatisticsRecord usage = new UsageStatisticsRecord();
		usage.setAction(action);
		usage.setContext(context);
		usage.setIp(getRemoteIP(request));
		usage.setUserAgent(request.getHeader("User-Agent"));
		usage.setCreatedOn(DateCalcService.getNow());
		Cookie ltsCookie = CookieService.INSTANCE.getLongTermSessionCookie(request);
		usage.setUserCookie(ltsCookie != null ? ltsCookie.getValue() : null);
		usage.setDomain(getDomain(request));
		usage.setUserId(getUserId(request, longTimeToken));
		DB.store(usage);
	}

	private int getDomain(HttpServletRequest request) {
		if (request.getAttribute("community") != null) {
			return CommunityService.get(request);
		} else {
			return -1;
		}
	}

	/**
	 * Logic: Always use HTTP_X_FORWARDED_FOR if provided. Otherwise use the IP from the request if this is not
	 * 127.0.0.1 (=behind a local reverse proxy) then use X-Forwarded-For (default for haproxy)
	 * 
	 * @param request
	 * @return
	 */
	private String getRemoteIP(HttpServletRequest request) {
		final String remoteAddr;
		String httpXForwardedFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (httpXForwardedFor != null) {
			remoteAddr = httpXForwardedFor;
		} else {
			String ipFromRequest = request.getRemoteAddr();
			if ("127.0.0.1".equals(ipFromRequest)) {
				remoteAddr = request.getHeader("X-Forwarded-For");
			} else {
				remoteAddr = ipFromRequest;
			}
		}
		return remoteAddr;
	}

	private Integer getUserId(HttpServletRequest request, LongTimeToken longTimeToken) {
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		if (userId == null && longTimeToken != null && longTimeToken.getLongTimeToken() != null) {
			LoginResponse resp = new LoginCheck(request, longTimeToken.getLongTimeToken()).check();
			userId = resp.getUserId();
		}
		return userId;
	}

	@ToString
	public static class LongTimeToken {

		@Getter
		@Setter
		private String longTimeToken;

	}

}
