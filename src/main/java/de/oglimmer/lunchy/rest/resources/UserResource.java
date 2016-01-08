package de.oglimmer.lunchy.rest.resources;

import java.util.Calendar;
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
import de.oglimmer.lunchy.email.EmailProvider;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.UserProvider;
import de.oglimmer.lunchy.rest.dto.LoginResponse;
import de.oglimmer.lunchy.rest.dto.ResultParam;
import de.oglimmer.lunchy.rest.dto.UserAdminResponse;
import de.oglimmer.lunchy.rest.dto.UserResponse;
import de.oglimmer.lunchy.security.Permission;
import de.oglimmer.lunchy.security.SecurityProvider;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.DateCalcService;

@Path("users")
public class UserResource extends BaseResource {

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{email}")
	public ResultParam checkByEmail(@Context HttpServletRequest request, @PathParam("email") String email) {
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email, CommunityService.get(request));
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
			UsersRecord user = UserDao.INSTANCE.getById(id, CommunityService.get(request));
			if (user != null) {
				if (user.getPermissions() != input.getPermissions() && user.getPermissions() != 2) {
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
	@Path("saveListViewColConfig")
	public ResultParam saveListViewColConfig(@Context HttpServletRequest request, ListViewColConfigInput input) {
		UsersRecord userRec = SessionProvider.INSTANCE.getLoggedInUser(request, CommunityService.get(request));
		userRec.setListViewColPrio(input.getListViewColPrio());
		UserDao.INSTANCE.store(userRec);
		return new ResultParam(true, null);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{email}/sendPasswordLink")
	public ResultParam sendPasswordLink(@Context HttpServletRequest request, @PathParam("email") String email) {
		UsersRecord user = UserDao.INSTANCE.getUserByEmail(email, CommunityService.get(request));
		ResultParam rp = new ResultParam();
		if (user != null) {
			user.setPasswordResetToken(RandomStringUtils.randomAlphanumeric(128));
			user.setPasswordResetTimestamp(DateCalcService.getNow());
			UserDao.INSTANCE.store(user);
			EmailProvider.INSTANCE.sendPasswordLink(user);
			rp.setSuccess(true);
		}
		return rp;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{token}/resetPassword")
	public ResultParam resetPassword(@Context HttpServletRequest request, @PathParam("token") String token, PasswordInput input) {
		UsersRecord user = UserDao.INSTANCE.getUserByToken(token, CommunityService.get(request));
		ResultParam rp = new ResultParam();
		if (user != null) {
			if (DateCalcService.youngerThan(user.getPasswordResetTimestamp(), Calendar.HOUR_OF_DAY, 24)) {
				user.setPasswordResetToken(null);
				user.setPasswordResetTimestamp(null);
				user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
				UserDao.INSTANCE.store(user);
				rp.setSuccess(true);
				EmailProvider.INSTANCE.sendPasswordResetDone(user);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<UserAdminResponse> query(@Context HttpServletRequest request) {
		Class clazz = SecurityProvider.INSTANCE.checkRightOnSession(request, Permission.ADMIN) ? UserAdminResponse.class
				: UserResponse.class;
		return query(CommunityService.get(request), clazz, "User");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("current")
	public Response current(@Context HttpServletRequest request) {
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		if (userId != null) {
			UsersRecord user = UserDao.INSTANCE.getById(userId, CommunityService.get(request));
			return Response.ok(BeanMappingProvider.INSTANCE.map(user, UserSelfResponse.class)).build();
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public UserSelfResponse get(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		UsersRecord user = UserDao.INSTANCE.getById(id, CommunityService.get(request));
		return BeanMappingProvider.INSTANCE.map(user, UserSelfResponse.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponse createAndLogin(@Context HttpServletRequest request, UserCreateInput input) {
		UsersRecord user = UserProvider.INSTANCE.makeNew(request, input);
		// HACK: avoid overwriting
		input.setEmailUpdates(user.getEmailUpdates());
		copyDtoToRec(user, input);
		UserProvider.INSTANCE.sendEmail(user);
		return UserProvider.INSTANCE.storeAndLogin(request, user);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public LoginResponse updateAndLogin(@Context HttpServletRequest request, @PathParam("id") Integer id, UserUpdateInput input) {
		LoginResponse result;

		UsersRecord user = UserDao.INSTANCE.getById(id, CommunityService.get(request));
		if (user == null || !BCrypt.checkpw(input.getCurrentpassword(), user.getPassword())) {
			result = new LoginResponse();
			result.setErrorMsg("Password wrong");
		} else {
			if (input.getPassword() != null && !input.getPassword().trim().isEmpty()) {
				user.setPassword(BCrypt.hashpw(input.getPassword(), BCrypt.gensalt()));
			}
			copyDtoToRec(user, input);
			result = UserProvider.INSTANCE.storeAndLogin(request, user);
		}
		return result;
	}

	private void copyDtoToRec(UsersRecord user, UserUpdateInput input) {
		// HACK: avoid overwriting password
		input.setPassword(user.getPassword());
		BeanMappingProvider.INSTANCE.map(input, user);
		handleEmailUpdates(user, input);
	}

	private void handleEmailUpdates(UsersRecord user, UserUpdateInput input) {
		if (input.getEmailUpdates() != null) {
			UserProvider.INSTANCE.setEmailUpdates(user, input.getEmailUpdates());
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
	public static class ListViewColConfigInput {
		private String listViewColPrio;
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
	public static class UserSelfResponse {
		private Integer id;
		private String email;
		private String displayname;
		private Integer fkBaseOffice;
		// needs to be type String or angular would not === values
		private String emailUpdates;
		private String listViewColPrio;
	}

}
