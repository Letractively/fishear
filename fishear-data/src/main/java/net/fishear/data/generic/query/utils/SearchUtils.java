package net.fishear.data.generic.query.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;

import net.fishear.data.generic.annotations.Interval;
import net.fishear.data.generic.annotations.Intervals;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.Globals;

public class SearchUtils
{

	private static Logger log = LoggerFactory.getLogger(SearchUtils.class);

	private static final Object UNDEFINED = "(UNDEFINED)";
	
	/**
	 * Creates search conditions in accordancy to values filled in 'idao'
	 * instance. It applies JavaBean mechanism in conjunction with
	 * {@link Searchable} annotation to determine whose values are added to
	 * search condition.
	 * 
	 * @param entity
	 * @return null if no condition is created (no appropriated fields are
	 *         filled ...), oherwise the condition.
	 * @see Searchable to explain how the search condition is created
	 */
	public static Conditions createSearchConditions(Object entity) {
		if (entity == null) {
			throw new NullPointerException("entity must not be null");
		}
		return createSearchConditions(entity, null, "", new Vector<String>(), 0);
	}

	/**
	 * Creates search conditions in accordancy to values filled in 'idao'
	 * instance. It applies JavaBean mechanism in conjunction with
	 * {@link Searchable} annotation to determine whose values are added to
	 * search condition.
	 * 
	 * @param entity1 the first entity. Must not be null.
	 * @param entity2 the second entity. It is optional, may be null. If not, intervals for dates and numbers are generated in case both values are passed.
	 * 
	 * @return null if no condition is created (no appropriated fields are
	 *         filled ...), oherwise the condition.
	 * @see Searchable to explain how the search condition is created
	 */
	public static Conditions createSearchConditions(Object entity1, Object entity2) {
		if (entity1 == null) {
			throw new NullPointerException("entity must not be null");
		}
		return createSearchConditions(entity1, entity2, "", new Vector<String>(), 0);
	}

