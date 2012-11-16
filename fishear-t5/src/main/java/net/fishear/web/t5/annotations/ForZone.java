package net.fishear.web.t5.annotations;

//import static java.lang.annotation.ElementType.CONSTRUCTOR;
//import static java.lang.annotation.ElementType.FIELD;
//import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.ElementType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForZone {

	String value() default "";
	
}
