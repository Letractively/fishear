package net.fishear.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fishear.Interfaces.IdI;
import net.fishear.exceptions.AppException;



public class 
	EntityUtils
{
	private static final Class<Double> doubleClass = Double.TYPE;
	private static final Class<Float> floatClass = Float.TYPE;
	private static final Class<Long> longClass = Long.TYPE;
	private static final Class<Integer> integerClass = Integer.TYPE;
	private static final Class<Short> shortClass = Short.TYPE;
	private static final Class<Byte> byteClass = Byte.TYPE;
	private static final Class<Boolean> booleanClass = Boolean.TYPE;
	private static final Class<Character> charClass = Character.TYPE;

	public static final Object[] EOA = new Object[0];
	public static final Class<?> TOP_LEVEL_CLASS = Object.class;
	
	private static Object NO_OBJECT = new Object();

	private static final Hashtable<Class<?>, Hashtable<String, FldDesc>> cache = new Hashtable<Class<?>, Hashtable<String, FldDesc>>();
	
	private static Logger log = LoggerFactory.getLogger(EntityUtils.class);
	private static boolean jpaAvailable = true;
	
	public static enum FillFlags {

		/**
		 * for 'flags' in {@link #fillDestination(Object, Object, int)}. 
		 * If set, only null values from target will be filled out. 
		 */
		FILL_EMPTY_ONLY,
		
		/**
		 * for 'flags' in {@link #fillDestination(Object, Object, int)}. 
		 * If set, all values in dest (except IDs) will be overwritten.
		 */
		FILL_ALL, 
		
		/**
		 * Overwrite fields that are null in source entity.
		 * By default, not nulls in dst are preserved is src is null.
		 */
		OVERWRITE_BY_NULLS
	}
	
	/**
	 * @author ffyxrr
	 * flags that modify #listDifferencies method behaviour 
	 */
	public static enum DiffFlags {

		/**
		 * If set, transient fields/getters are involved into to comparison. 
		 * Ztansient values are ommitted by default.
		 */
		COMPARE_TRANSIENT
		
	}
	
	

	/** return true if both 'e1' and 'e2' are null, or if both id's of 'e1' and 'e2' are null, or if id's of 'e1' and 'e2' are equals.
	 * In other cases, returns false.
	 */
	public static boolean equalsId(IdI<?> e1, IdI<?> e2) {
		if(e1 == null || e2 == null) {
			return e1 == e2;
		}
		Object id1 = e1.getId();
		Object id2 = e2.getId();
		if(id1 == null || id2 == null) {
			return id1 == id2;
		}
		return id1.equals(id2);
	}

	/** returns ID of object 'entity'.
	 * If the 'entity' is instance of IdI, calls it's getId() method.
	 * Otherwise, try call getId() (if such method exists) or try seek for field or getter annotated as {@link Id}.
	 * Throws runtime exception in case ID field could not be found. In case 'entity' is null, returns null.
	 * The ID method must return {@link Long}, otherwise the exception is thrown.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getId(Object entity) {
		if(entity == null) {
			return null;
		}
		if(entity instanceof IdI) {
			return (T) ((IdI<?>)entity).getId();
		}
		if(jpaAvailable) {
			try {
				List<Field> anof  = Annotations.getAnnotatedFields(entity.getClass(), Id.class);
				List<Method> anom = Annotations.getAnnotatedMethods(entity.getClass(), Id.class);
				if((anof.size() + anom.size()) > 1) {
					throw new IllegalStateException(String.format("Too many objects annotated with '%s' annotation. Expected 1, found %s", Id.class.getName(), anof.size()));
				}
				try {
					if (anof.size() > 0) {
						return (T) getGetter(entity.getClass(), anof.get(0).getName()).invoke(entity);
					}
					if (anom.size() > 0) {
						return (T) anom.get(0).invoke(entity);
					}
				} catch (Exception ex) {
					throw Exceptions.runtime(ex);
				}
			} catch (NoClassDefFoundError ex) {
				jpaAvailable = false;
			}
		}
		return null;
	}
	
	/** returns method like the {@link Class#getMethod(String, Class...)} does, or null in case such method does not exist. 
	 * Never throws exception.
	 * @param classOrObject if it is class, returns the method directly from it. Otherwise, calls classOrObject,getClass() to search the method for.
	 * @param metName method name
	 * @param paramTypes param types
	 * @return method, or null
	 */
	public static Method getMethod(Object classOrObject, String metName, Class<?>... parameterTypes) {
		try {
			Class<?> clazz;
			if(classOrObject instanceof Class) {
				clazz = (Class<?>)classOrObject;
			} else {
				clazz = classOrObject.getClass();
			}
			return clazz.getMethod(metName, parameterTypes);
		} catch (Exception ex) {}
		return null;
	}
	
	
	/**
	 * creates getter/setter name from field name.
	 * 
	 * @param fldName field name (without get/set prefix, first character case is not important)
	 * @param prefix the "get" or "set" 
	 * @return getter / setter name
	 */
	public static String toMethodName(String fldName, String prefix) {
		return prefix.concat(fldName.substring(0, 1).toUpperCase().concat(fldName.substring(1)));
	}

	/**
	 * creates field name from getter/setter name.
	 * 
	 * @param getterSetterName getter or setter name (starting with get/set prefix)
	 * @return field name always starting with lower case
	 */
	public static String toFieldName(String getterSetterName) {
		String fldName = getterSetterName.substring(3);
		return fldName.substring(0, 1).toLowerCase().concat(fldName.substring(1));
	}

	/** search for public method in class of 'source' and call it.
	 * @param source the source object
	 * @param metName method name
	 * @param params pairs of Class + Object. Class is param type, Object is param value.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T callMethod(Object source, String metName, T dftVal, Object... params) {
		if(params.length % 2 != 0) {
			throw new IllegalArgumentException("params size has to even");
		}
		if(source == null) {
			return dftVal;
		}
		Class<?>[] cla = new Class[params.length / 2];
		Object[] args = new Class[params.length / 2];
		int cnt = 0;
		for (int i = 0; i < params.length; i += 2) {
			cla[cnt] = (Class<?>) params[i];
			args[cnt] = params[i + 1];
			cnt++;
		}
		Method met = getMethod(source, metName, cla);
		if(met != null) {
			try {
				return (T) met.invoke(source, args);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return dftVal;
	}
	
	/**
	 * compare two entities using all available "getters". Returns true if both
	 * entities are identical classes and the same field in each entity has the
	 * same values. If getter method for field exists (with the same return value
	 * as field), it is used to obtain fiels's value. Otherwise, value is taken
	 * directly from field. If both fields (from e1 and e2) are null, it is OK.
	 * @param e1 first object
	 * @param e2 second object
	 * @param flags flags that correct behavior
	 */
	public static boolean equals(Object e1, Object e2, DiffFlags... flags) {
		return equals(e1, e2, Integer.MAX_VALUE, flags);
	}

	/** like {@link #equals(Object, Object, DiffFlags...)}, but maximum depth for inner fields is limited to "maxDepth". 
	 * @param e1 first object
	 * @param e2 second object
	 * @param maxDepth max depth 
	 * @param flags flags that correct behavior
	 * @return true if objects are equals, false otherwise 
	 */
	public static boolean equals(Object e1, Object e2, int maxDepth, DiffFlags... flags) {
		
		boolean compareTransient = false;

		for(DiffFlags fl : flags) {
			switch(fl) {
			case COMPARE_TRANSIENT:
				compareTransient = true;
				continue;
			}
		}
		return equalsInternal(e1, e2, new HashSet<String>(), maxDepth, compareTransient);
	}

	public static boolean equals(Object e1, Object e2, Set<String> done, int depth) {
		return equalsInternal(e1, e2, done, depth, false);
	}

	private static boolean equalsInternal(Object e1, Object e2, Set<String> done, int depth, boolean compareTransient) {

		Class<?> clazz = e1.getClass();
		log.debug("equalsInternal: Comparison started at depth level {} for entity class '{}'", depth, clazz);
		if(depth < 0) {
			log.trace("equalsInternal: Level is < 0 => assuming equals");
			return true;
		}
		if (e1 == e2) {
			log.trace("equalsInternal: Entities are the same. No difference.");
			return true;
		}
		if (clazz != e2.getClass()) {
			log.trace("equalsInternal: Entities are instanes of different classes.");
			return false;
		}
		String cln = clazz.getName();
		if(cln.startsWith("java.") || cln.startsWith("javax.")) {
			log.trace("equalsInternal: Java native class {}; compare direct", clazz);
			return e1 == null ? e2 == null : e1.equals(e2);
		}
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				boolean transientFld = false;
				Field fld = fields[i];
				String fldn = fld.getName();
				if(Modifier.isStatic(fld.getModifiers())) {
					log.trace("Static field '{}', ignored", fldn);
					continue;
				}
				if (compareTransient ? false : isTransient(fld)) {
					log.trace("Transient field '{}', will be ignored at the next step", fldn);
					transientFld = true;
				}

				String metn = "get" + fldn.substring(0, 1).toUpperCase() + fldn.substring(1);
				Method m;
				try {
					Object v1, v2;
					try {
						m = clazz.getMethod(metn);
						if(Modifier.isStatic(m.getModifiers())) {
							log.trace("Static method '{}', ignored", fldn);
							continue;
						}
						if(done.contains(metn)) {
							log.trace("Getter with name '{}' already proccessed, ignore", metn);
							continue;
						}
						if (compareTransient ? false : isTransient(m) || !Modifier.isPublic(m.getModifiers()) || transientFld) {
							if(log.isTraceEnabled()) { log.trace("Transient getter '{}' or field '{}', ignore", metn, fldn); }
							continue;
						}
						done.add(metn);
					} catch (NoSuchMethodException ex) { // exceptions can be OK here if getter does not exists
						continue;
					}
					v1 = m.invoke(e1);
					v2 = m.invoke(e2);
					if(v1 == v2) {
						log.trace("Values for field '{}' are the same => equals", fldn);
						continue;
					} else if (v1 == null || v2 == null) {
						log.trace("One value for field '{}' is null, the other not => not equals", fldn);
						return false;
					} else if (!equalsInternal(v1, v2, done, depth - 1, compareTransient)) {
						log.trace("Deep equals for field '{}' returned false => NOT equals", fldn);
						return false;
					}
				} catch (Exception e) {
					log.error("Cannot get value for field: '{}'\nValue type: '{}'", fld, fld.getType());
					throw new AppException(e);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return true;
	}
	
	public static class Property {

		private String name;
		private Object value1;
		private Object value2;
		private Class<?> valueType;

		public Property(String fldn, Object v1, Object v2, Class<?> type) {
			this.value1 = v1;
			this.value2 = v2;
			this.name = fldn;
			this.valueType = type;
		}
		
		public boolean equals(Object o) {
			if(o == null || !(o instanceof Property)) {
				return false;
			}
			Property p = (Property) o;
			return eq(name, p.name) && eq(value1, p.value1) && eq(value2, p.value2);
		}
		
		private boolean eq(Object o1, Object o2) {
			return o1 == null ? o2 == null : o1.equals(o2);
		}

		public String toString() {
			return "" + name + ": "+ value1 + "<=>" + value2;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the value1
		 */
		public Object getValue1() {
			return value1;
		}

		/**
		 * @param value1 the value1 to set
		 */
		public void setValue1(Object value1) {
			this.value1 = value1;
		}

		/**
		 * @return the value2
		 */
		public Object getValue2() {
			return value2;
		}

		/**
		 * @param value2 the value2 to set
		 */
		public void setValue2(Object value2) {
			this.value2 = value2;
		}

		/**
		 * @return the valueType
		 */
		public Class<?> getValueType() {
			return valueType;
		}

		/**
		 * @param valueType the valueType to set
		 */
		public void setValueType(Class<?> valueType) {
			this.valueType = valueType;
		}
		
	}
	
	/** compares two entities and returns list of properties (aka fields) that are different.
	 * Each compared property must exist as field and must have corresponding getter, utherwise it is ignored diring comparison.
	 * @param e1 first (source) entity
	 * @param e2 second (target) entity
	 * @param flags 
	 * @return
	 */
	public static List<Property> listDifferencies(Object e1, Object e2, DiffFlags... flags) {

		boolean compareTransient = false;
		
		for(DiffFlags fl : flags) {
			switch(fl) {
			case COMPARE_TRANSIENT:
				compareTransient = true;
				continue;
			}
		}
		
		if(e1 == null || e2 == null) {
			throw new IllegalStateException(String.format("Both objects to compare must be not null (e1=%s, e2=%s)", e1, e2));
		}

		Set<String> done = new HashSet<String>();

		List<Property> list = new ArrayList<EntityUtils.Property>();
		Class<?> clazz = e1.getClass();
		log.debug("listDifferencies: Comparison started");
		if (e1 == e2) {
			log.trace("listDifferencies: Entities are the same. No difference.");
			return list;
		}
		if (clazz != e2.getClass()) {
			throw new IllegalArgumentException(String.format("Both entities nust be instance of the same class: %s <=> %s", clazz, e2.getClass()));
		}
		String cln = clazz.getName();
		if(cln.startsWith("java.") || cln.startsWith("javax.")) {
			throw new IllegalArgumentException("java native classes cannot be compared");
		}
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				boolean transientFld = false;
				Field fld = fields[i];
				String fldn = fld.getName();
				log.trace("Field {}", fldn);
				if(Modifier.isStatic(fld.getModifiers())) {
					log.trace("Static field '{}', ignored", fldn);
					continue;
				}
				if (compareTransient ? false : isTransient(fld)) {
					log.trace("Transient field {}, will be ignored at the next step", fldn);
					transientFld = true;
				}
				String metn = "get" + fldn.substring(0, 1).toUpperCase() + fldn.substring(1);
				Method m;
				try {
					Object v1, v2;
					try {
						m = clazz.getMethod(metn);
						if(Modifier.isStatic(fld.getModifiers())) {
							log.trace("Static method '{}', ignored", metn);
							continue;
						}
						if(done.contains(metn)) {
							log.trace("Getter with name {} already proccessed, ignore", metn);
							continue;
						}
						if (compareTransient ? false : isTransient(m) || !Modifier.isPublic(m.getModifiers()) || transientFld) {
							if(log.isTraceEnabled()) { log.trace("Transient getter {} or field {}, ignore", metn, fldn); }
							continue;
						}
						done.add(metn);
					} catch (NoSuchMethodException ex) { // exceptions can be OK here if getter does not exists
						log.trace("Getter {} does not exist, ignore", metn);
						continue;
					}
					v1 = m.invoke(e1);
					v2 = m.invoke(e2);
					if(v1 == v2) {
						log.trace("Values for field {} are the same, ignore", fldn);
						continue;
					} else if (v1 == null || v2 == null || (e1 instanceof IdI<?> ? equalsId((IdI<?>)e1, (IdI<?>)e2) : !v1.equals(v2))) {
						if(log.isTraceEnabled()) {
							log.trace(String.format("Added difference for field %s, v1={}, v2={}", fldn), v1, v2);
						}
						list.add(new Property(fldn, v2, v1, fld.getType()));
					}
				} catch (Exception e) {
					log.error("Cannot get value for field: {}\nValue type: {}", fld, fld.getType());
					throw new AppException(e);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return list;
	}
	
	/** provides list of all values form entity as list of differencies.
	 * @param e rhe entity
	 * @param isTarget if true, values are filled to target value of list item. Default is source value.
	 * @param flags the flags
	 * @return list of all values
	 */
	public static List<Property> fillDifferencies(Object e, boolean isTarget, DiffFlags... flags) {

		boolean compareTransient = false;
		
		for(DiffFlags fl : flags) {
			switch(fl) {
			case COMPARE_TRANSIENT:
				compareTransient = true;
				continue;
			}
		}
		
		Set<String> done = new HashSet<String>();

		List<Property> list = new ArrayList<EntityUtils.Property>();
		Class<?> clazz = e.getClass();
		log.debug("listDifferencies: Comparison started");

		String cln = clazz.getName();
		if(cln.startsWith("java.") || cln.startsWith("javax.")) {
			throw new IllegalArgumentException("java native classes cannot be compared");
		}
		do {
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				boolean transientFld = false;
				Field fld = fields[i];
				String fldn = fld.getName();
				log.trace("Field {}", fldn);
				if(Modifier.isStatic(fld.getModifiers())) {
					log.trace("Static field '{}', ignored", fldn);
					continue;
				}
				if (compareTransient ? false : isTransient(fld)) {
					log.trace("Transient field {}, will be ignored at the next step", fldn);
					transientFld = true;
				}
				String metn = "get" + fldn.substring(0, 1).toUpperCase() + fldn.substring(1);
				Method m;
				try {
					try {
						m = clazz.getMethod(metn);
						if(Modifier.isStatic(fld.getModifiers())) {
							log.trace("Static method '{}', ignored", metn);
							continue;
						}
						if(done.contains(metn)) {
							log.trace("Getter with name {} already proccessed, ignore", metn);
							continue;
						}
						if (compareTransient ? false : isTransient(m) || !Modifier.isPublic(m.getModifiers()) || transientFld) {
							if(log.isTraceEnabled()) { log.trace("Transient getter {} or field {}, ignore", metn, fldn); }
							continue;
						}
						done.add(metn);
					} catch (NoSuchMethodException ex) { // exceptions can be OK here if getter does not exists
						log.trace("Getter {} does not exist, ignore", metn);
						continue;
					}
					if(isTarget) {
						log.trace("Target values for field {} setted", fldn);
						list.add(new Property(fldn, null, m.invoke(e), fld.getType()));
					} else {
						log.trace("Source values for field {} setted", fldn);
						list.add(new Property(fldn, m.invoke(e), null, fld.getType()));
					}
				} catch (Exception ex) {
					log.error("Cannot get value for field: {}\nValue type: {}", fld, fld.getType());
					throw new AppException(ex);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return list;
	}
	
	private static boolean isAnnotation(Object fldOrMethod, Class<? extends Annotation> annot) {
		if(fldOrMethod == null) {
			return false;
		}
		if(fldOrMethod instanceof Field) {
			return ((Field)fldOrMethod).getAnnotation(annot) != null;
		} else if(fldOrMethod instanceof Method) {
			return ((Method)fldOrMethod).getAnnotation(annot) != null;
		} else {
			throw new IllegalArgumentException(String.format("'fldOrMethod' must be field or method, but '%s' is not", fldOrMethod));
		}
	}

	/**
	 * solves situation when JPA libraries are not available at runtime.
	 * 
	 * @param fldOrMethod required object
	 * @return true if field or method is annotated by {@link Transient}, false otherwise.
	 */
	private static boolean isTransient(Object fldOrMethod) {
		if(jpaAvailable) {
			try {
				return isAnnotation(fldOrMethod, Transient.class);
			} catch(NoClassDefFoundError ex) {
				jpaAvailable = false;
			}
		}
		return false;
	}

	/** returns true if property or its getter is annotated ad {@link Transient}
	 * @param entityClass the class
	 * @param propertyName proiperty name
	 * @return
	 */
	public static boolean isTransientProperty(Class<?> entityClass, String propertyName) {
		FldDesc fd = getFldDesc(entityClass, propertyName);
		return isTransient(fd.getGetter()) || isTransient(fd.getField());
	}

	/**
	 * solves situation when JPA libraries are not available at runtime.
	 * 
	 * @param fldOrMethod required object
	 * @return true if field or method is annotated by {@link Id}, false otherwise.
	 */
	private static boolean isId(Object fldOrMethod) {
		if(jpaAvailable) {
			try {
				return isAnnotation(fldOrMethod, Id.class);
			} catch(NoClassDefFoundError ex) {
				jpaAvailable = false;
			}
		}
		return false;
	}

	private static class FldDesc
	{

		private final String name; // normalized name (begins with uppercase,
									// without 'get' or 'set' at begin
		private final Class<?> clazz;

		private boolean hasGetter = true;
		private boolean hasSetter = true;
		private boolean hasField;

		private Field fld;
		private Method getter;
		private Method setter;

		private FldDesc(String name, Class<?> clazz) {
			this.name = name;
			this.clazz = clazz;
		}

		private /*synchronized*/ void initField() {
			if (this.fld == null && this.hasField) {
				try {
					this.fld = clazz.getField(name.substring(0, 1).toLowerCase().concat(name.substring(1)));
					if (!this.fld.isAccessible()) {
						this.fld.setAccessible(true);
					}
				} catch (RuntimeException ex) {
					throw ex;
				} catch (Exception ex) {
					this.hasField = false;
				}
			}
		}

		private /*synchronized*/ void initGetter() {
			if (this.getter == null && this.hasGetter) {
				try {
					this.getter = clazz.getMethod("get".concat(name));
					if (!this.getter.isAccessible()) {
						this.getter.setAccessible(true);
					}
				} catch (RuntimeException ex) {
					throw ex;
				} catch (Exception ex) {
					try {
						this.getter = clazz.getMethod("is".concat(name));
						if (!this.getter.isAccessible()) {
							this.getter.setAccessible(true);
						}
					} catch (RuntimeException e2) {
						throw e2;
					} catch (Exception e2) {
						this.hasGetter = false;
					}
				}
			}
		}

		private boolean isOk(Method met) {
			if (met != null) {
				Class<?> type = met.getReturnType();
				return Void.TYPE != type && Void.class != type;
			}
			return false;
		}

		private /*synchronized*/ void initSetter() {
			if (this.setter == null && this.hasSetter) {
				// none of getter or field is initialized yet => I must
				// initialize at leat one of then
				if (this.getter == null && this.fld == null) {
					initGetter();
					if (!isOk(this.getter)) {
						// getter does not exist or it does not return any value
						// (is void)
						initField();
						if (this.fld == null) {
							// none of getter or field is known => I cannot
							// determine setter type => return
							this.hasSetter = false;
							return;
						}
					}
				}
				Class<?> setterType;
				if (!isOk(this.getter)) {
					setterType = this.getter.getReturnType();
					if (!this.setter.isAccessible()) {
						this.setter.setAccessible(true);
					}
				} else {
					setterType = this.fld.getType();
				}
				try {
					this.setter = clazz.getMethod("set".concat(name), setterType);
				} catch (RuntimeException ex) {
					throw ex;
				} catch (Exception ex) {
					this.hasSetter = false;
				}
			}
		}

		public Method getGetter() {
			if (this.getter == null && this.hasGetter) {
				initGetter();
			}
			return this.getter;
		}

		public Method getSetter() {
			if (this.setter == null && this.hasSetter) {
				initSetter();
			}
			return this.getter;
		}

		public Field getField() {
			if (this.fld == null && this.hasField) {
				initField();
			}
			return this.fld;
		}
	}
	
	/**
	 * calls getter method for the given property and returns its return value.
	 * If any error ocurred, returns dftVal.
	 * 
	 * @param object the object that the property is searched for
	 * @param propertyName the property name
	 * @return In case property not found, returns null. Otherwise returns value of getter or dftVal.
	 */
	public static <T> Object getValue(T object, String propertyName, T dftVal) {
		int ii;
		Object reto = object;
		while((ii = propertyName.indexOf('.')) > 0) {
			reto = getValue_(reto, propertyName.substring(0, ii), NO_OBJECT);
			if(reto == null) {
				return null;
			}
			if(reto == NO_OBJECT) {
				return dftVal;
			}
			propertyName = propertyName.substring(ii + 1);
		}
		return getValue_(reto, propertyName, dftVal);
	}
	
	public static boolean setValue(Object object, String propertyName, Object value) {
		if(value == null) {
			throw new IllegalArgumentException("Cannot determine setzter type for null value. Use call with explicit type.");
		}
		return setValue(object, propertyName, value, value.getClass());
	}

	public static boolean setValue(Object object, String propertyName, Object value, Class<?> type) {
		int ii;
		Object reto = object;
		while((ii = propertyName.indexOf('.')) > 0) {
			reto = getValue_(reto, propertyName.substring(0, ii), NO_OBJECT);
			if(reto == null) {
				return false;
			}
			if(reto == NO_OBJECT) {
				return false;
			}
			propertyName = propertyName.substring(ii + 1);
		}
		Class<?> clazz = reto.getClass();
		String setterName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		try {
			clazz.getMethod(setterName, type).invoke(reto, value);
			return true;
		} catch(NoSuchMethodException ex) {
			return false;
		} catch(Exception ex) {
			log.error("Cannot set value", ex);
		}
		return false;
	}
	
	

	/**
	 * gets value obtained by the getter for given property, 
	 * 
	 * @param object
	 * @param name
	 * @param dft
	 * @return
	 */
	private static <T> Object getValue_(T object, String name, T dft) {

		if(object == null) {
			return dft;
		}
		
		Class<?> clazz = object.getClass();

		String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		try {
			Method met = null;
			met = clazz.getMethod(getterName);
			return met.invoke(object);
		} catch(NoSuchMethodException ex) {
			return dft;
		} catch(Exception ex) {
			if(log.isInfoEnabled()) {
				log.debug("Cannot get value {} on object {}: " + ex);
			}
		}
		return dft;
	}

	/**
	 * returns name without 'get' or 'set' prefix (if any). Returned bame begins
	 * with uppercase character.
	 */
	private static String normalize(String name) {
		if (name != null && (name = name.trim()).length() > 0) {
			if (name.length() > 3) {
				// is it getter or setter name ?
				if ((name.startsWith("get") || name.startsWith("set")) && Character.isUpperCase(name.charAt(3))) {
					name = name.substring(3);
				}
			}
			return name.substring(0, 1).toUpperCase().concat(name.substring(1));
		}
		return "";
	}

	private static final FldDesc getFldDesc(Class<?> clazz, String name) {
		Hashtable<String, FldDesc> ht;
		String s = normalize(name);

		if ((ht = cache.get(clazz)) == null) {
			if ((ht = cache.get(clazz)) == null) {
				ht = new Hashtable<String, FldDesc>();
				cache.put(clazz, ht);
			}
		}
		FldDesc fd;
		if ((fd = ht.get(s)) == null) {
			if ((fd = ht.get(s)) == null) {
				fd = new FldDesc(s, clazz);
				ht.put(s, fd);
			}
		}
		return fd;
	}

	/** returns the getter method for given fiels name.
	 * Case of first letter of 'name' does not matter.
	 */
	public static Method getGetter(Class<?> clazz, String name) {

		FldDesc fd = getFldDesc(clazz, name);
		return fd.getGetter();
	}

	public static Method getSetter(Class<?> clazz, String name) {

		FldDesc fd = getFldDesc(clazz, name);
		return fd.getSetter();
	}

	public static Field getField(Class<?> clazz, String name) {

		FldDesc fd = getFldDesc(clazz, name);
		return fd.getField();
	}

	/** returns ID of entity, or null id entity is null.
	 * It is helpful in case inner intity - I cannot care about entity is nul or not.
	 */
	public static Object getId(IdI<?> entity) {
		return entity == null ? null : entity.getId();
	}

	@SuppressWarnings("unused")
	private static Class<?> getUpperClass(Class<?> primitive) {
		if(primitive == byteClass) {
			return Byte.class;
		} else if(primitive == shortClass) {
			return Short.class;
		} else if(primitive == shortClass) {
			return Short.class;
		} else if(primitive == integerClass) {
			return Integer.class;
		} else if(primitive == longClass) {
			return Long.class;
		} else if(primitive == doubleClass) {
			return Double.class;
		} else if(primitive == floatClass) {
			return Float.class;
		} else if(primitive == booleanClass) {
			return Boolean.class;
		} else if(primitive == charClass) {
			return Character.class;
		} else {
			throw new IllegalArgumentException("Class must be primitive, but the '" + primitive + "'' does not");
		}
	}

	/** takes all non-empty fields from srcE and fill them's values to dstE.
	 * Both entities must be instance of the same class. Both getter and setter must exist, otherwise exception is thrown. 
	 * Methods annotated by {@link Transient} annotation and getId() methods are ignored.
	 * Methods annotated by {@link Id} annotation are ignored too.
	 * If getter returns null, setter is not called. Also if values from both instances are the same, setter is not called.
	 * @param srcE the source entity
	 * @param dstE the destination
	 * @param flags flags controlling copying
	 * @return the destination object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fillDestination(Object srcE, Object dstE, FillFlags... flags) {
		
		Class<?> clazz = srcE.getClass();
		if( !srcE.getClass().isAssignableFrom(dstE.getClass())) {
			throw new IllegalArgumentException(String.format("The target entitiy must be assignable from the source. Src: %s, Dst: %s", srcE.getClass().getName(), dstE.getClass().getName()));
		}
		String getterName = "(unknown)";
		try {
			boolean fillEmptyOnly = false;
			boolean fillAll = false;
			boolean fillSourceNulls = false;
			for(FillFlags fl : flags) {
				if(fl == FillFlags.FILL_ALL) {
					fillAll = true;
				} else if(fl == FillFlags.FILL_EMPTY_ONLY) {
					fillEmptyOnly = true;
				} else if(fl == FillFlags.OVERWRITE_BY_NULLS) {
					fillSourceNulls = true;
				}
			}

			while(clazz != TOP_LEVEL_CLASS) {
				getterName = "(unknown)";
				Method[] met = clazz.getMethods();
				for (int i = 0; i < met.length; i++) {
					getterName = "(unknown)";
					Method m = met[i];
					getterName = m.getName();
					if(getterName.startsWith("get")) {
						if(isId(m) || getterName.equalsIgnoreCase("getId")) {
							continue;
						}
						Class<?> vtyp = m.getReturnType();
						try {
							Method setter = clazz.getMethod("set"+getterName.substring(3), vtyp);
							if(
									!isTransient(m) && 
									!isTransient(setter) &&
									Modifier.isPublic(m.getModifiers()) && Modifier.isPublic(setter.getModifiers())
							) {
								Object valSrc = m.invoke(srcE);
								if(valSrc != null) {
									Object valDst = m.invoke(dstE);
									if(fillAll || valDst == null || (!fillEmptyOnly && !valSrc.equals(valDst))) {
										setter.invoke(dstE, valSrc);
									}
								} else if(fillSourceNulls && m.invoke(dstE) != null) {
									setter.invoke(dstE, valSrc);
								}
								
							}
						} catch(NoSuchMethodException ex) {
							// that is OK
						}
					}
				}
				clazz = clazz.getSuperclass();
			}
		} catch (Exception ex) {
			log.error("Exception during proccessing getter '{}'", getterName);
			throw new IllegalStateException(ex);
		}
		return (T) dstE;
	}

	/** returns true if the entity is considererd as new (non-saved to persistent location).
	 * @param entity the entity
	 * @return true or false
	 */
	public static boolean isNew(Object entity) {
		Object id = getId(entity);
		if(id == null) {
			return true;
		}
		if(id instanceof Number) {
			return ((Number)id).longValue() == 0;
		} else if(id instanceof CharSequence) {
			return ((CharSequence)id).length() == 0;
		}
		return false;
	}

	public static final <T, S> T convertType(S source, Class<T> target) {
		return Coercions.convertType(source, target);
	}

	public static String toString(Object entity, String... attrNames) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < attrNames.length; i++) {
			String attr = Texts.tos(attrNames[i]);
			if((attr.startsWith("\"") && attr.endsWith("\"")) || (attr.startsWith("\'") && attr.endsWith("\'"))) {
				sb.append(attr.substring(1, attr.length() - 1));
			} else {
				sb.append(getAttribute(attr, entity));
			}
		}
		return sb.toString();
	}

	/**
	 * gets attribute from bean instance and returns it as proper type.
	 * Throws runtime exception in case any other exception occurs.
	 * 
	 * @param attrName
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(String attrName, Object entity) {
		try {
			return (T) BeanUtils.getProperty(entity, attrName);
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}
	
	/**
	 * gets String representation of attribute value from bean instance and returns it as proper type.
	 * In case internal exception occurs, returns 'defaultValue'. Logs exception only if logLevel is set to DEBUG.
	 * Returns String representation of value. If do you need original value, use {@link #getRawValue(String, Object, Object)}
	 * 
	 * @param attrName
	 * @param entity
	 * @param defaultValue
	 * @return property value as string, or default value in case exception occurs.
	 */
	public static String getValue(String attrName, Object entity, String defaultValue) {
		try {
			return BeanUtils.getProperty(entity, attrName);
		} catch (Exception ex) {
			log.debug("exception during value getting", ex);
			return defaultValue;
		}
	}

	/**
	 * gets attribute value from bean instance and returns it as proper type.
	 * In case internal exception occurs, returns 'defaultValue'. Logs exception only if logLevel is set to DEBUG.
	 * 
	 * @param attrName
	 * @param entity
	 * @param defaultValue
	 * @return property value as string, or default value in case exception occurs.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRawValue(String attrName, Object entity, T defaultValue) {
		try {
			if(entity == null) {
				return null;
			} else {
				return (T) BeanUtilsBean.getInstance().getPropertyUtils().getNestedProperty(entity, attrName);
			}
		} catch (Exception ex) {
			log.debug("exception during value getting", ex);
			return defaultValue;
		}
	}

	/**
	 * returns descriptor for given property 
	 */
	static PropertyDescriptor getPd(PropertyDescriptor[] pda, String name) {
		for(PropertyDescriptor pd : pda) {
			if(pd.getName().equals(name)) {
				return pd;
			}
		}
		return null;
	}
	
	/** returns comparator that compares certain property value from given entity.
	 * The property value must be implementation of {@link Comparable} interface, otherwise exception is thrown.
	 * Nested properties are allowed. In such case tip values are compared (they must implement comparable), 
	 * the intermediate values need not. If some of intermediate values are null, comparison ends and such value is treated as "less".
	 * <br />
	 * <b>Ecample</b>
	 * We have this entity value: Department.employee.name
	 * <ul>
	 * <li>: Department1.employeee == null && Depatrment2.employee != null => result is -1</li>
	 * <li>: Department1.employeee == null && Depatrment2.employee == null => result is 0</li>
	 * <li>: Department1.employeee != null && Depatrment2.employee == null => result is 1</li>
	 * <li>: Department1.employeee != null && Depatrment2.employee != null => 'employee.name' is compared and returned</li>
	 * </ul>
	 * @param entityType entity class
	 * @param propertyName name of property that will be compared. Nested properties (separated by dot) are allowed.
	 * @return the {@link EntityComparator} 
	 * @throws IllegalArgumentException in case any error occurs
	 */
	public static <T> EntityComparator<T> getComparator(Class<T> entityType, String propertyName) {

		final Method[] ma;
		
		try {

			String[] as = propertyName.split("\\.");

			if(as.length == 0) {
				as = new String[] {propertyName};
			}
			

			Class<?> type = entityType;

			ma = new Method[as.length];
			
			
			PropertyUtilsBean pu = BeanUtilsBean.getInstance().getPropertyUtils();

			int i = 0;
			for(String s : as) {
				PropertyDescriptor pd = getPd(pu.getPropertyDescriptors(type), s);

				ma[i] = pu.getReadMethod(pd);
				type = ma[i].getReturnType();
				i++;
			}

		} catch(RuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw new IllegalArgumentException(String.format("Entity class '%s' does not contain property '%s'", entityType.getName(), propertyName));
		}

		if(!Comparable.class.isAssignableFrom(ma[ma.length - 1].getReturnType())) {
			throw new IllegalArgumentException(String.format("Property value '%s' of entity '%s' must implement 'java.lang.Comparable' interface", propertyName, entityType.getName()));
		}

		EntityComparator<T> cmp = new EntityComparator<T>(ma);
		return cmp;
	}
	
}
