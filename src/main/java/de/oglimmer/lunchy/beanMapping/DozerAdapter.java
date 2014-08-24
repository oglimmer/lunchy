package de.oglimmer.lunchy.beanMapping;

import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

import org.jooq.Record;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Slf4j
public class DozerAdapter {

	private static LoadingCache<String, String> fieldNameConversionCache = CacheBuilder.newBuilder().build(
			new CacheLoader<String, String>() {
				public String load(String fieldName) {
					StringBuilder buff = new StringBuilder(fieldName.length());
					for (int i = 0; i < fieldName.length(); i++) {
						char c = fieldName.charAt(i);
						if (Character.isUpperCase(c)) {
							buff.append('_');
						}
						buff.append(c);
					}
					return buff.toString();
				}
			});

	private Record delegate;

	public DozerAdapter(Record delegate) {
		this.delegate = delegate;
	}

	public String getString(String fieldName) {
		return (String) getValue(fieldName);
	}

	public Object getValue(String fieldName) {
		try {
			return delegate.getValue(fieldNameConversionCache.get(fieldName));
		} catch (ExecutionException | IllegalArgumentException e) {
			log.error("Failed to get value for fieldname:" + fieldName + " from object " + delegate, e);
		}
		return null;
	}

}
