package net.fishear.data.generic.annotations;

import javax.persistence.Embedded;

/**
 * marks {@link Embedded} value as an interval.
 * If the field is searchable, it is always added to search condition.
 * It is possible pat other name then the field / getter name.
 * 
 * @author terber
 *
 */
public @interface Interval
{
	/**
	 * @return field name as it appears in entity. If empty, the variavle name is used.
	 */
	String[] value() default {};
}
