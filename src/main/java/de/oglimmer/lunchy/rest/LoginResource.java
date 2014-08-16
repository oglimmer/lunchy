package de.oglimmer.lunchy.rest;

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

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.LoginResponseProvider.LoginResponse;

@Path("login")
public class LoginResource {

	private static final String USER_PASS_WRONG = "Email unkown or password incorrect!";

	private LoginResponseProvider loginProvider = LoginResponseProvider.INSTANCE;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse check(@Context HttpServletRequest request, @QueryParam(value = "longTimeToken") String longTimeToken) {
		LoginResponse response = new LoginResponse();
		UsersRecord user = loginProvider.getLoggedInUser(request.getSession(false));
		if (user != null) {
			loginProvider.login(response, user, request.getSession(true));
		}
		if (!response.isSuccess() && longTimeToken != null) {
			UsersRecord userFromToken = UserDao.INSTANCE.getByLongTimeToken(longTimeToken);
			if (userFromToken != null) {
				if (isLongTimeTokenYoungerThen3Month(userFromToken)) {
					loginProvider.login(response, userFromToken, request.getSession(true));
				} else {
					loginProvider.removeToken(userFromToken);
				}
			}
		}
		return response;
	}

	private boolean isLongTimeTokenYoungerThen3Month(UsersRecord user) {
		Calendar now = GregorianCalendar.getInstance();
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new Date(user.getLongtimetimestamp().getTime()));
		cal.add(Calendar.MONTH, 3);
		return now.before(cal);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponse login(@Context HttpServletRequest request, InputParam input) {
		LoginResponse result = new LoginResponse();
		String email = input.getEmail();
		boolean longTimeLogin = email != null && email.startsWith("#");
		if (longTimeLogin) {
			email = email.substring(1);
		}
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email);
		if (user != null) {
			if (!BCrypt.checkpw(input.getPassword(), user.getPassword())) {
				result.setErrorMsg(USER_PASS_WRONG);
			} else {
				if (longTimeLogin) {
					loginProvider.generateToken(user);
				}
				loginProvider.login(result, user, request.getSession(true));
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
			UsersRecord user = loginProvider.getLoggedInUser(request.getSession(false));
			loginProvider.removeToken(user);
			loginProvider.destroySession(request.getSession(false));
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
