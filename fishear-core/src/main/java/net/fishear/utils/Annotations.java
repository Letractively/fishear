package net.fishear.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Annotations {

	private static final Class<?> TOP_LEVEL_CLASS = Object.class;

	public static class AnnotFields<T extends Annotation> {

		public final Field fld;
		public final T annot;

		public AnnotFields(Field obj, T annot) {
			this.fld = obj;
			this.annot = annot;
			if(!fld.isAccessible()) {
				fld.setAccessible(true);
			}
		}
	}

	public static class AnnotMethod<T extends Annotation> {

		public final Method met;
		public final T annot;

		public AnnotMethod(Method met, T annot) {
			this.met = met;
			this.annot = annot;
			if(!met.isAccessible()) {
				met.setAccessible(true);
			}
		}
	}

	/**
	 * returns all fields annotated with given annotation ant corresponding
	 * annotation instance.
	 * The order of returned elements starts in object's class and continue to superclasses.
	 * @param <T>
	 * @param object
	 * @param annotCl
	 * @return
	 */
	public static <T extends Annotation> List<AnnotFields<T>> getAnnotatedFields(Object object, Class<T> annotCl) {

		List<AnnotFields<T>> evaList = new ArrayList<AnnotFields<T>>();

		Class<?> clazz = object.getClass();
		do {
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field fld = fields[i];
				T ev;
				if ((ev = fld.getAnnotation(annotCl)) != null) {
					evaList.add(new AnnotFields<T>(fld, ev));
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return evaList;
	}

	/**
	 * returns all fields annotated with given annotation ant corresponding
	 * annotation instance.
	 * The order of returned elements starts in object's class and continue to superclasses.
	 * @param <T>
	 * @param object
	 * @param annotCl
	 * @return method list, or empty list (never returns null)
	 */
	public static <T extends Annotation> List<AnnotMethod<T>> getAnnotatedMethods(Object object, Class<T> annotCl) {

		List<AnnotMethod<T>> evaList = new ArrayList<AnnotMethod<T>>();

		Class<?> clazz = object.getClass();
		do {
			java.lang.reflect.Method[] fields = clazz.getDeclaredMethods();
			for (int i = 0; i < fields.length; i++) {
				Method met = fields[i];
				T ev;
				if ((ev = met.getAnnotation(annotCl)) != null) {
					evaList.add(new AnnotMethod<T>(met, ev));
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return evaList;
	}

	/**
	 * returns all methods annotated with given annotation in class inheritance hierarchy.
	 * The order of returned elements starts in 'clazz' and continues to superclasses.
	 * Each method is presented only once in result - with it's first occurance (does not matter it is static or not).
	 * @param <T>
	 * @param clazz the class where the annotations are searched
	 * @param annotClass the annotation it is searched for
	 * @return method list, or empty list (never returns null)
	 */
	public static <T extends Annotation> List<Method> getAnnotatedMethods(Class<?> clazz, Class<T> annotClass) {
		List<Method> list = new ArrayList<Method>();
		Set<String> set = new TreeSet<String>();
		do {
			java.lang.reflect.Method[] mets = clazz.getDeclaredMethods();
			for (int i = 0; i < mets.length; i++) {
				Method met = mets[i];

				T ev = met.getAnnotation(annotClass);
				if ((ev) != null) {
					String signature = getSignature(met);
					if(!set.contains(signature)) {
						set.add(signature);
						list.add(met);
					}
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return list;
	}

	private static String getSignature(Method met) {
		StringBuilder sb = new StringBuilder();
		sb.append(met.getName()).append("(");
		Class<?>[] pta = met.getParameterTypes();
		for (int i = 0; i < pta.length; i++) {
			if(i > 0) sb.append(",");
			sb.append(pta[i].getName());
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * returns all fields annotated with given annotation.
	 * The order of returned elements starts in 'clazz' and continues to superclasses.
	 * Every field from every superclass is presented in result, regardless it's visibility. 
	 * Therefore, in result may exist fields with the same name.
	 * @param <T>
	 * @param clazz the class where the annotations are searched
	 * @param annotClass the annotation it is searched for
	 * @return field list, or empty list (never returns null)
	 */
	public static <T extends Annotation> List<Field> getAnnotatedFields(Class<?> clazz, Class<T> annotClass) {
		List<Field> list = new ArrayList<Field>();
		do {
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field met = fields[i];
				T ev = met.getAnnotation(annotClass);
				if ((ev) != null) {
					list.add(met);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != TOP_LEVEL_CLASS);
		return list;
	}
}
