package net.fishear.data.generic.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.util.Collection;

import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.GenericService;

/**
 * getter annotation, that instructs {@link GenericService} to load field's content programmatically after entity is loaded (aither in list method or read methos).
 * This is done by access of this field's value, in case {@link Collection} calls {@link Collection#size()} method.
 * Another way to set eagerly loaded fields is class level annotation {@link EagerLoads}.
 * Eagerly loaded properties list may also be set for each query using {@link QueryConstraints} (which overrides both annotations - uf you use empty list or array for the query, no eager loading will be applied).
 * 
 * @author terber
 *
 */
@Documented
@Retention(RUNTIME)
public @interface Eager
{

}
