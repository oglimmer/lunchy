package de.oglimmer.lunchy.rest;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.lunchy.database.UserDao;

@Slf4j
public enum SecurityProvider {
	INSTANCE;

	private LoadingCache<Integer, Permission> cb;

	private SecurityProvider() {
		cb = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Permission>() {
			public Permission load(Integer key) {
				return Permission.fromVal(UserDao.INSTANCE.getById(key).getPermissions());
			}
		});
	}

	public void checkAdmin(HttpServletRequest request) {
		checkRight(request, Permission.ADMIN);
	}

	public void checkConfirmedUser(HttpServletRequest request) {
		checkRight(request, Permission.CONFIRMED_USER);
	}

	private void checkRight(HttpServletRequest request, Permission permissionToCheck) {
		if (!checkRightOnSession(request, permissionToCheck)) {
			throw new UserRightException();
		}
	}

	private boolean checkRightOnSession(HttpServletRequest request, Permission permissionToCheck) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Integer userId = (Integer) session.getAttribute("userId");
			if (userId != null) {
				return checkRightOnUser(permissionToCheck, userId);
			}
		}
		return false;
	}

	private boolean checkRightOnUser(Permission permissionToCheck, Integer userId) {
		try {
			Permission permission = cb.get(userId);
			if (permission.getVal() >= permissionToCheck.getVal()) {
				return true;
			}
		} catch (ExecutionException e) {
			log.error("Failed to ret permission from cache", e);
		}
		return false;
	}

	public void updateCache(Integer id) {
		cb.refresh(id);
	}

	public void reset() {
		cb.invalidateAll();
	}

}

@AllArgsConstructor
enum Permission {
	USER(0), CONFIRMED_USER(1), ADMIN(2);
	@Getter
	private int val;

	static Permission fromVal(int val) {
		for (Permission per : values()) {
			if (per.getVal() == val) {
				return per;
			}
		}
		return null;
	}
}