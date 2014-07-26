package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Path("/users/{email}")
public class UserResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResultParam check(@Context HttpServletRequest request,
			@PathParam("email") String email) {
		ResultParam result = new ResultParam();
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email);
		if (user != null) {
			result.setSuccess(true);
		}
		return result;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResultParam create(@Context HttpServletRequest request,
			@PathParam("email") String email, InputParam input) {

		ResultParam result = new ResultParam();
		try {
			UsersRecord user = new UsersRecord();
			user.setDisplayname(input.getDisplayname());
			user.setEmail(email);
			user.setPassword(BCrypt.hashpw(input.getPassword(),
					BCrypt.gensalt()));
			user.setCreatedon(new Timestamp(new Date().getTime()));
			user.setPermissions(0);
			UserDao.INSTANCE.store(user);
			result.setSuccess(true);
			request.getSession(true).setAttribute("userId", user.getId());
		} catch (org.jooq.exception.DataAccessException e) {
			if (e.getCause() instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {
				result.setErrorMsg("User already exists");
			} else {
				throw e;
			}
		}
		return result;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InputParam {
		private String password;
		private String displayname;
	}

}
