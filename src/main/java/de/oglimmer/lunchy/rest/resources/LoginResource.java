package de.oglimmer.lunchy.rest.resources;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.DateCalcService;

@Path("login")
public class LoginResource {

	private static final String USER_PASS_WRONG = "Email unkown or password incorrect!";

	private SessionProvider sessionProvider = SessionProvider.INSTANCE;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse check(@Context HttpServletRequest request, @QueryParam(value = "longTimeToken") String longTimeToken) {
		return new CheckLogic(request, longTimeToken).check();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(@Context HttpServletRequest request, InputParam input) {
		return new LoginLogic(request, input).login();
	}

	@DELETE
	public void logout(@Context HttpServletRequest request) {
		UsersRecord user = sessionProvider.getLoggedInUser(request, CommunityService.get(request));
		if (user != null) {
			sessionProvider.removeToken(user);
			sessionProvider.destroySession(request.getSession(false));
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputParam {
		private String email;
		private String password;
		private boolean keepLoggedIn;
	}

	class CheckLogic {

		private HttpServletRequest request;
		private String longTimeToken;

		private CheckLogic(HttpServletRequest request, String longTimeToken) {
			this.request = request;
			this.longTimeToken = longTimeToken;

		}

		private LoginResponse check() {
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

	@AllArgsConstructor
	class LoginLogic {

		private HttpServletRequest request;
		private InputParam input;

		private Response login() {
			LoginResponse result;
			UsersRecord user = getUserFromInput();
			if (user != null) {
				result = processUserLogin(user);
			} else {
				result = getFailResponse();
			}
			return buildResponse(result);
		}

		private UsersRecord getUserFromInput() {
			return UserDao.INSTANCE.getUserByEmail(input.getEmail(), CommunityService.get(request));
		}

		private LoginResponse processUserLogin(UsersRecord user) {
			if (isPasswordCorrect(user)) {
				handleKeepLoggedIn(user);
				return sessionProvider.createSession(user, request.getSession(true), input.isKeepLoggedIn());
			} else {
				return getFailResponse();
			}
		}

		private void handleKeepLoggedIn(UsersRecord user) {
			if (input.isKeepLoggedIn()) {
				sessionProvider.generateToken(user);
			}
		}

		private boolean isPasswordCorrect(UsersRecord user) {
			return BCrypt.checkpw(input.getPassword(), user.getPassword());
		}

		private LoginResponse getFailResponse() {
			LoginResponse result = new LoginResponse();
			result.setErrorMsg(USER_PASS_WRONG);
			return result;
		}

		private Response buildResponse(LoginResponse result) {
			Response.ResponseBuilder builder = Response.ok(result);
			if (input.isKeepLoggedIn()) {
				builder.cookie(new NewCookie("lunchylogintoken", result.getLongTimeToken(), request.getContextPath(), null, null,
						60 * 60 * 24 * 90, false));
			}
			return builder.build();
		}
	}

}
