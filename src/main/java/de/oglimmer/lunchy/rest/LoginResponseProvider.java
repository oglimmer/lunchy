package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.LoginResponse;

public enum LoginResponseProvider {
	INSTANCE;

	private static final String ATTR_NAME = "userId";

	public void login(LoginResponse loginResponse, UsersRecord user, HttpSession session) {
		fillResponse(loginResponse, user);
		createSession(user, session);
		user.setLastLogin(new Timestamp(new Date().getTime()));
		UserDao.INSTANCE.store(user);
	}

	private void createSession(UsersRecord user, HttpSession session) {
		session.setAttribute(ATTR_NAME, user.getId());
	}

	public void destroySession(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
	}

	public Integer getLoggedInUserId(HttpSession session) {
		if (session != null) {
			return (Integer) session.getAttribute(ATTR_NAME);
		}
		return null;
	}

	public UsersRecord getLoggedInUser(HttpSession session, int fkCommunity) {
		Integer userId = getLoggedInUserId(session);
		if (userId != null) {
			return UserDao.INSTANCE.getById(userId, fkCommunity);
		}
		return null;
	}

	private void fillResponse(LoginResponse loginResponse, UsersRecord user) {
		loginResponse.setSuccess(true);
		loginResponse.setFkOffice(user.getFkBaseOffice());
		loginResponse.setUserId(user.getId());
		loginResponse.setLongTimeToken(user.getLongTimeToken());
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
			user.setLongTimeTimestamp(new Timestamp(new Date().getTime()));
			user.setLongTimeToken(RandomStringUtils.randomAlphanumeric(128));
		}
	}

}
