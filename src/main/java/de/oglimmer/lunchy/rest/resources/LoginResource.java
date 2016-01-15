package de.oglimmer.lunchy.rest.resources;

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

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.rest.resources.helper.LoginCheck;
import de.oglimmer.lunchy.services.CommunityService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Path("login")
public class LoginResource {

	private static final String USER_PASS_WRONG = "Email unkown or password incorrect!";

	private SessionProvider sessionProvider = SessionProvider.INSTANCE;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse check(@Context HttpServletRequest request, @QueryParam(value = "longTimeToken") String longTimeToken) {
		return new LoginCheck(request, longTimeToken).check();
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

