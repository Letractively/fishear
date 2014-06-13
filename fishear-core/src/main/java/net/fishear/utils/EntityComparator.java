package net.fishear.utils;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * compares two objects (formally 'entites') by them list of values.
 * 
 * @author ffyxrr
 *
 * @param <T>
 */
public class EntityComparator<T> implements Comparator<T> {
	
	private final Method[] ma;
	
	private boolean invert;
	
	EntityComparator(Method[] ma) {
		this.ma = ma;
	}
	
	private Object[] val(Object o) {
		
		Object val = o;
		Object[] va = new Object[ma.length];
		
		int i = 0;
		for(Method m : ma) {
			try {
				va[i++] = val = m.invoke(val);
				if(val == null) {
					break;
				}
			} catch (Exception ex) {
				throw new IllegalStateException(ex);
			}
		}
		return va;
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		return invert ? -compareInternal(o1, o2) : compareInternal(o1, o2);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int compareInternal(Object o1, Object o2) {

		Object[] va1 = val(o1);
		Object[] va2 = val(o2);

		for(int i = 0; i < va1.length; i++) {
			if(va1[i] == null) {
				return va2[i] == null ? 0 : -1;
			} else if(va2[i] == null) {
				return 1;
			}
		}

		Object v1 = va1[va1.length - 1];
		Object v2 = va2[va2.length - 1];

		if(v1 == null) {
			return v2 == null ? 0 : -1;
		} else if(v2 == null) {
			return v1 == null ? 0 : 1;
		} else {
			return ((Comparable)v1).compareTo(v2);
		}
	}

	/**
	 * @return the invert
	 */
	public boolean isInvert() {
		return invert;
	}

	/**
	 * @param invert the invert to set
	 * @return this instance
	 */
	public EntityComparator<T> setInvert(boolean invert) {
		this.invert = invert;
		return this;
	}
}