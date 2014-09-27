package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.email.EmailProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.rest.resources.UserResource.UserCreateInput;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.DateCalcService;

public enum UserProvider {
	INSTANCE;

	public UsersRecord makeNew(HttpServletRequest request, UserCreateInput input) {
		UsersRecord user = new UsersRecord();
		user.setFkCommunity(CommunityService.get(request));
		makeNew(user, input.getPassword());
		return user;
	}

	public void makeNew(UsersRecord user, String password) {
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
		user.setCreatedOn(new Timestamp(new Date().getTime()));
		user.setLastLogin(new Timestamp(new Date().getTime()));
		user.setPermissions(0);
		user.setLastEmailUpdate(DateCalcService.getOneWeekAgo());
		user.setEmailUpdates(0);
	}

	public void sendEmail(UsersRecord user) {
		EmailProvider.INSTANCE.sendWelcome(user.getEmail(), user.getDisplayname(), user.getFkCommunity());
	}

	public LoginResponse storeAndLogin(HttpServletRequest request, UsersRecord userRec) {
		try {

			UserDao.INSTANCE.store(userRec);
			return SessionProvider.INSTANCE.createSession(userRec, request.getSession(true), false);

		} catch (org.jooq.exception.DataAccessException e) {
			if (e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {
				LoginResponse result = new LoginResponse();
				result.setErrorMsg("User already exists");
				return result;
			} else {
				throw e;
			}
		}
	}

	public void setEmailUpdates(UsersRecord user, Integer newEmailUpdate) {
		user.setEmailUpdates(newEmailUpdate);
		if (user.getEmailUpdates() == 1) {
			user.setNextEmailUpdate(DateCalcService.getNever());
		} else {
			user.setNextEmailUpdate(DateCalcService.getNextMonday());
		}
	}
}
