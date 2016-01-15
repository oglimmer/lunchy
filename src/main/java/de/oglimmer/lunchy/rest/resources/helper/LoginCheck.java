package de.oglimmer.lunchy.rest.resources.helper;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.DateCalcService;
import lombok.Getter;

public class LoginCheck {


	private SessionProvider sessionProvider = SessionProvider.INSTANCE;

	private HttpServletRequest request;
	private String longTimeToken;

	public LoginCheck(HttpServletRequest request, String longTimeToken) {
		this.request = request;
		this.longTimeToken = longTimeToken;

	}

	public LoginResponse check() {
		SessionCheck sessionCheck = new SessionCheck();
		if (sessionCheck.isValid()) {
			return getSuccessResponse(sessionCheck.getUser());
		}
		LongTimeTokenCheck lttCheck = new LongTimeTokenCheck();
		if (lttCheck.isValid()) {
			return getSuccessResponse(lttCheck.getUser());
		}
		return getNoSuccessResponse();
	}

	private LoginResponse getNoSuccessResponse() {
		LoginResponse noSuccessResp = new LoginResponse();
		setCompanyName(noSuccessResp);
		return noSuccessResp;
	}

	public LoginResponse getSuccessResponse(UsersRecord user) {
		LoginResponse resp = sessionProvider.createSession(user, request.getSession(true), false);
		setCompanyName(resp);
		return resp;
	}

	private void setCompanyName(LoginResponse resp) {
		resp.setCompanyName(CommunityDao.INSTANCE.getById(CommunityService.get(request)).getName());
	}

	class SessionCheck {
		@Getter
		private UsersRecord user;

		public boolean isValid() {
			loadUserFromSessionAttr();
			return user != null;
		}

		private void loadUserFromSessionAttr() {
			user = sessionProvider.getLoggedInUser(request, CommunityService.get(request));
		}
	}

	class LongTimeTokenCheck {
		@Getter
		private UsersRecord user;

		public boolean isValid() {
			if (isLongTimeTokenPresent()) {
				tryToLoadUserByToken();
				if (userFound()) {
					if (isLongTimeTokenYoungerThan3Month()) {
						return true;
					} else {
						sessionProvider.removeToken(user);
					}
				}
			}
			return false;
		}

		private boolean isLongTimeTokenYoungerThan3Month() {
			return DateCalcService.youngerThan(user.getLongTimeTimestamp(), Calendar.MONTH, 3);
		}

		private boolean userFound() {
			return user != null;
		}

		private boolean isLongTimeTokenPresent() {
			return longTimeToken != null;
		}

		private void tryToLoadUserByToken() {
			user = UserDao.INSTANCE.getByLongTimeToken(longTimeToken, CommunityService.get(request));
		}

	}

	
}
