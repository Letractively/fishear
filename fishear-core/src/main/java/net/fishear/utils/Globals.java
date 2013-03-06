package net.fishear.utils;


import net.fishear.Interfaces.DescriptionI;
import net.fishear.exceptions.AppException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class Globals {
	
	
	public static final Class<Double> doubleClass = Double.TYPE;
	public static final Class<Float> floatClass = Float.TYPE;
	
    private static final Hashtable<String, String> RESOLVED_CFGS = new Hashtable<String, String>();

    private static final Hashtable<Class<?>, Logger> LOGGER_CACHE = new Hashtable<Class<?>, Logger>();

    /**
     * returns value of parameter passed as JAVA definition or OS variable (which is found first in this order).
     * Result is written to stdout. Successfully solved parameter values are cached - value returned in first call is returned always.
     *
     * @param paramName required name of the parameter. Parameter name is "normalized" (case, dots and underlines 
     * are changed as needed) according to parameter search scope: 
     * <ul>
     * <li>for OS variable the name is converted to upper case to form  "XXX_YYY_ZZZ..."</li>
     * <li>for java definition it is converted to lowercase to form "xxx.yyy.zzz...."</li>
     * </ul>
     * @param dft default value, returned in case param value is not solved. This value is not cached.
     * @param log can be null = no logging is provided. If it is not null, method writes variable source using it's info method.
     * @return value of either property/variable, or 'dft' if neither is found.
     */
    public static final String getEnvOrDef(String paramName, String dft, Logger log) {
        paramName = paramName.replace('_', '.').toLowerCase();	// normalize for JAVA defs - in this form is cached
        String env, tParn;
        env = RESOLVED_CFGS.get(paramName);
        if (env != null) {
            return env;					// is in cache => return cached value
        }

        if(log == null) {
        	log = LoggerFactory.getLogger(Globals.class);
        }
        env = System.getProperty(tParn = paramName, null);
        if (env == null) {
            try {
                env = System.getenv(tParn = paramName.replace('.', '_').toUpperCase());  	// normalize for OS env
            } catch (Throwable ex) {
                env = null;				// that is OK - jdk 5 does not support env variables (only <= 1.4 and >= 1.6)
            }
            if (env == null) {
                log.info(
                        "Parameter '" + paramName +
                        "' not found either in OS variables or java definitions. " +
                        (dft == null ? "" : "Default returned:\n  => '" + dft + "'")
                );
            } else {
                log.info(
                        "Parameter '" + paramName +
                        "' has taken from OS variables as '" +
                        tParn + "'. Value: \n  => '" + env + "'"
                );
            }
        } else {
            log.info(
                    "Parameter '" + paramName +
                    "' has taken from JRE definitions as '" +
                    tParn + "'. Value: \n  => '" + env + "'"
            );
        }
        if (env != null) {
            RESOLVED_CFGS.put(paramName, env);
        } else if(dft != null) {
        	env = dft;						// not cached
        }
        return env;
    }

    /**
     * returns value of parameter passed as JAVA definition or OS variable (which is found first in this order).
     * Result is written to stdout. Successfully solved parameter values are cached - value returned in first call is returned always.
     *
     * @return value of either property/variable, or null if neither is found.
     * @see #getEnvOrDef(String, String)
     */
    public static final String getEnvOrDef(String paramName) {
    	return getEnvOrDef(paramName, null, null);
    }

    private static Class<?> getMyClass(int level) {
        StackTraceElement ela[] = new Throwable().getStackTrace();
        if (ela != null && ela.length > (level + 1)) {
        	int i = 0;
        	for (; i < ela.length && ela[i].getClassName().equals(Globals.class.getName()); i++) {
        		// do nothing
			}
            try {
                Class<?> clazz = Class.forName(ela[level + i].getClassName());
                return clazz;
            } catch (Exception ex) {
            	ex.printStackTrace();
                throw new UnsupportedOperationException(
                        "The JVM '" + System.getProperty("java.vendor") +
                                " " + System.getProperty("java.version") +
                                "' does not return stack trace info.");
            }
        } else {
            UnsupportedOperationException nEx;
            try {
                nEx = new UnsupportedOperationException(
                        "The JVM '" + System.getProperty("java.vendor") +
                                " " + System.getProperty("java.version") +
                                "' does not return stack trace info.");
            } catch (Exception ex) {
                nEx = new UnsupportedOperationException(
                        "The JVM does not return stack trace info.");
            }
            throw nEx;
        }
    }

    /**
     * returns logger unique for each class, what this method is called from.
     * Provides caching for loggers for each class, e.g. each Logger´s instance is created only once.
     */
    public static Logger getLogger() {

        Class<?> myClass = getMyClass(1);                // additional level due call from Global class itself
        Logger logger = LOGGER_CACHE.get(myClass);
        if (logger == null) {
//            synchronized (Globals.class) {
                logger = LOGGER_CACHE.get(myClass);
                if (logger == null) {
                    logger = LoggerFactory.getLogger(myClass);
					LOGGER_CACHE.put(myClass, logger);
				}
//			}
		}
		return logger;
	}
    
    /** returns string which contains description of all objects in 'list' separated by comma. 
     * @param list list whose {@link EntityI#getEntityDescription()} method is called
     * @see #listToText(List, String)
     */
    public static String listToText(List<? extends DescriptionI> list) {
    	return listToText(list, ", ");
    }

    /** returns string which contains description of all objects in 'list' separated by 'separator'. 
     * @param list list whose {@link EntityI#getEntityDescription()} method is called
     * @param separator text that separe elements in returned string
     */
    public static String listToText(List<? extends DescriptionI> list, String separator) {
    	StringBuilder sb = new StringBuilder();
    	for (Iterator<? extends DescriptionI> iterator = list.iterator(); iterator.hasNext();) {
    		DescriptionI ido = iterator.next();
			if(sb.length() > 0) {
				sb.append(separator);
			}
			sb.append(ido.getEntityDescription());
		}
    	return sb.toString();
    }


    /** null - safe clone of object.
	 * Never throws {@link CloneNotSupportedException}, throws {@link ErrorException} with caused exception instead.
	 * @param <T>
	 * @param o
	 * @return clone of 'o' od null if 'o' is null
	 * @throws AppException with its cause set to original exception in case any error ocurred
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T o) {
		if(o == null) { return null; }
		try {
			Method met = o.getClass().getMethod("clone");
			if(!met.isAccessible()) {
				met.setAccessible(true);
			}
			return (T)met.invoke(o);
		} catch (Exception e) {
			if(e instanceof RuntimeException) {
				throw (RuntimeException)e;
			}
			throw new AppException(e);
		}
	}


//    /** returns value if field annotated by given annotation class
//	 * @param annot annotation class whose field named 'fldName' is searched for.
//	 * @param component object on which is field seached
//	 * @param fldName name of field. It is either directly name of field, or value of annotation's value 'annotSource' (if it is passed).
//	 * @param annotSource name of annotation value, which is used instead of field name. 
//	 * If it is passed, field with annotation's type having value 'fldName' is searched in 'component' instead of search field's name directly.
//	 * TODO: Not implemented yet
//	 * @return
//	 */
//	public static Object getAnnotatedValue(Class<? extends Annotation> annot, Object component, String fldName, String annotSource) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
