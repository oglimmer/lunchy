package de.oglimmer.lunchy.rest.resources;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.oglimmer.lunchy.database.dao.UsageDao;
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

@Path("usage")
public class UsageResource {

	@POST
	@Path("{action}/{context}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void register(@Context HttpServletRequest request, @PathParam("action") String action,
			@PathParam("context") String context, LongTimeToken longTimeToken) {
		
		UsageStatisticsRecord usage = new UsageStatisticsRecord();
		usage.setAction(action);
		usage.setContext(context);
		usage.setIp(getRemoteIP(request));
		usage.setUserAgent(request.getHeader("User-Agent"));
		usage.setCreatedOn(DateCalcService.getNow());
		usage.setUserId(getUserId(request, longTimeToken));
		Cookie ltsCookie = CookieService.INSTANCE.getLongTermSessionCookie(request);
		usage.setUserCookie(ltsCookie != null ? ltsCookie.getValue() : null);
		usage.setDomain(CommunityService.get(request));
		UsageDao.INSTANCE.store(usage);
	}

	private Integer getUserId(HttpServletRequest request, LongTimeToken longTimeToken) {
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		if (userId == null && longTimeToken != null && longTimeToken.getLongTimeToken() != null) {
			LoginResponse resp = new LoginCheck(request, longTimeToken.getLongTimeToken()).check();
			userId = resp.getUserId();
		}
		return userId;
	}

	private String getRemoteIP(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		if ("127.0.0.1".equals(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		}
		return remoteAddr;
	}

	@ToString
	public static class LongTimeToken {

		@Getter
		@Setter
		private String longTimeToken;

	}

}
