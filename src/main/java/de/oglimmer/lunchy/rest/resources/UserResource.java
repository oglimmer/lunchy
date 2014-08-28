package de.oglimmer.lunchy.rest.resources;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import javax.ws.rs.core.Response.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.SecurityProvider;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.rest.dto.ResultParam;
import de.oglimmer.lunchy.rest.dto.UserAdminResponse;
import de.oglimmer.lunchy.services.Community;
import de.oglimmer.lunchy.services.DateCalculation;
import de.oglimmer.lunchy.services.Email;

@Path("users")
public class UserResource extends BaseResource {

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{email}")
	public ResultParam checkByEmail(@Context HttpServletRequest request, @PathParam("email") String email) {
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email, Community.get(request));
		ResultParam rp = new ResultParam();
		if (user != null) {
			rp.setSuccess(true);
			rp.setErrorMsg(Integer.toString(user.getId()));
		}
		return rp;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/savePermission")
	public ResultParam savePermission(@Context HttpServletRequest request, @PathParam("id") Integer id, PermissionInput input) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		ResultParam rp = new ResultParam();
		if (input.getPermissions() == 0 || input.getPermissions() == 1) {
			UsersRecord user = UserDao.INSTANCE.getById(id, Community.get(request));
			if (user != null) {
				if (user.getPermissions() != input.getPermissions()) {
					user.setPermissions(input.getPermissions());
					UserDao.INSTANCE.store(user);
					SecurityProvider.INSTANCE.updateCache(user.getId());
				}
				rp.setSuccess(true);
			}
		}
		return rp;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{email}/sendPasswordLink")
	public ResultParam sendPasswordLink(@Context HttpServletRequest request, @PathParam("email") String email) {
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email, Community.get(request));
		ResultParam rp = new ResultParam();
		if (user != null) {
			user.setPasswordResetToken(RandomStringUtils.randomAlphanumeric(128));
			user.setPasswordResetTimestamp(new Timestamp(new Date().getTime()));
			UserDao.INSTANCE.store(user);
			Email.INSTANCE.sendPasswordLink(user);
			rp.setSuccess(true);
		}
		return rp;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{token}/resetPassword")
	public ResultParam resetPassword(@Context HttpServletRequest request, @PathParam("token") String token, PasswordInput input) {
		UsersRecord user = UserDao.INSTANCE.getUserByToken(token, Community.get(request));
		ResultParam rp = new ResultParam();
		if (user != null) {
			if (DateCalculation.INSTANCE.youngerThan(user.getPasswordResetTimestamp(), Calendar.HOUR_OF_DAY, 24)) {
				user.setPasswordResetToken(null);
				user.setPasswordResetTimestamp(null);
				user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
				UserDao.INSTANCE.store(user);
				rp.setSuccess(true);
				Email.INSTANCE.sendPasswordResetDone(user);
			} else {
				rp.setErrorMsg("Token too old");
			}
		} else {
			rp.setErrorMsg("Token not found");
		}
		return rp;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserAdminResponse> query(@Context HttpServletRequest request) {
		SecurityProvider.INSTANCE.checkAdmin(request);
		return query(Community.get(request), UserAdminResponse.class);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("current")
	public Response current(@Context HttpServletRequest request) {
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		if (userId != null) {
			UsersRecord user = UserDao.INSTANCE.getById(userId, Community.get(request));
			return Response.ok(BeanMappingProvider.INSTANCE.map(user, UserResponse.class)).build();
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public UserResponse get(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		UsersRecord user = UserDao.INSTANCE.getById(id, Community.get(request));
		return BeanMappingProvider.INSTANCE.map(user, UserResponse.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponse createAndLogin(@Context HttpServletRequest request, UserCreateInput input) {
		UsersRecord user = new UsersRecord();
		user.setFkCommunity(Community.get(request));
		user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
		user.setCreatedOn(new Timestamp(new Date().getTime()));
		user.setLastLogin(new Timestamp(new Date().getTime()));
		user.setPermissions(0);
		user.setLastEmailUpdate(DateCalculation.INSTANCE.getOneWeekAgo());
		// set email update via input dto to avoid overwriting it with null
		input.setEmailUpdates(0);
		Email.INSTANCE.sendWelcome(input.getEmail(), input.getDisplayname(), Community.get(request));

		return store(request, user, input);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public LoginResponse updateAndLogin(@Context HttpServletRequest request, @PathParam("id") Integer id, UserUpdateInput input) {
		LoginResponse result;

		UsersRecord user = UserDao.INSTANCE.getById(id, Community.get(request));
		if (user == null || !BCrypt.checkpw(input.getCurrentpassword(), user.getPassword())) {
			result = new LoginResponse();
			result.setErrorMsg("Password wrong");
		} else {
			if (input.getPassword() != null && !input.getPassword().trim().isEmpty()) {
				user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
			}
			result = store(request, user, input);
		}
		return result;
	}

	private LoginResponse store(HttpServletRequest request, UsersRecord userRec, UserUpdateInput inputDto) {
		try {
			copyDtoToRec(userRec, inputDto);

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

	private void copyDtoToRec(UsersRecord user, UserUpdateInput input) {
		// HACK: avoid overwriting password
		input.setPassword(user.getPassword());
		BeanMappingProvider.INSTANCE.map(input, user);
		handleEmailUpdates(user, input);
	}

	private void handleEmailUpdates(UsersRecord user, UserUpdateInput input) {
		if (input.getEmailUpdates() != null) {
			setEmailUpdates(user, input.getEmailUpdates());
		}
	}

	private void setEmailUpdates(UsersRecord user, Integer newEmailUpdate) {
		user.setEmailUpdates(newEmailUpdate);
		if (user.getEmailUpdates() == 1) {
			user.setNextEmailUpdate(DateCalculation.INSTANCE.getNever());
		} else {
			user.setNextEmailUpdate(DateCalculation.INSTANCE.getNextMonday());
		}
	}

	@Data
	public static class PasswordInput {
		private String password;
	}

	@Data
	public static class PermissionInput {
		private Integer permissions;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class UserCreateInput extends UserUpdateInput {
	}

	@Data
	public static class UserUpdateInput {
		private String email;
		private String password;
		private String currentpassword;
		private String displayname;
		private Integer fkBaseOffice;
		private Integer emailUpdates;
	}

	@Data
	public static class UserResponse {
		private Integer id;
		private String email;
		private String displayname;
		private Integer fkBaseOffice;
		// needs to be type String or angular would not === values
		private String emailUpdates;
	}

}
