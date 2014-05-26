package net.fishear.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import net.fishear.exceptions.AppException;


public class Lists {

	/** creates and returns new {@link ArrayList}, which contains all elements from 'list' whose satisfy {@link ListFilter} 'filter'.
	 */
	public static <T> List<T>sublist(List<T> list, ListFilter<T> filter) {
		List<T> retlist = new ArrayList<T>();
		for (T t : list) {
			if(filter.addToResult(t)) {
				retlist.add(t);
			}
		}
		return retlist;
	}

	public static <T> List<T>sublist(List<T> list, int from, int to) {
		if(from < 0) {
			from = 0;
		}
		if(from >= list.size()) {
			return new ArrayList<T>();
		}
		if (to >= list.size()) {
			to = list.size();
		}

		List<T> retlist = new ArrayList<T>(to - from);
		for (int i = from; i < to; i++) {
			retlist.add(list.get(i));
		}
		return retlist;
	}

	/** return true if list contains at least one element which satisfy to filter 'filter'.
	 * Return false if list DOES NOT contain those element.
	 */
	public static <T> boolean contains(List<T> list, ListFilter<T> filter) {
		for (T t : list) {
			if(filter.addToResult(t)) {
				return true;
			}
		}
		return false;
	}

	/** prepares map (suitable as source for Tapestry lists or selects).
	 * Keys and values are transformed using toString methods.
	 * null keys are NOT added map, null values are added as empty string.
	 * @param list source list
	 * @param keyName name of key field. It's getter is used to obtain keys. In may be closed to ${ ... } (but may not). Keys are trimmed.
	 * @param valueNames list of vales. If value is closed by "'...'" (apostrophes), it is copied to target value "as is" (but without aposrophes).
	 * Otherwise, it's it is assumed that name is the property name. Hence getter is used to obtain value - it must exist. 
	 * Values are NOT trimmed.
	 */
	public static Map<String, String> toMap(List<? extends Object> list, String keyName, String... valueNames) {

		Map<String, String> map = new TreeMap<String, String>();
		if((keyName = Texts.tos(keyName)).length() == 0) {
			throw new IllegalArgumentException("The 'keyName' must not be null or empty");
		}
		if(keyName.startsWith("${") && keyName.endsWith("}")) {
			keyName = keyName.substring(2, keyName.length() - 1).trim();
		}
		if((keyName = Texts.tos(keyName)).length() == 0) {
			throw new IllegalArgumentException("The 'keyName' must not be null or empty");
		}
		if(list.size() > 0) {
			if(valueNames == null || valueNames.length == 0) {
				valueNames = new String[] {keyName};
			}
			try {
				Object[] valVals = new Object[valueNames.length];
				for (Object e : list) {
					Class<? extends Object> clazz = e.getClass();
					Method keyMet = EntityUtils.getGetter(clazz, keyName);
					if(keyMet == null) {
						throw new AppException("no getter for key '"+keyName+"' found in class '"+clazz+"'");
					}
					for (int i = 0; i < valueNames.length; i++) {
						String valueName = valueNames[i];
						String valn;
						if(valueName.startsWith("'") && valueName.endsWith("'") && valueName.length() > 2) {
							valVals[i] = valueName.substring(1, valueName.length() - 1);
						} else {
							if(valueName.startsWith("${") && valueName.endsWith("}")) {
								valn = valueName.substring(2, valueName.length() - 1).trim();
							} else {
								valn = valueName;
							}
							Method valMet = EntityUtils.getGetter(clazz, valn);
							if(valMet == null) {
								throw new AppException("no getter for value '"+valn+"' found in class '"+clazz+"'");
							}
							valVals[i] = valMet.invoke(e);
						}
					}

					StringBuilder sb = new StringBuilder();
					Object key = keyMet.invoke(e);
					for (int i = 0; i < valueNames.length; i++) {
						sb.append(valVals[i] == null ? "" : valVals[i].toString());
					}
					if(key != null) {
						map.put(Texts.tos(key.toString()), sb.toString());
					}
				}
			} catch (IllegalArgumentException ex) {
				throw new AppException(ex);
			} catch (IllegalAccessException ex) {
				throw new AppException(ex);
			} catch (InvocationTargetException ex) {
				throw new AppException(ex);
			}
		}
		return map;
	}
	
	/** converts array to {@link ArrayList} and returns the list.
	 */
	public static <T> List<T> toList(T... array) {
		List<T> list = new ArrayList<T>();
		if(array != null) {
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
		}
		return list;
	}
	
	/** 
	 * sets given property value to all list items.
	 * 
	 * @param list the list
	 * @param propertyName name of property to be set
	 * @param value the value ...
	 * @return the same list (allows chainig)
	 */
	public static <T> List<T> set(List<T> list, String propertyName, Object value) {
		for(T it : list) {
			EntityUtils.setValue(it, propertyName, value);
		}
		return list;
	}
}
