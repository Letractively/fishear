package net.fishear.utils;

import net.fishear.exceptions.AppException;


public class Enums
{

	/** converts string value 's' to corresponding enum's value of type 'type'.
	 * Throws ErrorException (which wraps real cause) in case 's' is not element from within 'type', or any other error occur.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<?>> T toValue(String s, Class<T> type) {
		if(s == null) {
			return null;
		}
		try {
			return (T) type.getMethod("valueOf", Class.class, String.class).invoke(null, type, s);
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	/** checks if enumeration type 'type' has the item 'value'. If not, throws ErrorException.
	 * @return checked value
	 */
	public static <T extends Enum<?>> String toString(String s, Class<T> type) {
		return toValue(s, type).toString();
	}

	/** null-safe variant of 'type.toString()'.
	 * Returns null in case 'type' is null, 'type.toString()' otherwise.
	 */
	public static String toString(Enum<?> type) {
		return type == null ? null : type.toString();
	}
}
