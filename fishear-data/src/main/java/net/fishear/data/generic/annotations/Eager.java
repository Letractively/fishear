package net.fishear.data.generic.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.persistence.Embedded;

import net.fishear.data.generic.query.restrictions.Restrictions;

/**
 * marks {@link Embedded} couple of properties inside entity or {@link Embedded} value as an interval
 * If marked, descendant of AbstractSearch generates overlapping search for such fields (see {@link Restrictions#overlap(String, String, Object, Object)} ).

 * On class level it can tie couple of properties as an interval. If more intervals is required, {@link Intervals} annotation is used instead.
 * 
 * @author terber
 *
 */
@Documented
@Retention(RUNTIME)
public @interface Eager
{

}
