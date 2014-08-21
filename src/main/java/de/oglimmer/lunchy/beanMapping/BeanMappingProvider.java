package de.oglimmer.lunchy.beanMapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldDefinition;
import org.dozer.loader.api.TypeDefinition;
import org.dozer.loader.api.TypeMappingBuilder;
import org.reflections.Reflections;

public enum BeanMappingProvider {
	INSTANCE;

	private DozerBeanMapper dbm = new DozerBeanMapper();

	private BeanMappingProvider() {
		dbm.addMapping(new AutoBeanMappingBuilder());
	}

	public DozerBeanMapper getMapper() {
		return dbm;
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
				createFields(tmb, field);
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
