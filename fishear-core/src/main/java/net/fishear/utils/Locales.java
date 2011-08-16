package net.fishear.utils;

public class Locales
{
	
	/** returns language part of locale string in lowercase.
	 */
	public static String getLanguage(String localeCode) {
		String s = localeCode.trim();
		if(s.length() > 0) {
			int ii = s.indexOf('_');
			if(ii > 0) {
				return s.substring(0, ii).trim().toLowerCase();
			}
		}
		return s;
	}

}
