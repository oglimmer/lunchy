package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;

import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.services.DateCalcService;

public enum SessionProvider {
	INSTANCE;

	private static final String ATTR_NAME = "userId";

	public LoginResponse createSession(UsersRecord user, HttpSession session, boolean setToken) {
		LoginResponse loginResponse = new LoginResponse();
		fillResponse(loginResponse, user, setToken);
		createSession(user, session);
		user.setLastLogin(DateCalcService.getNow());
		UserDao.INSTANCE.store(user);
		return loginResponse;
	}

	private void createSession(UsersRecord user, HttpSession session) {
		session.setAttribute(ATTR_NAME, user.getId());
	}

	public void destroySession(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
	}

	public Integer getLoggedInUserId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return getLoggedInUserId(session);
		}
		return null;
	}

	public Integer getLoggedInUserId(HttpSession session) {
		if (session != null) {
			return (Integer) session.getAttribute(ATTR_NAME);
		}
		return null;
	}

	public UsersRecord getLoggedInUser(HttpServletRequest request, int fkCommunity) {
		return getLoggedInUser(request.getSession(false), fkCommunity);

	}

	public UsersRecord getLoggedInUser(HttpSession session, int fkCommunity) {
		Integer userId = getLoggedInUserId(session);
		if (userId != null) {
			return UserDao.INSTANCE.getById(userId, fkCommunity);
		}
		return null;
	}

	private void fillResponse(LoginResponse loginResponse, UsersRecord user, boolean setToken) {
		loginResponse.setSuccess(true);
		loginResponse.setFkOffice(user.getFkBaseOffice());
		loginResponse.setUserId(user.getId());
		loginResponse.setPermissions(user.getPermissions());
		if (setToken) {
			loginResponse.setLongTimeToken(user.getLongTimeToken());
		}
	}

	public void removeToken(UsersRecord user) {
		if (user != null && user.getLongTimeToken() != null) {
			user.setLongTimeToken(null);
			user.setLongTimeTimestamp(null);
			UserDao.INSTANCE.store(user);
		}
	}

	public void generateToken(UsersRecord user) {
		if (user.getLongTimeToken() == null) {
			user.setLongTimeTimestamp(DateCalcService.getNow());
			user.setLongTimeToken(RandomStringUtils.randomAlphanumeric(128));
		}
	}

}
