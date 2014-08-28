package de.oglimmer.lunchy.rest.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.DaoFactory;
import de.oglimmer.lunchy.services.Community;

public abstract class BaseResource {

	private static final String[] KEYS = { "Response", "UpdateInput", "CreateInput" };

	protected Response get(HttpServletRequest request, int id, Class<?> clazz) {
		Object rec = DaoFactory.INSTANCE.getDao(getDaoName(clazz)).getById(id, Community.get(request));
		if (rec == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(BeanMappingProvider.INSTANCE.map(rec, clazz)).build();
	}

	private String getDaoName(Class<?> clazz) {
		String className = clazz.getSimpleName();
		for (String key : KEYS) {
			if (className.endsWith(key)) {
				className = className.substring(0, className.indexOf(key));
			}
		}
		return className;
	}

	protected <T> List<T> query(int fkParent, Class<T> clazz) {
		return BeanMappingProvider.INSTANCE.mapList(DaoFactory.INSTANCE.getDao(getDaoName(clazz)).getListByParent(fkParent), clazz);
	}

	protected String convertToJson(List<String> listOfStrings) {
		JsonArray jsonResultList = new JsonArray();
		for (String string : listOfStrings) {
			jsonResultList.add(new JsonPrimitive(string));
		}
		return jsonResultList.toString();
	}

}
