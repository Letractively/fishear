package net.fishear.utils;

/** commonly used text tools and utils.
 * @author terber
 */
public class Texts {

	
	/** checks and trims string.
	 * @param s input string. If it is null or empty (= contains whitespaces only), this method returns 'dft'. 
	 * Otherwise method returns trimmed version of 's'.
	 * @param dft this value is returned in case 's' is empty. It can be null; in this case 'tos' method returns null in case 's' is empty.
	 * The 'dft' value is returned exactly "as is" => return value can be compared using "==" operator.
	 */
	public static String tos(String s, String dft) {
		if(s == null || (s = s.trim()).length() == 0) {
			return dft;
		}
		return s;
	}
	
	/** checks and trims string.
	 * @param s input string. If it is null or empty (= contains whitespaces only), this method returns empty string.
	 * Otherwise returns trimmed version of 's'.
	 */
	public static String tos(String s) {
		return tos(s, "");
	}

	/** removes all whitespace characters from string.
	 * @return string without whitespaces. Never returns null (returns empty string in case s is null).
	 */
	public static String removeWhitespaces(String s) {
		if(s != null) {
			int slen = s.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < slen; i++) {
				char zn = s.charAt(i);
				if(!Character.isWhitespace(zn)) {
					sb.append(zn);
				}
			}
			return sb.toString();
		}
		return "";
	}

	/** trims string and removes all whitespace characters from within the text. 
	 * Placess exactly one space character in place of any amount of whitespaces. 
	 * @return string with normalized whitespaces. Never returns null (returns empty string in case s is null).
	 */
	public static String normalizeWhitespaces(String s) {
		if(s != null && (s = s.trim()).length() > 0) {
			boolean isWhitespace = false;
			int slen = s.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < slen; i++) {
				char zn = s.charAt(i);
				if(Character.isWhitespace(zn)) {
					if(!isWhitespace) {
						sb.append(' ');
						isWhitespace = true;
					}
				} else {
					sb.append(zn);
					isWhitespace = false;
				}
			}
			return sb.toString();
		}
		return "";
	}

	public static String tos(Object o) {
		return tos(o, "");
	}

	public static String tos(Object o, String dft) {
		if(o == null) {
			return dft;
		}
		return tos(o.toString(), dft);
	}

	/** returns element 'index' from array 'as'.
	 * Returns 'dft' in case array is shorter than 'index', item is null or any other error.
	 * Never throws exception.
	 */
	public static String tos(String[] as, int index, String dft) {
		if(as == null || as.length < index) {
			return dft;
		}
		return tos(as[index], dft);
	}

	/** returns element 'index' from array 'as'.
	 * Returns empty string in case array is shorter than 'index', item is null or any other error.
	 * Never throws exception.
	 */
	public static String tos(String[] as, int index) {
		return tos(as, index, "");
	}

	/** returns string which is joining of s1 and s2. 
	 * If both s1 and s2 are not empty, returns s1 + between + s2 (s1 and s2 are trimmed, between is not).
	 * Otherwise, returns s1 or s2, which is not empty, or empty string (if both are empty).
	 */
	public static final String tos(String s1, String between, String s2) {
		s1 = tos(s1);
		s2 = tos(s2);
		if(s1.length() > 0) {
			if(s2.length() > 0) {
				return s1 + (between == null ? "" : between) + s2;
			} else {
				return s1;
			}
		} else {
			return s2;
		}
	}

	public static String substring(String s, int i, int j) {
		if(j < i) {
			throw new IllegalArgumentException("End index is less than begin index");
		}
		if(s == null) {
			return "";
		}
		if(s.length() <= i) {
			return "";
		}
		if(s.length() <= j) {
			return s.substring(i);
		} else {
			return s.substring(i, j);
		}
	}

	public static String insertAfter(String s, String insertedText, String insertAfterText) {
		int ii;
		if((ii = s.indexOf(insertAfterText)) > 0) {
			ii += insertAfterText.length();
			s = s.substring(0, ii).trim() + insertedText + s.substring(ii).trim();
		}
		return s;
	}

	public static final int EXCLUDE_NULLS = 0x000001;
	public static final int EXCLUDE_EMPTY = 0x000002;
	
	/** creates one string from all elements of 'oa'.
	 * @param oa
	 * @param prefix
	 * @param flags the bit flags to modify functionality ({@link #EXCLUDE_EMPTY},  {@link #EXCLUDE_NULLS} )
	 * @return
	 */
	public static String tos(Object[] oa, String prefix, int flags) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < oa.length; i++) {
			String s = oa[i] == null ? null : tos(oa[i]);
			if((s == null && (flags & EXCLUDE_NULLS) != 0) || (s.length() == 0 && ((flags & EXCLUDE_EMPTY) != 0))) {
				continue;
			}
			sb.append(prefix).append(s);
		}
		return sb.toString();
	}
	
	/** creates one string from all elements of 'oa'.
	 * Using {@link #EXCLUDE_EMPTY} and {@link #EXCLUDE_NULLS} as default flags.
	 * @param oa
	 * @param prefix
	 * @see #tos(Object[], String, int)
	 */
	public static String tos(Object[] oa, String prefix) {
		return tos(oa, prefix, EXCLUDE_EMPTY | EXCLUDE_NULLS);
	}
	
	public static String toName(String s) {
		if(s == null) {
			return null;
		}
		if(s.length() == 0) {
			return "";
		}
		return s.substring(0, 1).toUpperCase().concat(s.substring(1).toLowerCase());
	}
	
	
	/**
	 * trims all strings in array. In cayse any item is null, sets 'dft' value (without change).
	 * Always returns copy of the original string.
	 * 
	 * @param as
	 * @param dft default value that is set in case array element is null
	 * @return trimmed values
	 */
	public static String[] trimAll(String[] as, String dft) {
		if(as == null) {
			return null;
		}
		String[] nas = new String[as.length];
		for(int i = 0; i < as.length; i++) {
			nas[i] = as[i] == null ? dft : as[i].trim();
		}
		return nas;
	}

	/**
	 * sets all elements that are empty (nulls or strings that contains only whitespaces) to given value.
	 * All vales are trimmed, the 'dft' is set without change.
	 * Always returns copy of the original string.
	 * 
	 * @param as the array
	 * @param dft default value that is set in case array element is null or empty
	 * @return trimmed values. If 'as' is null, returns null.
	 */
	public static String[] setAllEmpty(String[] as, String dft) {
		if(as == null) {
			return null;
		}
		String[] nas = new String[as.length];
		String s;
		for(int i = 0; i < as.length; i++) {
			if(as[i] == null || (s = as[i].trim()).length() == 0) {
				s = dft;
			}
			nas[i] = s;
		}
		return nas;
	}

	/**
	 * removes all elements from the array that are null values.
	 * Always returns copy of the original array.
	 * 
	 * @param as the array
	 * @return copy of the array with no null elements.
	 */
	public static String[] removeNulls(String[] as) {
		if(as == null) {
			return null;
		}
		int okCnt = 0;
		for(int i = 0; i < as.length; i++) {
			if(as[i] != null) {
				as[okCnt++] = as[i];
			}
		}
		String[] nas = new String[okCnt];
		System.arraycopy(as, 0, nas, 0, okCnt);
		return nas;
	}

	/**
	 * removes all empty elements (nulls and that containing only whitespaces) from the array.
	 * Always returns copy of the original array.
	 * 
	 * @param as
	 * @return
	 */
	public static String[] removeEmpty(String[] as) {
		return Texts.removeNulls(Texts.setAllEmpty(as, null));
	}
	
}
