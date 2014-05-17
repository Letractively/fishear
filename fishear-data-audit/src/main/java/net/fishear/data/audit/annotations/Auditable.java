package net.fishear.data.audit.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Documented
@Retention(RUNTIME)
public @interface Auditable {

	/**
	 * @return list of property named that should be ignored during comparison.
	 * Implementation does not check non-existing fields. Only those that exist and have difference are ignored.
	 */
	String[] ignored() default {};

}
