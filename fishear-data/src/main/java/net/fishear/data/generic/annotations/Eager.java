package net.fishear.data.generic.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.GenericService;

/**
 * getter annotation, that instructs {@link GenericService} to load field's content immediately after entity is loaded (either in list method or read methods).
 * Underlying framework way is used to load to load lazy initialized properties values . 
 * Another way to set eagerly loaded fields is class level annotation {@link EagerLoads}.
 * Eagerly loaded properties may be also set for each query using {@link QueryConstraints} (which overrides both annotations - if you use empty list or array for the query, no eager loading will be applied).
 * 
 * @author terber
 *
 */
@Documented
@Retention(RUNTIME)
public @interface Eager
{

}
