package net.fishear.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.fishear.exceptions.AppException;




public class 
	Classes 
{

    /**
     * returns the {@link Class}, from whose this method was called.
     * This is static equivalent for 'anObject.getClass()' method.
     *
     * @throws UnsupportedOperationException if JVM does not provide stack trace info
     */
    public static final Class<?> getMyClass() {
        return getMyClass(0);
    }
    
    public static String getPackage(Class<?> cl) {
    	String s = cl.getPackage().getName().trim();
    	if(s.endsWith(".")) {
    		s = s.substring(0, s.length() - 1);
    	}
    	return s;
    }

    /**
     * returns the package path, from whose this method was called.
     * Returned path has packages separated by dot, and it DOES NOT end with dot.
     * @throws UnsupportedOperationException if JVM does not provide stack trace info
     */
    public static final String getMyPackage() {
    	return getPackage(getMyClass());
    }

    public static StackTraceElement[] removeStartingItems(StackTraceElement[] sta, Class<?> startingClass) {

    	try {
	    	Class<?> clazz = startingClass;
	    	int i = 0;
			for(; i < sta.length ; i++) {
				if(!sta[i].getClassName().equals(clazz.getName())) {
					break;
				}
			}
			
			StackTraceElement[] rsta = new StackTraceElement[sta.length - i];
			int cnt = 0;
			for(; i < sta.length ; i++) {
				rsta[cnt++] = sta[i];
			}
			return rsta;
    	} catch (Exception ex) {
    		return sta;
    	}
    }
    
    /**
     * @param level aditional level, used for internal calls from this class.
     * @see #getMyClass()
     */
    public static Class<?> getMyClass(int level) {
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

	public static String getShortClassName(Object o) {
		return getShortClassName(o.getClass());
	}

	/** returns name of class without package
	 */
	public static String getShortClassName(Class<?> cl) {
		String cln = cl.getName();
		return cln.substring(cln.lastIndexOf('.') + 1);
	}
	
	/** returns path to classpath resource whose name is based on name of class 'cl'. 
	 * To this path is appended 'suffix'.
	 */
	public static String getResourcePathForClass(Class<?> cl, String suffix) {
		String s = getPackage(cl).replace('.', '/')+"/"+getShortClassName(cl)+suffix;
		return s;
	}

	/** returns path to classpath resource whose path is based on package of class 'cl', which is appended by 'filename'. 
	 */
	public static String getResourcePathForPackage(Class<?> cl, String filename) {
		String s = getPackage(cl).replace('.', '/')+"/"+filename;
		return s;
	}

	/** Converts object to serialized form, returns it as array of bytes.
	 */
	public static byte[] serialize(Object o) {
		if(o == null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.flush();
			return baos.toByteArray();
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	/** make object drom byte array. 
	 * Expects the 'ba' is serialized object, returns object created from it.
	 */
	public static Object deserialize(byte[] ba) {
		if(ba == null || ba.length == 0) {
			return null;
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(ba));
			return ois.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex);
		}
	}

	public static String serializeToXml(Object o) {
		if(o == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder xenc = new XMLEncoder(baos);
		xenc.writeObject(o);
		xenc.close();
		return baos.toString();
	}

	public static Object deserializeFromXml(String s) {
		if(s == null) {
			return null;
		}
		XMLDecoder xdec = new XMLDecoder(new ByteArrayInputStream(s.getBytes()));
		return xdec.readObject();
	}

	public static String getParentPackage(String pkg) {
		if(pkg != null) {
			int ii = pkg.replace('/', '.').lastIndexOf('.');
			if(ii > 0) {
				pkg = pkg.substring(0, ii);
			}
		}
		return pkg;
	}

	/** search over fields which ones implement given or extend given class.
	 * @param clazz class which is scanned for fields
	 * @param superclassOrInterface the superclass ot interface to search for
	 * @param withAnnotations list of annotations that given fiels has ti have (at least one of them)
	 * @return first ocurrance of field, or null if not found
	 */
	public static Field findImplementationOf(Class<?> clazz, Class<?> superclassOrInterface, Class<? extends Annotation>... withAnnotations) {
		List<Field> list = findImplementationOf_(clazz, superclassOrInterface, withAnnotations);
		if(list.size() > 0) {
			return (Field) list.get(0);
		}
		return null;
	}

	private static List<Field> findImplementationOf_(Class<?> clazz, Class<?> superclassOrInterface, Class<? extends Annotation>... withAnnotations) {
		List<String> added = new ArrayList<String>();
		List<Field> list = new ArrayList<Field>();
		Field[] fa = clazz.getDeclaredFields();
		for (int i = 0; i < fa.length; i++) {
			if(!added.contains(fa[i].getName())) {
				Class<?> facl = fa[i].getType();
				if(superclassOrInterface.isAssignableFrom(facl)) {
					if(withAnnotations != null) {
						for (Class<? extends Annotation> ancl :  withAnnotations) {
							if(fa[i].getAnnotation(ancl) != null) {
								added.add(fa[i].getName());
								list.add(fa[i]);
							}
						}
					} else {
						added.add(fa[i].getName());
						list.add(fa[i]);
					}
				}
			}
		}
		return list;
	}

}
