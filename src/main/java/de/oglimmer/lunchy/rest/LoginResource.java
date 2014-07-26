package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Path("login")
public class LoginResource {

	/*
	 * curl -H "Content-Type: application/json" -d
	 * '{"email":"oli@zimpasser.de","password":"foo"}'
	 * http://localhost:8080/lunchy/rest/login
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultParam who(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Object userId = session.getAttribute("userId");
			return userId != null ? new ResultParam(true, userId.toString())
					: new ResultParam(false, null);
		} else {
			return new ResultParam(false, null);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResultParam login(@Context HttpServletRequest request,
			InputParam input) {
		ResultParam result = new ResultParam();
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(input.getEmail());
		if (user != null) {
			if (!BCrypt.checkpw(input.getPassword(), user.getPassword())) {
				result.setErrorMsg("User/pass wrong");
			} else {
				request.getSession(true).setAttribute("userId", user.getId());
				result.setSuccess(true);
				user.setLastlogin(new Timestamp(new Date().getTime()));
				UserDao.INSTANCE.store(user);
			}
		} else {
			result.setErrorMsg("User/pass wrong");
		}
		return result;
	}

	@DELETE
	public void logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
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
