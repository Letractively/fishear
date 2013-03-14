package net.fishear.utils;

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

		public final String name;
		public final Object value1;
		public final Object value2;

		public Property(String fldn, Object v1, Object v2) {
			this.value1 = v1;
			this.value2 = v2;
			this.name = fldn;
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
			return "Property: " + name + ", v1="+ value1 + ", v2=" + value2;
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
		
		Set<String> done = new HashSet<String>();

		List<Property> list = new ArrayList<EntityUtils.Property>();
		Class<?> clazz = e1.getClass();
		log.debug("listDifferencies: Comparison started");
		if (e1 == e2) {
			log.trace("listDifferencies: Entities are the same. No difference.");
			return list;
		}
		if (clazz != e2.getClass()) {
			throw new IllegalArgumentException("Both entities nust be instance of the same class");
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
					} else if (v1 == null || v2 == null || !v1.equals(v2)) {
						if(log.isTraceEnabled()) {
							log.trace(String.format("Added difference for field %s, v1={}, v2={}", fldn), v1, v2);
						}
						list.add(new Property(fldn, v1, v2));
					}
				} catch (Exception e) {
					log.error("Cannot get value for field: {}\nValue type: {}", fld, fld.getType());
					throw new AppException(e);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return list;
	}
	
	private static boolean isAnnotation(Object fldOrMethod, Class<? extends Annotation> annot) {
		if(fldOrMethod instanceof Field) {
			return ((Field)fldOrMethod).getAnnotation(annot) != null;
		} else if(fldOrMethod instanceof Method) {
			return ((Method)fldOrMethod).getAnnotation(annot) != null;
		} else {
			throw new IllegalArgumentException("'fldOrMethod' must be field or method");
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
					this.fld = clazz.getField(name.substring(0, 1)
							.toLowerCase().concat(name.substring(1)));
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
					this.hasGetter = false;
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
					this.setter = clazz.getMethod("set".concat(name),
							setterType);
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
			throw new IllegalArgumentException("The target entitiy must be assignable from the source.");
		}
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
				Method[] met = clazz.getMethods();
				for (int i = 0; i < met.length; i++) {
					Method m = met[i];
					String metName = m.getName();
					if(metName.startsWith("get")) {
						if(isId(m) || metName.equalsIgnoreCase("getId")) {
							continue;
						}
						Class<?> vtyp = m.getReturnType();
						try {
							Method setter = clazz.getMethod("set"+metName.substring(3), vtyp);
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
			throw new RuntimeException(ex);
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
	 * gets attribute value from bean instance and returns it as proper type.
	 * In case internal exception occurs, returns 'defaultValue'. Logs exception only if logLevel is set to DEBUG.
	 * 
	 * @param attrName
	 * @param entity
	 * @param defaultValue
	 * @return property value, or default value in case exception occurs.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(String attrName, Object entity, T defaultValue) {
		try {
			return (T) BeanUtils.getProperty(entity, attrName);
		} catch (Exception ex) {
			log.debug("exception during value getting", ex);
			return defaultValue;
		}
	}
	
}
