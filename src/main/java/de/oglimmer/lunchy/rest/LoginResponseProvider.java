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

	public void login(LoginResponse loginResponse, UsersRecord user, HttpSession session) {
		fillResponse(loginResponse, user);
		createSession(user, session);
		user.setLastlogin(new Timestamp(new Date().getTime()));
		UserDao.INSTANCE.store(user);
	}

	private void createSession(UsersRecord user, HttpSession session) {
		session.setAttribute("userId", user.getId());
	}

	public void destroySession(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
	}

	public Integer getLoggedInUserId(HttpSession session) {
		if (session != null) {
			return (Integer) session.getAttribute("userId");
		}
		return null;
	}

	public UsersRecord getLoggedInUser(HttpSession session) {
		Integer userId = getLoggedInUserId(session);
		if (userId != null) {
			return UserDao.INSTANCE.getById(userId);
		}
		return null;
	}

	private void fillResponse(LoginResponse loginResponse, UsersRecord user) {
		loginResponse.setSuccess(true);
		loginResponse.setFkOffice(user.getFkbaseoffice());
		loginResponse.setUserId(user.getId());
		loginResponse.setLongTimeToken(user.getLongtimetoken());
	}

	public void removeToken(UsersRecord user) {
		if (user != null && user.getLongtimetoken() != null) {
			user.setLongtimetoken(null);
			user.setLongtimetimestamp(null);
			UserDao.INSTANCE.store(user);
		}
	}

	public void generateToken(UsersRecord user) {
		if (user.getLongtimetoken() == null) {
			user.setLongtimetimestamp(new Timestamp(new Date().getTime()));
			user.setLongtimetoken(RandomStringUtils.randomAlphanumeric(128));
		}
	}

}
