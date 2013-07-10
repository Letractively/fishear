package net.fishear.data.generic.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.persistence.Embedded;

/**
 * at entity class level allows mark more intervals.
 * 
 * @see Interval
 * @author terber
 *
 */
@Documented
@Retention(RUNTIME)
public @interface Intervals
{
	Interval[] value() default {};
}
