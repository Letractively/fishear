package net.fishear.utils;

import java.util.HashMap;
import java.util.Map;

public class Maps {

	/** converts object key-value pairs passed as an array to a map.
	 * Array must have even number of elements, each odd element must not be null and must be a String (= this is a key), odd element can be anything (including null).
	 * Duplicate keys are silently overwritten, last occurance of the same key is valid.
	 * @param parameters array with key-value pairs
	 * @return map
	 */
	public static Map<String, Object> toMap(Object... parameters) {

		Map<String, Object> map = new HashMap<String, Object>();

		if(parameters != null) {

			if(parameters.length %2 == 1) {
				throw new IllegalArgumentException("Parameter count must be even number");
			}
			
			for(int i = 0; i < parameters.length; i += 2) {
				if(parameters[i] == null || !(parameters[i] instanceof String)) {
					throw new IllegalArgumentException(String.format("Each even parameter must be not null string value, but parameter at index %s ('%s)' is not.", i, parameters[i]));
				}
				String key = parameters[i].toString();
				Object val = parameters[i + 1];
				map.put(key, val);
			}
		}
		return map;
	}
}
