package de.oglimmer.lunchy.beanMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ForeignKey {

	String dao();

	String refColumnLabel();

	String refColumnName();

}
