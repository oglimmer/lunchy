package de.oglimmer.lunchy.beanMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.SneakyThrows;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldDefinition;
import org.dozer.loader.api.TypeDefinition;
import org.dozer.loader.api.TypeMappingBuilder;
import org.jooq.Record;
import org.reflections.Reflections;

import de.oglimmer.lunchy.database.Dao;
import de.oglimmer.lunchy.database.DaoFactory;

public enum BeanMappingProvider {
	INSTANCE;

	private DozerBeanMapper dbm = new DozerBeanMapper();
	private KeyResolver keyResolver = new KeyResolver();

	private BeanMappingProvider() {
		dbm.addMapping(new AutoBeanMappingBuilder());
	}

	public void map(Object source, Object destination) {
		dbm.map(source, destination);
		keyResolver.resolve(source, destination);
	}

	public <T> T map(Object source, Class<T> destinationClass) {
		T obj = dbm.map(source, destinationClass);
		keyResolver.resolve(source, obj);
		return obj;
	}

	public <T> List<T> mapList(Collection<?> col, Class<T> clazz) {
		List<T> resultList = new ArrayList<>();
		for (Object rec : col) {
			resultList.add(map(rec, clazz));
		}
		return resultList;
	}

}

/**
 * Creates Dozer mappings for all target classes annotated with @RestDto. It is assumed that the source class is of type DozerAdapter. As
 * this is a generic data container it creates a field mapping for each attribute in the target class.
 */
class AutoBeanMappingBuilder extends BeanMappingBuilder {
	@Override
	protected void configure() {
		Reflections ref = new Reflections("de.oglimmer");
		Set<Class<?>> allRestDtoClasses = ref.getTypesAnnotatedWith(RestDto.class);
		for (Class<?> clazz : allRestDtoClasses) {
			handleRestDtoClass(clazz);
		}
	}

	private void handleRestDtoClass(Class<?> clazz) {
		TypeDefinition from = new TypeDefinition(DozerAdapter.class);
		TypeDefinition to = new TypeDefinition(clazz);
		TypeMappingBuilder tmb = mapping(from, to);
		for (Field field : getAllFields(clazz)) {
			if (!field.isSynthetic()) {
				ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
				if (foreignKey == null) {
					createFields(tmb, field);
				}
			}
		}
	}

	private void createFields(TypeMappingBuilder typeMappingBuilder, Field field) {
		// fieldA (the from) is of type DozerAdapter
		FieldDefinition fieldA = new FieldDefinition("this");
		fieldA.mapMethods("getValue", null);
		fieldA.mapKey(field.getName());
		// fieldB (the to) is a standard java bean
		FieldDefinition fieldB = new FieldDefinition(field.getName());
		typeMappingBuilder.fields(fieldA, fieldB);
	}

	private Set<Field> getAllFields(Class<?> type) {
		Set<Field> fields = new HashSet<>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}
};

/**
 * Fields on a target DTO annotated with ForeignKey, needs to get their value from another Record
 */
class KeyResolver {
	/**
	 * @param loadedRecord
	 *            either of type Record or DozerAdapter
	 * @param targetDto
	 */
	@SneakyThrows(value = { IllegalArgumentException.class, IllegalAccessException.class, SecurityException.class,
			InvocationTargetException.class, NoSuchMethodException.class })
	public void resolve(Object loadedRecord, Object targetDto) {
		for (Field fieldOnTargetDto : getAllFields(targetDto.getClass())) {
			ForeignKey foreignKey = fieldOnTargetDto.getAnnotation(ForeignKey.class);
			if (foreignKey != null) {
				setFieldWithForeignKeyAnnotation(loadedRecord, targetDto, fieldOnTargetDto, foreignKey);
			}
		}
	}

	private void setFieldWithForeignKeyAnnotation(Object loadedRecord, Object targetDto, Field targetField, ForeignKey foreignKey)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Dao<?> dao = DaoFactory.INSTANCE.getDao(foreignKey.dao());
		Integer entityId = getIntValueGeneric(loadedRecord, foreignKey.refColumnName());
		Integer fkCommunity = getIntValueGeneric(loadedRecord, "fkCommunity");
		Record referencedRecord = dao.getById(entityId, fkCommunity);
		setValueOnTargetDto(targetDto, targetField, foreignKey, referencedRecord);
	}

	private void setValueOnTargetDto(Object targetDto, Field targetField, ForeignKey foreignKey, Record referencedRecord)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		targetField.setAccessible(true);
		targetField.set(targetDto, callGetter(referencedRecord, foreignKey.refColumnLabel()));
	}

	private Integer getIntValueGeneric(Object object, String recordKey) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		if (object instanceof DozerAdapter) {
			return getIntValueGeneric((DozerAdapter) object, recordKey);
		} else if (object instanceof Record) {
			return getIntValueGeneric((Record) object, recordKey);
		} else {
			throw new RuntimeException("Unsupported object of type " + object.getClass().getName());
		}
	}

	private Integer getIntValueGeneric(DozerAdapter object, String recordKey) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Method method = object.getClass().getDeclaredMethod("getValue", String.class);
		return (Integer) method.invoke(object, recordKey);
	}

	private Integer getIntValueGeneric(Record object, String recordKey) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		Method method = object.getClass().getDeclaredMethod("get" + StringUtils.capitalize(recordKey));
		return (Integer) method.invoke(object);
	}

	private Object callGetter(Object record, String getterMethodName) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return record.getClass().getMethod("get" + StringUtils.capitalize(getterMethodName)).invoke(record);
	}

	private Set<Field> getAllFields(Class<?> type) {
		Set<Field> fields = new HashSet<>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}
}
