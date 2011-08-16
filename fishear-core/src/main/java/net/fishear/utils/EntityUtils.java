package net.fishear.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import net.fishear.Interfaces.IdI;



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
	 */
//	public static boolean equals(IdI e1, Object e2) {
//		return equals(e1, e2, new HashSet<Field>());
//	}
//
//	public static boolean equals(IdI e1, Object e2,
//			Set<Field> done) {
//		if (e1 == e2) {
//			return true;
//		}
//		Class<?> clazz = e1.getClass();
//		if (clazz != e2.getClass()) {
//			return false;
//		}
//		do {
//			Field[] fields = clazz.getDeclaredFields();
//			for (int i = 0; i < fields.length; i++) {
//				Field fld = fields[i];
//				String fldn = fld.getName();
//				if (fld.getAnnotation(Transient.class) != null) {
//					continue;
//				}
//				if (done.contains(fld)) {
//					continue;
//				}
//				done.add(fld);
//				String metn = "get" + fldn.substring(0, 1).toUpperCase()
//						+ fldn.substring(1);
//				Method m;
//				try {
//					Object v1, v2;
//					try {
//						m = clazz.getMethod(metn);
//						if (m.getAnnotation(Transient.class) != null) {
//							continue;
//						}
//						if (!m.isAccessible()) {
//							m.setAccessible(true);
//						}
//						v1 = m.invoke(e1, EOA);
//						v2 = m.invoke(e2, EOA);
//					} catch (Exception e) { // exceptions is OK here if getter
//											// does not exists
//						if (!fld.isAccessible()) {
//							fld.setAccessible(true);
//						}
//						v1 = fld.get(e1);
//						v2 = fld.get(e2);
//					}
//					if (v1 == null) {
//						if (v2 != null) {
//							return false;
//						}
//					} else {
//						if (v2 == null) {
//							return false;
//						}
//						if (v1 instanceof IdI) {
//							if (!equals((IdI) v1,
//									(IdI) v2, done)) {
//								return false;
//							}
//						} else {
//							if (!v1.equals(v2)) {
//								return false;
//							}
//						}
//					}
//				} catch (Exception e) {
//					System.err.println("Cannot get value for field: " + fld
//							+ "\nValue type: " + fld.getType());
//					// e.printStackTrace();
//					throw new AppException(e);
//				}
//			}
//		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
//		return true;
//	}

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
				if ((name.startsWith("get") || name.startsWith("set"))
						&& Character.isUpperCase(name.charAt(3))) {
					name = name.substring(3);
				}
			}
			return name.substring(0, 1).toUpperCase().concat(name.substring(1));
		}
		return "";
	}

	private static final Hashtable<Class<?>, Hashtable<String, FldDesc>> cache = new Hashtable<Class<?>, Hashtable<String, FldDesc>>();

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

	/** takes all non-empty fields from srcE and fill it's values to dstE.
	 * @param srcE the source entity
	 * @param dstE the destination
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void fillNotEmpty(Object srcE, Object dstE) {
		
		Class<?> clazz = srcE.getClass();
		if(srcE.getClass() != dstE.getClass()) {
			throw new IllegalArgumentException("The source and target entities must be instance of the same class.");
		}
		try {
			while(clazz != Object.class) {
				Method[] met = clazz.getMethods();
				for (int i = 0; i < met.length; i++) {
					Method m = met[i];
					String metName = m.getName();
					if(metName.startsWith("get") && !metName.equals("getId")) {
						Class<?> vtyp = m.getReturnType();
						try {
							Method setter = clazz.getMethod("set"+metName.substring(3), vtyp);
							if(
									m.getAnnotation(Transient.class) == null && 
									setter.getAnnotation(Transient.class) == null &&
									Modifier.isPublic(m.getModifiers()) && Modifier.isPublic(setter.getModifiers())
							) {
								Object val = m.invoke(srcE);
								if(val != null) {
									setter.invoke(dstE, val);
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
}
