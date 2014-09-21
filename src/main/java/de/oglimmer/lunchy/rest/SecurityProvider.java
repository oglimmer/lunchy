package de.oglimmer.lunchy.rest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.lunchy.database.dao.UserDao;

@Slf4j
public enum SecurityProvider {
	INSTANCE;

	private LoadingCache<Integer, Permission> cb;

	private SecurityProvider() {
		cb = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(new CacheLoader<Integer, Permission>() {
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

	public boolean checkRightOnSession(HttpServletRequest request, Permission permissionToCheck) {
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		if (userId != null) {
			return checkRightOnUser(permissionToCheck, userId);
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
