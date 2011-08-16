package net.fishear.utils;


import net.fishear.Interfaces.IdI;
import net.fishear.exceptions.ValidationException;

import org.apache.commons.lang.StringUtils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;

public class Numbers {

	public static final BigDecimal ZERO = new BigDecimal(0);
	
	/** returns long number from 'num'. 
	 * If 'num' id  null, returns 'dft'.
	 */
	public static Long tol(Number num, Long dft) {
		return num == null ? dft : num.longValue();
	}

	/** returns long number from 'num'. 
	 * If 'num' id  null, returns 'dft'.
	 */
	public static Long tol(Object num, Long dft) {
		if(num == null) {
			return dft;
		}
		if(num instanceof Number) {
			return tol((Number)num);
		} else {
			return tol(num.toString(), dft);
		}
	}

	/** returns long number from 'num'. 
	 * If 'num' id  null, returns zero (= 0L).
	 */
	public static Long tol(Number num) {
		return tol(num, 0L);
	}

	/** returns long number parsed from 's'. 
	 * If 's' cannot be parsed as long number (for some reasons), returns 'dft'.
	 */
	public static Long tol(String s, Long dft) {
		if(s == null || (s = s.trim()).length() == 0) {
			return dft;
		}
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException ex) {
			return dft;
		}
	}
	
	/** returns long number parsed from 's'. 
	 * If 's' cannot be parsed as long number (for some reasons), returns zero (= 0L).
	 */
	public static Long tol(String s) {
		return tol(s, 0L);
	}

	/** returns {@link BigDecimal} number parsed from 's'. 
	 * If 's' cannot be parsed as such number (for some reasons), returns 'dft'.
	 */
	public static BigDecimal tobd(String s, BigDecimal dft) {
		if(s == null || (s = s.trim()).length() == 0) {
			return dft;
		}
		try {
			return new BigDecimal(s);
		} catch (NumberFormatException ex) {
			return dft;
		}
	}
	
	/** returns {@link BigDecimal} value of number.
	 * If the 'n' is null or it's double value is NaN, returns 'dft'.
	 * Never throws exception.
	 */
	public static BigDecimal tobd(Number n, BigDecimal dft) {
		if(n != null) {
			if(n instanceof BigDecimal) {
				return (BigDecimal) n;
			}
			double d = n.doubleValue();
			if(!Double.isNaN(d)) {
				return new BigDecimal(d);
			}
		}
		return dft;
	}

	/** returns {@link BigDecimal} value of number.
	 * If the 'n' is null or it's double value is NaN, returns {@link #ZERO}.
	 * It is possible to distinct between zero value and error zero (represented by {@link #ZERO} value) using '==' (equals) operator.
	 * Never throws exception.
	 */
	public static BigDecimal tobd(Number n) {
		return tobd(n, ZERO);
	}

	/** returns double number parsed from 's'. 
	 * If 's' cannot be parsed as double number (for some reasons), returns 'dft'.
	 */
	public static Double tod(String s, Double dft) {
		if(s == null || (s = s.trim()).length() == 0) {
			return dft;
		}
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException ex) {
			return dft;
		}
	}
	
	/** returns double value of number.
	 * If the 'n' is null or it's double value is NaN, returns 'dft'.
	 * Never throws exception.
	 */
	public static Double tod(Number n, Double dft) {
		if(n != null) {
			double d = n.doubleValue();
			if(!Double.isNaN(d)) {
				return d;
			}
		}
		return dft;
	}

	/** returns double value of number.
	 * If the 'n' is null or it's double value is NaN, returns 0.
	 * Never throws exception.
	 */
	public static Double tod(Number n) {
		return tod(n, 0D);
	}

	private static boolean isNumber(String s) {
		return !s.matches("\\p{Alpha}");
	}

	public static Number parseCurrency(String text, NumberFormat format) throws ParseException {
		String s = Texts.removeWhitespaces(text);
		Currency c = format.getCurrency(); 
		String cf;
		if(c != null && (cf = Texts.tos(c.getSymbol())).length() > 0) {
			StringUtils.remove(s, cf);
		}
		if(!isNumber(s)) {
			throw new ParseException(s, 0);
		}
		return format.parse(s);
	}

	public static Number parseDecimal(String text, NumberFormat format) throws ValidationException {
		String s = Texts.removeWhitespaces(text);
		if(s.length() == 0) {
			throw new ValidationException("value-is-not-number", text);
		}
		if(format instanceof DecimalFormat) {
			DecimalFormat df = (DecimalFormat) format;
			if(",".equals(df.getDecimalFormatSymbols())) {
				if(StringUtils.countMatches(s, ".") == 1 && s.indexOf(',') < 0) {
					s = s.replace('.', ',');
				}
			} else if(".".equals(df.getDecimalFormatSymbols())) {
				if(StringUtils.countMatches(s, ",") == 1 && s.indexOf('.') < 0) {
					s = s.replace(',', '.');
				}
			}
		}
		if(!isNumber(s)) {
			throw new ValidationException("value-is-not-number", text);
		}
		try {
			return format.parse(s);
		} catch (ParseException ex) {
			throw new ValidationException("cannot-parse-value", text);
		}
	}

	/** returns element of array 'an' at index 'index'.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #tol(Number)
	 */
	public static Long tol(Number[] an, int index, Long dft) {
		if(an != null && an.length > index) {
			return tol(an[index], dft);
		}
		return dft;
	}

	/** returns element of array 'as' at index 'index' converted to long number.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #tol(Number)
	 */
	public static Long tol(String[] as, int i, Long dft) {
		if(as != null && as.length > i) {
			return tol(as[i], dft);
		}
		return dft;
	}

	/** returns element of array 'an' at index 'index'.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #tod(Number)
	 */
	public static Double tod(Number[] an, int index, Double dft) {
		if(an != null && an.length > index) {
			return tod(an[index], dft);
		}
		return dft;
	}

	/** returns element of array 'as' at index 'index' converted to long number.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #tod(Number)
	 */
	public static Double tod(String[] as, int i, Double dft) {
		if(as != null && as.length > i) {
			return tod(as[i], dft);
		}
		return dft;
	}

	/** returns element of array 'an' at index 'index'.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #tod(Number)
	 */
	public static BigDecimal tobd(Number[] an, int index, BigDecimal dft) {
		if(an != null && an.length > index) {
			return tobd(an[index], dft);
		}
		return dft;
	}

	/** returns element of array 'an' at index 'index'.
	 * In case array is null, it's size is less then 'index' or any other error, returns {@link #ZERO}.
	 * Never throws exception.
	 * @see #tod(Number)
	 */
	public static BigDecimal tobd(Number[] an, int index) {
		return tobd(an, 0, ZERO);
	}

	/** returns element of array 'as' at index 'index' converted to {@link BigDecimal} number.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #tod(Number)
	 */
	public static BigDecimal tobd(String[] as, int i, BigDecimal dft) {
		if(as != null && as.length > i) {
			return tobd(as[i], dft);
		}
		return dft;
	}
	
	/** returns element of array 'as' at index 'index' converted to {@link BigDecimal} number.
	 * In case array is null, it's size is less then 'index' or any other error, returns {@link #ZERO}.
	 * Never throws exception.
	 * @see #tod(Number)
	 */
	public static BigDecimal tobd(String[] as, int i) {
		return tobd(as, i, ZERO);
	}

	/** return true if both n1 and n2 are null, or it's value are the same.
	 */
	public static boolean equals(Number n1, Number n2) {
		if(n1 == null) {
			return n2 == null;
		}
		if(n2 != null) {
			return n1.equals(n2);
		}
		return false;
	}

	/** return true if both n1 and n2 are null, or it's value are the same.
	 */
	public static boolean equalsId(IdI<?> n1, IdI<?> n2) {
		return EntityUtils.equalsId(n1, n2);
	}

	/** returns int number parsed from 's'. 
	 * If 's' cannot be parsed as int number (for some reasons), returns 0.
	 */
	public static int toi(String s) {
		return toi(s, 0);
	}

	/** returns int number parsed from 's'. 
	 * If 's' cannot be parsed as int number (for some reasons), returns 'dft'.
	 */
	public static int toi(String s, int dft) {
		if(s == null || (s = s.trim()).length() == 0) {
			return dft;
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			return dft;
		}
	}

	/** returns int number from 'num'. 
	 * If 'num' is  null, returns 0.
	 */
	public static int toi(Number num) {
		return toi(num, 0);
	}

	/** returns int number from 'num'. 
	 * If 'num' is  null, returns 'dft'.
	 */
	public static int toi(Number num, int dft) {
		return num == null ? dft : num.intValue();
	}

	/** returns element of array 'an' at index 'index'.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #toi(Number)
	 */
	public static int toi(Number[] an, int index, int dft) {
		if(an != null && an.length > index) {
			return toi(an[index], dft);
		}
		return dft;
	}

	/** returns element of array 'as' at index 'index' converted to long number.
	 * In case array is null, it's size is less then 'index' or any other error, returns 'dft'.
	 * Never throws exception.
	 * @see #toi(Number)
	 */
	public static int toi(String[] as, int i, int dft) {
		if(as != null && as.length > i) {
			return toi(as[i], dft);
		}
		return dft;
	}
}
