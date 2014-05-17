package net.fishear.utils;


/**
 * Utility class for validating incoming data to methods.
 * <p/>
 * First argument of each method is object whose state will be checked. Second
 * argument is object name for composing error message.
 * <p/>
 * When method predicate is <code>false</code> method throws
 * <tt>IllegalArgumentException</tt>. If passed, do nothing.
 * <p/>
 * Each error message contains description "what happpen" and argument real name
 * for better error tracking.
 * 
 * <p>
 * Example: <blockquote>
 * 
 * <pre>
 * 
 * public void takeName(ExtPerson person, String name) {
 *      Defender.notNull(obj, "person");
 *      Defender.notNullOrEmpty(name, "name");
 *      ...
 *      ...
 * }
 * 
 * </pre>
 * 
 * </blockquote>
 */
public class Defender
{

	/**
	 * Class cannot be instanciate. :P
	 */
	protected Defender() { }

	/**
	 * Check string, if is null or empty.
	 * 
	 * @param arg
	 *            Checked string.
	 * @param argName
	 *            String real name.
	 * @return the 'arg'
	 * 
	 * @throws IllegalArgumentException
	 *             When string argument is null or empty.
	 */
	public static String notNullOrEmpty(final String arg, final String argName) {
		Defender.notNull(arg, argName);
		if (arg.isEmpty()) {
			throwIllegalArgumentException("String argument '%s' cannot be empty!", argName);
		}
		return arg;
	}

	public static void greatThanZero(final Number arg, final String argName) {
		notNull(arg, argName);
		if (arg.longValue() <= 0) {
			throwIllegalArgumentException("Numeric argument '%s' must be great han zero!", argName);
		}
	}

	public static void greatThanOrZero(final Number arg, final String argName) {
		notNull(arg, argName);
		if (arg.longValue() < 0) {
			throwIllegalArgumentException("Numeric argument '%s' must be great han zero!", argName);
		}
	}

	/**
	 * Check object, if is null.
	 * 
	 * @param obj
	 *            Checked object.
	 * @param argNameOrText
	 *            Object real name. If contains whitespace (even at begin or end), the {@link IllegalStateException} 
	 *            is thrown with this text. Otherwise, {@link IllegalArgumentException} is thrown with text 
	 *            "Argument ... cannot be null".
	 * @return  the 'obj'
	 * 
	 * @throws IllegalArgumentException
	 *             When object is null. The text is con
	 * @throws IllegalStateException
	 *             When object is null. The text is con
	 */
	public static <T> T notNull(final T obj, final String argNameOrText) {
		if (obj == null) {
			throwException("Argument '%s' cannot be null!", argNameOrText);
		}
		return obj;
	}

	private static void throwException(String string, String argNameOrText) {
		if(argNameOrText == null || argNameOrText.trim().length() == 0) {
			argNameOrText = "(unknown_name)";
		}
		RuntimeException ex ;
		if(argNameOrText.indexOf(' ') >= 0 || argNameOrText.indexOf('\t') >= 0 || argNameOrText.indexOf('\r') >= 0 || argNameOrText.indexOf('\n') >= 0) {
			ex = new IllegalStateException(argNameOrText);
		} else {
			ex = new IllegalArgumentException(String.format(string, argNameOrText));
		}
		throw fillStackTrace(ex);
	}

	private static RuntimeException fillStackTrace(RuntimeException ex) {

		try {
			ex.setStackTrace(Classes.removeStartingItems(ex.getStackTrace(), Defender.class));
		} catch (Exception eex) {
			// ignored in case VM has not implemented some methods...
		}
		return ex;
	}

	/** checks if an array is not null, has minimum length as 'minLen' and each item to minLen is not null.
	 * Otherwise, the exception is thrown. It is construct as in {@link #notNull(Object, String)} method.
	 * @param array
	 */
	public static void notEmpty(Object[] array, int minLen, String argNameOrText) {
		notNull(array, argNameOrText);
		if(array.length < minLen) {
			throwException(String.format("Array '%s' must hav minimum length %s, but it has %s", "%s", minLen, array.length), argNameOrText);
		}
		for (int i = 0; i < array.length; i++) {
			if(array[i] == null) {
				throwException(String.format("Array '%s' must hav elements to size %s not null, but item at index %s is null", "%s", minLen, i), argNameOrText);
			}
		}
	}

	/** checks it the array contains at least one element, which must not to be null.
	 * @see #notEmpty(Object[], int, String)
	 */
	public static void notEmpty(Object[] array, String argNameOrText) {
		notEmpty(array, 1, argNameOrText);
	}

	/** checks if object 'o' is instance of class / interface 'clazz'
	 */
	public static void extend(Object o, Class<?> clazz, String argNameOrText) {
		if(!clazz.isAssignableFrom(o.getClass())) {
			if(clazz.isInterface()) {
				throwException(String.format("Object passed as argument '%s' must implement interface '%s', but '%s' dos not!", "%s", clazz.getName(), o.getClass()), argNameOrText);
			} else {
				throwException(String.format("Object passed as argument '%s' must extend the class '%s', but '%s' dos not!", "%s", clazz.getName(), o.getClass()), argNameOrText);
			}
		}
	}


	public static void isSet(Object field, String fieldName) {
		if(field == null) {
			throw new IllegalStateException(String.format("The field '%s' is null. Use appropriate setter to set value of this field before use it.", fieldName));
		}
	}

	/**
	 * Check object, if is null. The only differention to
	 * {@link #isNotNull(Object, String)} is the different text of message in
	 * case value is null.
	 * 
	 * @param obj
	 *            Checked object.
	 * @param argName
	 *            Object real name.
	 * 
	 * @throws IllegalArgumentException
	 *             When object is null.
	 */
	public static void isNotNullValue(final Object obj, final String argName) {
		if (obj == null) {
			throwIllegalArgumentException("Value of %s cannot be null!", argName);
		}
	}

	/**
	 * Compose error message and throws <tt>IllegalArgumentException</tt>.
	 * 
	 * @param pattern
	 *            Error message string pattern.
	 * @param argNames
	 *            Field of string values inserted into error message pattern.
	 * 
	 * @throws IllegalArgumentException
	 *             ALWAYS!!!
	 */
	private static void throwIllegalArgumentException(
			final String pattern, 
			final Object... argNames
	) {
		throw fillStackTrace(new IllegalArgumentException(String.format(pattern, argNames)));
	}
	
	/** checks if argument is not null. If so, simply returns it. Otherwise throws exception with text "Value returned by method 'metName' must not be null."
	 */
	public static <T> T notNullValue(T obj, String metName) {
		if(obj == null) {
    		throw new IllegalStateException(String.format("Value returned by method '%s' must not be null.", metName));
		}
		return obj;
	}
}
