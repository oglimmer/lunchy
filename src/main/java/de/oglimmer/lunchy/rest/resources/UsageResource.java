package de.oglimmer.lunchy.rest.resources;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import de.oglimmer.lunchy.database.dao.UsageDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsageStatisticsRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.services.CookieService;
import de.oglimmer.lunchy.services.DateCalcService;

@Path("usage")
public class UsageResource {

	@POST
	@Path("{action}/{context}")
	public void register(@Context HttpServletRequest request, @PathParam("action") String action,
			@PathParam("context") String context) {
		UsageStatisticsRecord usage = new UsageStatisticsRecord();
		usage.setAction(action);
		usage.setContext(context);
		usage.setIp(getRemoteIP(request));
		usage.setUserAgent(request.getHeader("User-Agent"));
		usage.setCreatedOn(DateCalcService.getNow());
		usage.setUserId(SessionProvider.INSTANCE.getLoggedInUserId(request));
		Cookie ltsCookie = CookieService.INSTANCE.getLongTermSessionCookie(request);
		usage.setUserCookie(ltsCookie != null ? ltsCookie.getValue() : null);
		UsageDao.INSTANCE.store(usage);
	}

	private String getRemoteIP(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		if ("127.0.0.1".equals(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		}
		return remoteAddr;
	}

}
