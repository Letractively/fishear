package net.fishear.web.t5.annotations;

//import static java.lang.annotation.ElementType.CONSTRUCTOR;
//import static java.lang.annotation.ElementType.FIELD;
//import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.ElementType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * marks field that must implement ServiceI for given entity (the same as class is designed for).
 * It may be used alternatively to getService methods
 * 
 * 
 * @author ffyxrr
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataService {

}
