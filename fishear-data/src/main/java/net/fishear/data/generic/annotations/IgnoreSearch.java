package net.fishear.data.generic.annotations;

/**
 * marks getters and fields which are explicitly declared as serchable.
 * If the field is searchable, it is always added to search condition.
 * It is possible pat other name then the field / getter name.
 * 
 * @author terber
 *
 */
public @interface IgnoreSearch
{
	/**
	 * @return field name as it appears in entity. If empty, the variavle name is used.
	 */
	String name() default "";

}
