package net.fishear.data.generic.query.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import net.fishear.Interfaces.IdI;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.Globals;

public class SearchUtils
{

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
		if (isInnerEntity) {
			Object id = EntityUtils.getId(entity);
			if (EntityUtils.isNew(entity)) {
				return QueryFactory.equals(innerName.concat("id"), id).where().conditions();
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
								anyOk |= cond.addLikeNotEmpty(fldName, chs == null ? null : chs.toString());
							} else if (retvalType == char[].class) {
								char[] chs = (char[]) m.invoke(entity, EntityUtils.EOA);
								anyOk |= cond.addLikeNotEmpty(fldName, chs == null ? null : new StringBuilder(chs.length).append(chs).toString());
							} else if (retvalType == Character.class || retvalType == Character.TYPE) {
								Character ch = (Character) m.invoke(entity, EntityUtils.EOA);
								anyOk |= cond.addLikeNotEmpty(fldName, ch == null ? null : ch.toString());
							} else if (retvalType == Boolean.class || retvalType == Boolean.TYPE) {
								Boolean fl = (Boolean) m.invoke(entity, EntityUtils.EOA);
								if (fl != null) {
									cond.add(Restrictions.equal(fldName, fl));
									anyOk |= true;
								}
							} else if (Date.class.isAssignableFrom(retvalType)) {
								Date date = (Date) m.invoke(entity, EntityUtils.EOA);
								if (date != null) {
									cond.add(Restrictions.equal(fldName, date));
									anyOk |= true;
								}
							} else if (Number.class.isAssignableFrom(retvalType) || retvalType.isPrimitive()) {
								Number n = (Number) m.invoke(entity, EntityUtils.EOA);
								if (Double.class == retvalType || Float.class == retvalType || BigDecimal.class == retvalType) {
									anyOk |= cond.addNan(fldName, n == null ? Double.NaN : n.doubleValue());
								} else if (Globals.doubleClass == retvalType || Globals.floatClass == retvalType) {
									anyOk |= cond.addNan(fldName, n == null ? Double.NaN : n.doubleValue());
								} else {
									anyOk |= cond.addNotZero(fldName, n == null ? 0 : n.longValue());
								}
							} else if (retvalType == Class.class) {
								Class<?> cl = (Class<?>) m.invoke(entity, EntityUtils.EOA);
								anyOk |= cond.addLikeNotEmpty(fldName, cl.getName());
							} else if (IdI.class.isAssignableFrom(retvalType)) {
								IdI<?> e1 = (IdI<?>) m.invoke(entity, EntityUtils.EOA);
								if (e1 != null) {
									Conditions c1 = createSearchConditions(e1, fldName.concat("."), vec, true);
									if (c1 != null) {
										cond.add(c1.getRootRestriction());
										anyOk = true;
									}
								}
							} else {
								// TODO:
							}
						}
					} catch (Exception ex) {
						if(ex instanceof NoSuchMethodException || ex instanceof IllegalAccessException) {
							// do nothing
						} else {
							ex.printStackTrace(); // TODO: better way how to inform ?
						}
					}
				}
			}
		}
		return anyOk ? cond : null;
	}
}
