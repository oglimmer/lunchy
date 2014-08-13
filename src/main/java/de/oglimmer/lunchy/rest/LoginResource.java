package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Path("login")
public class LoginResource {

	/*
	 * curl -H "Content-Type: application/json" -d '{"email":"oli@zimpasser.de","password":"foo"}' http://localhost:8080/lunchy/rest/login
	 */

	private static final String USER_PASS_WRONG = "Email unkown or password incorrect!";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultParam check(@Context HttpServletRequest request, @QueryParam(value = "longTimeToken") String longTimeToken) {
		HttpSession session = request.getSession(false);
		ResultParam response = new ResultParam();
		if (session != null) {
			Object userId = session.getAttribute("userId");
			if (userId != null) {
				response.setSuccess(true);
				response.setErrorMsg(userId.toString());
			}
		}
		if (!response.isSuccess() && longTimeToken != null) {
			UsersRecord user = UserDao.INSTANCE.getByLongTimeToken(longTimeToken);
			if (user != null) {

				Calendar now = GregorianCalendar.getInstance();
				Calendar cal = GregorianCalendar.getInstance();
				cal.setTime(new Date(user.getLongtimetimestamp().getTime()));
				cal.add(Calendar.MONTH, 3);
				if (now.before(cal)) {
					response.setSuccess(true);
					response.setErrorMsg(user.getId().toString());
					request.getSession(true).setAttribute("userId", user.getId());
				} else {
					user.setLongtimetoken(null);
					user.setLongtimetimestamp(null);
					UserDao.INSTANCE.store(user);
				}
			}
		}
		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResultParam login(@Context HttpServletRequest request, InputParam input) {
		ResultParam result = new ResultParam();
		String email = input.getEmail();
		if (email != null && email.startsWith("#")) {
			email = email.substring(1);
		}
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email);
		if (user != null) {
			if (!BCrypt.checkpw(input.getPassword(), user.getPassword())) {
				result.setErrorMsg(USER_PASS_WRONG);
			} else {
				request.getSession(true).setAttribute("userId", user.getId());
				result.setSuccess(true);
				user.setLastlogin(new Timestamp(new Date().getTime()));

				if (input.getEmail().startsWith("#")) {
					if (user.getLongtimetoken() == null) {
						user.setLongtimetimestamp(new Timestamp(new Date().getTime()));
						user.setLongtimetoken(RandomStringUtils.randomAlphanumeric(128));
					}
					result.setErrorMsg(user.getLongtimetoken());
				}

				UserDao.INSTANCE.store(user);
			}
		} else {
			result.setErrorMsg(USER_PASS_WRONG);
		}
		return result;
	}

	@DELETE
	public void logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Integer userId = (Integer) session.getAttribute("userId");
			if (userId != null) {
				UsersRecord user = UserDao.INSTANCE.getById(userId);
				if (user.getLongtimetoken() != null) {
					user.setLongtimetimestamp(null);
					user.setLongtimetoken(null);
					UserDao.INSTANCE.store(user);
				}
			}
			session.invalidate();
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputParam {
		private String email;
		private String password;
	}

}
