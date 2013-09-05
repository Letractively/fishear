package net.fishear.data.generic.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.GenericService;

/**
 * Class level annotation that instructs {@link GenericService} to eagerly load properties in its value.
 * 
 * Alternatively each getter may be annotated by {@link Eager} annotation, 
 * or eagerly loaded properties list may be set for each query using {@link QueryConstraints} (which overrides both annotations). 
 * 
 * @author terber
 * 
 * @see QueryConstraints#setEagerLoad(java.util.List)
 * @see QueryConstraints#eager(String...)
 *
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface EagerLoads
{
	/**
	 * @return field name as it appears in entity. If empty, the variavle name is used.
	 */
	String[] value() default {};
}
