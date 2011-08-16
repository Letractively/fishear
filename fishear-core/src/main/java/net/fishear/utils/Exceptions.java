package net.fishear.utils;


public class Exceptions
{

	
	/** returns root cause of exception chain.
	 * If no cause exists, returns 'ex'.
	 * The limit for parent's is 1000 due potential never-ending loop.
	 */
	public static Throwable getRootCause(Throwable ex) {
		int i = 0;
		Throwable tEx = ex;
		Throwable cause = tEx.getCause();
		while(cause != null && i < 1000) {
			tEx = cause;
			cause = tEx.getCause();
		}
		return i < 1000 ? tEx : ex;
	}
	
	/** returns root cause of exception chain, but only in case it is instance of 'rqCause'.
	 * If no such cause exists, returns null.
	 * The limit for parent's is 1000 due potential never-ending loop.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> T getRootCause(Throwable ex, Class<T> rqCause) {
		Defender.notNull(rqCause, "rqCause");
		if(ex == null) { return null; }
		int i = 0;
		Throwable tEx = ex;
		Throwable cause = tEx.getCause();
		while(!rqCause.isAssignableFrom(tEx.getClass()) && cause != null && i < 1000) {
			tEx = cause;
			cause = tEx.getCause();
		}
		if(i < 1000 && rqCause.isAssignableFrom(tEx.getClass())) {
			return (T) tEx;
		} else {
			return null;
		}
	}

	/** returns runtime exception created on the 'ex' base.
	 * If the 'ex' is reuntime exception, returns itself.
	 * Otherwise, returns wrapped exception to runtime.
	 */
	public static RuntimeException runtime(Throwable ex) {
		if(ex instanceof RuntimeException) {
			return (RuntimeException) ex;
		}
		return new RuntimeException(ex);
	}
}
