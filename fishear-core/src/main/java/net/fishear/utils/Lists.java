package net.fishear.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import net.fishear.exceptions.AppException;


public class Lists {

	public static final Object ERROR_OBJECT = new Object();
	
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
	
	
	public static <T> List<T>sublist(List<T> list, String propertyName, Object... allowedValues) {
		return sublist(false, list, propertyName, allowedValues);
	}

	/**
	 * filters list to only elements, that contaions property with given set of values.
	 * 
	 * 
	 * @param list the list
	 * @param propertyName name of property (may be nested, dot separated) that value is tested
	 * @param negate if true => the condition is negated. That means returns list with item that's 'propertyName' DOES NOT contain any of values in 'allowedValues'.
	 * @param allowedValues list of values that value of 'propertyName' must fit. 
	 * @return sublist
	 */
	public static <T> List<T>sublist(final boolean negate, List<T> list, final String propertyName, Object... allowedValues) {
	
		final List<?> values = Arrays.asList(allowedValues);
		ListFilter<T> filter = new ListFilter<T>() {

			@Override
			public boolean addToResult(T o) {
				Object theVal = EntityUtils.getRawValue(propertyName, o, null);
				if(values.contains(theVal)) {
					return !negate;
				} else {
					return negate;
				}
			}
		};

		return sublist(list, filter);
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
	
	/**
	 * sums all not null values in given numeric property.
	 * Null or not numeric values are ignored without error.
	 * 
	 * @param list the entity list 
	 * @param propertyName summed property name
	 * @return 
	 */
	public static BigDecimal sum(List<?> list, String propertyName) {
		
		BigDecimal ret = BigDecimal.ZERO;

		for(Object it : list) {
			Number num = (Number)EntityUtils.getValue(it, propertyName, null);
			if(num != null) {
				ret = ret.add(new BigDecimal(num.doubleValue()));
			}
		}
		return ret;
	}
	
	/**
	 * creates map from given value from each entity in the list as key and and the entity as value. 
	 * 
	 * @param list the entity list
	 * @param keyPropertyName property name (from the entity) that's value is set as a key
	 * @return map with the key and entity
	 */
	public static <K, T> Map<K, T> toKeyEntityMap(List<T> list, String keyPropertyName) {
		Map<K, T> map = new HashMap<K, T>();

		for(T it : list) {
			@SuppressWarnings("unchecked")
			K key = (K) EntityUtils.getValue(it, keyPropertyName, null);
			if(key == null) {
				throw new IllegalStateException(String.format("The key's value must not be null. The key is '%s'", keyPropertyName));
			} else {
				if(map.put(key, it) != null) {
					throw new IllegalStateException(String.format("Key Duplicities are not allowed. The key is '%s', value = %s", keyPropertyName, key));
				}
			}
		}
		
		return map;
	}
	
	
	/**
	 * groups list of entities by given property returning map with key and corresponding sublist.
	 * Creates map where the key is value of given property and value is corresponding sublist where each entity have the same value it the key property.
	 * 
	 * @param list the list for grouping
	 * @param keyPropertyName name of the key property from the entity (may be nested, separated by dot)
	 * @return map 
	 */
	public static <K, T> Map<K, List<T>> group(List<T> list, String keyPropertyName) {
		
		Map<K, List<T>> map = new HashMap<K, List<T>>();
		for(T it : list) {
			@SuppressWarnings("unchecked")
			K key = (K) EntityUtils.getValue(it, keyPropertyName, null);
			List<T> rlist = map.get(key);
			if(rlist == null) {
				rlist = new ArrayList<T>();
				map.put(key, rlist);
			}
			rlist.add(it);
		}
		
		return map;
		
	}
	
	/**
	 * extracts given property values to the extra list.
	 * Duplicities are grouped - each dup value is put only once to the final list. "equals" method is used to decide whether top add value or not.
	 * 
	 * @param list the source list
	 * @param extractedPropertyName name ot the property that will be extracted from the source entities and put to ne list.
	 * @return new list
	 */
	public static <T> List<T> extract(List<?> list, String extractedPropertyName) {
		List<T> nlist = new ArrayList<T>();
		for(Object en : list) {
			
			T val = EntityUtils.getRawValue(extractedPropertyName, en, null);

			if(val != null && !nlist.contains(val)) {
				nlist.add(val);
			}
		}
		return nlist;
	}
}
