package net.fishear.data.generic.query.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.Globals;

public class SearchUtils
{

	private static Logger log = LoggerFactory.getLogger(SearchUtils.class);
	
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
		return createSearchConditions(entity, "", new Vector<Method>(), false);
	}

	/**
	 * creates conditions using fields in 'entity' annotated by
	 * {@link Searchable} annotation.
	 * 
	 * @param entity
	 *            the entity
	 * @param innerName
	 *            the "inner" entitty name
	 * @param vec
	 * @param isInnerEntity
	 * @return
	 */
	private static Conditions createSearchConditions(Object entity, String innerName, Vector<Method> vec, boolean isInnerEntity) {
		Conditions cond = new Conditions();
		boolean anyOk = false;
		Class<?> clazz = entity.getClass();
		Method[] met = clazz.getMethods();
		if (isInnerEntity && !EntityUtils.isNew(entity)) {
			Object id = EntityUtils.getId(entity);
			if(id != null) {
				return QueryFactory.equals("id", id).where().conditions();
			}
		}
		for (int i = 0; i < met.length; i++) {
			Method m = met[i];
			if (vec.contains(m)) {
				continue;
			}
			vec.add(m);
			String metName = m.getName();

			// Has the method name the convenience 'getSomething' form, or is
			// marked as searchable ?
			if ((metName.startsWith("get") && metName.length() > 4 && Character.isUpperCase(metName.charAt(3)) && m.getParameterTypes().length == 0)) {
				// method is not annotated as any known 'nonvalue' type
				if ((m.getAnnotation(Id.class) == null && m.getAnnotation(GeneratedValue.class) == null && m.getAnnotation(Transient.class) == null)) {
					Class<?> retvalType = m.getReturnType();
					// checks if the set method exists with the same argument as
					// return value of get method
					try {
						if ((clazz.getMethod("set".concat(metName.substring(3)), new Class[] { retvalType }) != null)) {
							String fldName;
							fldName = innerName.concat(metName.substring(3, 4).toLowerCase().concat(metName.substring(4)));
							if (CharSequence.class.isAssignableFrom(retvalType)) {
								CharSequence chs = (CharSequence) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'CharSequence' and value {}", fldName, chs);
								anyOk |= cond.addLikeNotEmpty(fldName, chs == null ? null : chs.toString());
							} else if (retvalType == char[].class) {
								char[] chs = (char[]) m.invoke(entity, EntityUtils.EOA);
								if(chs != null) {
									String str = new StringBuilder(chs.length).append(chs).toString();
									log.trace("Adding propety {} of type 'char[]' and value {}", fldName, str);
									anyOk |= cond.addLikeNotEmpty(fldName, str);
								} else {
									log.trace("Adding propety {} of type 'char[]' and value {}", fldName, chs);
								}
							} else if (retvalType == Character.class || retvalType == Character.TYPE) {
								Character ch = (Character) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'Character' and value {}", fldName, ch);
								anyOk |= cond.addLikeNotEmpty(fldName, ch == null ? null : ch.toString());
							} else if (retvalType == Boolean.class || retvalType == Boolean.TYPE) {
								Boolean fl = (Boolean) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'Boolean' and value {}", fldName, fl);
								if (fl != null) {
									cond.add(Restrictions.equal(fldName, fl));
									anyOk |= true;
								}
							} else if (Date.class.isAssignableFrom(retvalType)) {
								Date date = (Date) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'Date' and value {}", fldName, date);
								if (date != null) {
									cond.add(Restrictions.equal(fldName, date));
									anyOk |= true;
								}
							} else if (Number.class.isAssignableFrom(retvalType) || retvalType.isPrimitive()) {
								Number n = (Number) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'Number' and value {}", fldName, n);
								if(n != null) {
									if (Double.class == retvalType || Float.class == retvalType || BigDecimal.class == retvalType) {
										anyOk |= cond.addNan(fldName, n == null ? Double.NaN : n.doubleValue());
									} else if (Globals.doubleClass == retvalType || Globals.floatClass == retvalType) {
										anyOk |= cond.addNan(fldName, n == null ? Double.NaN : n.doubleValue());
									} else {
										anyOk |= cond.addNotZero(fldName, n == null ? 0 : n.longValue());
									}
								}
							} else if (retvalType == Class.class) {
								Class<?> cl = (Class<?>) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'Class' and value {}", fldName, cl);
								anyOk |= cond.addLikeNotEmpty(fldName, cl.getName());
							} else if (Enum.class.isAssignableFrom(retvalType)) {
								Enum<?> en = (Enum<?>) m.invoke(entity, EntityUtils.EOA);
								log.trace("Adding propety {} of type 'Enum' and value {}", fldName, en);
								if(en != null) {
									cond.addEquals(fldName, en);
									anyOk = true;
								}
							} else if (EntityI.class.isAssignableFrom(retvalType)) {
								EntityI<?> e1 = (EntityI<?>) m.invoke(entity, EntityUtils.EOA);
								if (e1 != null) {
									log.trace("Field {} is type of EntityI, analysing entity", fldName, EntityI.class);
									if(e1.isNew()) {
										log.trace("Field {} is non-persistent instance, assuming deep analysis", fldName);
//										Conditions cond1 = createSearchConditions(e1, fldName.concat("."), vec, true);
										Conditions cond1 = createSearchConditions(e1, "", vec, true);
										if (cond1 != null) {
											cond.addJoin(fldName, cond1.getRootRestriction());
											anyOk = true;
										}
									} else {
										log.trace("Field {} is existing persistent instance, assuming equals entity", fldName);
										cond.addEquals(fldName, e1);
										anyOk = true;
									}
								}
							} else {
								Object o = m.invoke(entity, EntityUtils.EOA);
								if(o != null) {
									log.warn("Field {} is unsupported type {}; ignored", fldName, retvalType.getName());
								} else {
									log.debug("Field {} is unsupported type {}, but its value is null; ignored", fldName, retvalType.getName());
								}
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
		}
		return anyOk ? cond : null;
	}
}
