package net.fishear.utils;

public class Bools
{

	
	/** returns value of fl. In case 'fl' is bull, returns dft.
	 */
	public static boolean tob(Boolean fl, boolean dft) {
		return fl == null ? dft : fl.booleanValue();
	}

	/** returns value of fl. In case 'fl' is bull, returns false.
	 */
	public static boolean tob(Boolean fl) {
		return tob(fl, false);
	}

	/** returns value of fl. In case 'fl' is bull, returns dft.
	 */
	public static boolean tob(Object o, boolean dft) {
		if(o == null) {
			return dft;
		}
		if(o instanceof Boolean) {
			return tob((Boolean)o, dft);
		}
		return Boolean.valueOf(Texts.tos(o.toString(), String.valueOf(dft)));
	}

	/** returns value of fl. In case 'fl' is bull, returns dft.
	 */
	public static boolean tob(Object o) {
		return tob(o, false);
	}
}