	/**
	 * creates conditions using fields in 'entity' annotated by
	 * {@link Searchable} annotation.
	 * 
	 * @param entity1
	 *            the entity
	 * @param innerName
	 *            the "inner" entitty name
	 * @param done
	 * @param isInnerEntity
	 * @return
	 */
	private static Conditions createSearchConditions(Object entity1, Object entity2, String innerName, Vector<String> done, int level) {

		Conditions cond = new Conditions();
		boolean anyOk = false;
		Class<?> clazz = entity1.getClass();

		log.debug("Creating serarch conditions for entity '{}' ({})", clazz, entity1);

		if (level == 0 && !EntityUtils.isNew(entity1)) {
			if(entity2 != null) {
				throw new IllegalStateException("In case first entity is loaded from DB, the second one must be null.");
			}
			Object id = EntityUtils.getId(entity1);
			if(id != null) {
				return QueryFactory.equals("id", id).where().conditions();
			}
		}
		if(entity2 != null && entity2.getClass() != clazz) {
			throw new IllegalStateException(String.format("Both entities must be the same type, but first is '%s' and second is '%s'.", clazz.getName(), entity2.getClass().getName()));
		}
		
		// creates interval when class is annotated first
		if(clazz.getAnnotation(Interval.class) != null || clazz.getAnnotation(Intervals.class) != null) {
			log.trace("Proccesdsing interval annotated as 'Interval' at class level");
			Interval[] inta;
			if(clazz.getAnnotation(Interval.class) != null) {
				if(clazz.getAnnotation(Intervals.class) != null) {
					throw new IllegalStateException("Both 'Interval' and 'Intervals' annotations are not allowed at single entity");
				}
				log.debug("Proccesdsing interval annotated as 'Interval' at class level");
				inta = new Interval[] {clazz.getAnnotation(Interval.class)};
			} else {
				log.debug("Proccesdsing interval annotated as 'Intervals' at class level");
				inta = clazz.getAnnotation(Intervals.class).value();
			}
			for(Interval ano : inta) {
				log.trace("Class level interval for properties {} - {} ", ano.start(), ano.end());
				Object startval = EntityUtils.getRawValue(ano.start(), entity1, UNDEFINED);
				Object endval = EntityUtils.getRawValue(ano.end(), entity1, UNDEFINED);
				if(log.isTraceEnabled()) {
					log.trace(String.format("Generationg interval for property %s ... %s, values %s ... %s", ano.start(), ano.end(), startval, endval));
				}
				if(startval == UNDEFINED || endval == UNDEFINED) {
					throw new IllegalStateException(String.format("One of start or end value ('%s' or '%s') is not defined: ", ano.start(), ano.end()));
				}
				if(startval != null || endval != null) {
					cond.add(Restrictions.overlap(ano.start(), ano.end(), startval, endval));
					anyOk = true;
					done.add(clazz.getName() + "|" + EntityUtils.toMethodName(ano.start(), "get"));
					done.add(clazz.getName() + "|" + EntityUtils.toMethodName(ano.end(), "get"));
				}
			}
		}
		
		Method[] met = clazz.getMethods();
		for (int i = 0; i < met.length; i++) {
			Method m = met[i];
			String metName = m.getName();
			String uniMn = clazz.getName() + "|" + metName;
			if (done.contains(uniMn)) {
				continue;
			}
			done.add(uniMn);

			// Has the method name the convenience 'getSomething' form, or is
			// marked as searchable ?
			if ((metName.startsWith("get") && metName.length() > 4 && Character.isUpperCase(metName.charAt(3)) && m.getParameterTypes().length == 0)) {
				// method is not annotated as any known 'nonvalue' type
				Class<? extends Annotation> anoCl = Id.class;
				if ((m.getAnnotation(anoCl = Id.class) != null || m.getAnnotation(anoCl = GeneratedValue.class) != null || m.getAnnotation(anoCl = Transient.class) != null)) {
					log.trace("Getter '{}' is annotated by {}, ignored", m.getName(), anoCl);
					continue;
				}

				try {
					Class<?> retvalType = m.getReturnType();

					// checks if the set method exists with the same argument as
					// return value of get method
					if ((clazz.getMethod("set".concat(metName.substring(3)), new Class[] { retvalType }) != null)) {
						String fldName;
						fldName = innerName.concat(metName.substring(3, 4).toLowerCase().concat(metName.substring(4)));

						log.debug("Analyzing property '{}'", fldName);
						if(m.getAnnotation(Embedded.class) != null) {
							if(m.getAnnotation(Interval.class) != null) {
								log.debug("Embedded property '{}' is annotated as Interval", fldName);
								Object o1 = entity1 == null ? null : m.invoke(entity1, EntityUtils.EOA);
								if(o1 != null) {
									Interval ano = m.getAnnotation(Interval.class);
									Object startval = EntityUtils.getRawValue(ano.start(), o1, UNDEFINED);
									Object endval = EntityUtils.getRawValue(ano.end(), o1, UNDEFINED);
									if(startval == UNDEFINED || endval == UNDEFINED) {
										throw new IllegalStateException(String.format("One of start or end value ('%s' or '%s') is not defined: ", ano.start(), ano.end()));
									}
									if(log.isTraceEnabled()) {
										log.trace(String.format("Generationg interval for property %s.%s ... %s.%s, values %s ... %s", fldName, ano.start(), fldName, ano.end(), startval, endval));
									}
									if(startval != null || endval != null) {
										cond.add(Restrictions.overlap(fldName + "." + ano.start(), fldName + "." + ano.end(), startval, endval));
										anyOk = true;
									}
								} else {
									log.debug("Embedded property is annotated as Interval, but its value is null");
								}
							} else {
								Object o1 = entity1 == null ? null : m.invoke(entity1, EntityUtils.EOA);
								Object o2 = entity2 == null ? null : m.invoke(entity2, EntityUtils.EOA);
								if(o1 != null || o2 != null) {
									Conditions cond1 = createSearchConditions(o1, o2, fldName.concat("."), done, level + 1);
									if (cond1 != null) {
										cond.add(cond1.getRootRestriction());
										anyOk = true;
									}
								}
							}
							continue;
						}

						if (CharSequence.class.isAssignableFrom(retvalType)) {
							CharSequence chs = (CharSequence) m.invoke(entity1, EntityUtils.EOA);
							log.trace("Property '{}' of type 'CharSequence', value {}", fldName, chs);
							anyOk |= cond.addLikeNotEmpty(fldName, chs == null ? null : chs.toString());
						} else if (retvalType == char[].class) {
							char[] chs = (char[]) m.invoke(entity1, EntityUtils.EOA);
							if(chs != null) {
								String str = new StringBuilder(chs.length).append(chs).toString();
								log.trace("Property '{}' of type 'char[]', value {}", fldName, str);
								anyOk |= cond.addLikeNotEmpty(fldName, str);
							} else {
								log.trace("Property '{}' of type 'char[]', value {}", fldName, chs);
							}
						} else if (retvalType == Character.class || retvalType == Character.TYPE) {
							Character ch = (Character) m.invoke(entity1, EntityUtils.EOA);
							log.trace("Property '{}' of type 'Character', value {}", fldName, ch);
							anyOk |= cond.addLikeNotEmpty(fldName, ch == null ? null : ch.toString());
						} else if (retvalType == Boolean.class /*|| retvalType == Boolean.TYPE*/) {	// "small" boolean disabled since "not presented" state cannot be defined
							Boolean fl = (Boolean) m.invoke(entity1, EntityUtils.EOA);
							log.trace("Property '{}' of type 'Boolean', value {}", fldName, fl);
							if (fl != null) {
								cond.add(Restrictions.equal(fldName, fl));
								anyOk |= true;
							}
						} else if (Date.class.isAssignableFrom(retvalType)) {
							Date date1 = entity1 == null ? null : (Date) m.invoke(entity1, EntityUtils.EOA);
							Date date2 = entity2 == null ? null : (Date) m.invoke(entity2, EntityUtils.EOA);
							log.trace("Property '{}' of type 'Date', value {}", fldName, date1);
							if(entity2 == null) {
								if (date1 != null) {
									cond.add(Restrictions.equal(fldName, date1));
									anyOk |= true;
								}
							} else {
								if (date1 != null || date2 != null) {
									cond.add(Restrictions.interval(fldName, date1, date2));
									anyOk |= true;
								}
							}
						} else if (Number.class.isAssignableFrom(retvalType) || retvalType.isPrimitive()) {
							Number n1 = entity1 == null ? null : (Number) m.invoke(entity1, EntityUtils.EOA);
							Number n2 = entity2 == null ? null : (Number) m.invoke(entity2, EntityUtils.EOA);
							if(log.isTraceEnabled()) {
								log.trace(String.format("Property '%s' of type 'Number', value1 %s, value2 %s", fldName, n1, n2));
							}
							if(entity2 == null) {
								if(n1 != null) {
									boolean localOk = false;
									if (Double.class == retvalType || Float.class == retvalType || BigDecimal.class == retvalType) {
										localOk = cond.addNan(fldName, n1 == null ? Double.NaN : n1.doubleValue());
									} else if (Globals.doubleClass == retvalType || Globals.floatClass == retvalType) {
										localOk = cond.addNan(fldName, n1 == null ? Double.NaN : n1.doubleValue());
									} else if (Integer.class == retvalType || Integer.TYPE == retvalType) {
										if(retvalType.isPrimitive() ? n1.intValue() != 0 : true) {
											cond.addEquals(fldName, n1.intValue());
											localOk = true;
										}
									} else if (Short.class == retvalType || Short.TYPE == retvalType) {
										if(retvalType.isPrimitive() ? n1.shortValue() != 0 : true) {
											cond.addEquals(fldName, n1.shortValue());
											localOk = true;
										}
									} else if (Byte.class == retvalType || Byte.TYPE == retvalType) {
										if(retvalType.isPrimitive() ? n1.byteValue() != 0 : true) {
											cond.addEquals(fldName, n1.byteValue());
											localOk = true;
										}
									} else {
										if(retvalType.isPrimitive() ? n1.longValue() != 0 : true) {
											cond.addEquals(fldName, n1.longValue());
											localOk = true;
										}
									}
									if(localOk) {
										anyOk |= true;
										log.trace("Numeric property '{}' with value {} hes been added", fldName, n1);
									} else {
										log.trace("Numeric property '{}' with value {} hes NOT been added", fldName, n1);
									}
								} else {
									log.trace("Numeric property '{}' ommitted since value is null", fldName);
								}
							} else {
								if(n1 != null || n2 != null) {
									cond.add(Restrictions.interval(fldName, n1, n2));
									anyOk |= true;
									if(log.isTraceEnabled()) {
										log.trace(String.format("Numeric interval '%s' with value1 %s and value2 %s has been added", fldName, n1, n2));
									}
								}
							}
						} else if (retvalType == Class.class) {
							Class<?> cl = (Class<?>) m.invoke(entity1, EntityUtils.EOA);
							log.trace("Property '{}' of type 'Class', value {}", fldName, cl);
							anyOk |= cond.addLikeNotEmpty(fldName, cl.getName());
						} else if (Enum.class.isAssignableFrom(retvalType)) {
							Enum<?> en = (Enum<?>) m.invoke(entity1, EntityUtils.EOA);
							log.trace("Property '{}' of type 'Enum', value {}", fldName, en);
							if(en != null) {
								cond.addEquals(fldName, en);
								anyOk = true;
							}
						} else if (EntityI.class.isAssignableFrom(retvalType)) {
							log.trace("Field '{}' is type of EntityI, analysing entity", fldName);
							EntityI<?> e1 = entity1 == null ? null : (EntityI<?>) m.invoke(entity1, EntityUtils.EOA);
							EntityI<?> e2 = entity2 == null ? null : (EntityI<?>) m.invoke(entity2, EntityUtils.EOA);
							if (e1 != null || e2 != null) {
								log.trace("Field '{}': 'e1' or 'e2' value is not null. Analyzing.", fldName, e1);
								if(e1.isNew()) {
									log.trace("Field '{}' is non-persistent instance, assuming deep analysis", fldName);
//										Conditions cond1 = createSearchConditions(e1, fldName.concat("."), vec, true);
									Conditions cond1 = createSearchConditions(e1, e2, "", done, level + 1);
									if (cond1 != null) {
										cond.addJoin(fldName, cond1.getRootRestriction());
										anyOk = true;
									}
								} else {
									log.trace("Field '{}' is existing persistent instance, assuming equals entity", fldName);
									cond.addEquals(fldName, e1);
									anyOk = true;
								}
							} else {
								log.trace("Field '{}': both 'e1' and 'e2' values are nuill, do not analyze", fldName);
							}
						} else {
							Object o = m.invoke(entity1, EntityUtils.EOA);
							if(o != null) {
								// collections are silently ignored
								if(!Collection.class.isAssignableFrom(retvalType)) {
									log.warn("Field '{}' is unsupported type {}; ignored", fldName, retvalType.getName());
								}
							} else {
								log.debug("Field '{}' is unsupported type {}, but its value is null; ignored", fldName, retvalType.getName());
							}
						}
					} else {
						if(log.isTraceEnabled()) {
							log.trace("Setter with type '{}' does not exist fo Getter '{}'. Ignored", m.getReturnType(), m.getName());
						}
					}
				} catch (Exception ex) {
					if(ex instanceof NoSuchMethodException || ex instanceof IllegalAccessException) {
						// do nothing so far ?
					} else {
						log.error("Error while getting property value", ex);
					}
				}
			}
		}
		Conditions rcond = anyOk ? cond : null;
		log.debug("Serarch conditions for entity '{}' is {}", clazz, rcond);
		return rcond;
	}
}
