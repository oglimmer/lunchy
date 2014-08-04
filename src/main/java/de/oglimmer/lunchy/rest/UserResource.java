package de.oglimmer.lunchy.rest;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.Data;

import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.database.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;

@Path("/users")
public class UserResource {

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response checkByEmail(@Context HttpServletRequest request, @PathParam("id") String email) {
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email);
		ResultParam rp = new ResultParam();
		if (user != null) {
			rp.setSuccess(true);
			rp.setErrorMsg(Integer.toString(user.getId()));
		}
		return Response.ok(rp).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("current")
	public UserResponse get(@Context HttpServletRequest request) {
		UsersRecord user = UserDao.INSTANCE.getById((Integer) request.getSession(true).getAttribute("userId"));
		return UserResponse.getInstance(user);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public UserResponse get(@PathParam("id") Integer id) {
		UsersRecord user = UserDao.INSTANCE.getById(id);
		return UserResponse.getInstance(user);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public ResultParam create(@Context HttpServletRequest request, @PathParam("id") Integer id, UserInput input) {
		if (id != input.getId()) {
			throw new RuntimeException("id not matching");
		}
		return create(request, input);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResultParam create(@Context HttpServletRequest request, UserInput input) {
		ResultParam result = new ResultParam();

		try {
			UsersRecord user;
			if (input.getId() == null || input.getId() == 0) {
				user = new UsersRecord();
				user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
				user.setCreatedon(new Timestamp(new Date().getTime()));
				user.setLastlogin(new Timestamp(new Date().getTime()));
				user.setPermissions(0);
			} else {
				user = UserDao.INSTANCE.getById(input.getId());
				if (!BCrypt.checkpw(input.getCurrentpassword(), user.getPassword())) {
					result.setErrorMsg("Password wrong");
					user = null;
				} else {
					if (input.getPassword() != null && !input.getPassword().trim().isEmpty()) {
						user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
					}
				}
			}
			if (user != null) {
				user.setDisplayname(input.getDisplayname());
				user.setEmail(input.getEmail());
				UserDao.INSTANCE.store(user);
				result.setSuccess(true);
				request.getSession(true).setAttribute("userId", user.getId());
			}
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
	public static class UserInput {
		private Integer id;
		private String email;
		private String password;
		private String currentpassword;
		private String displayname;
	}

	@Data
	public static class UserResponse {
		private Integer id;
		private String email;
		private String displayname;

		public static UserResponse getInstance(UsersRecord userRec) {
			UserResponse userDto = new UserResponse();
			BeanMappingProvider.INSTANCE.getMapper().map(userRec, userDto);
			return userDto;
		}
	}

}
