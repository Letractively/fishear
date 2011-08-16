/**
 * 
 */
package net.fishear.exceptions;

/** this exception id thrown in case program calls the method (usually in dummy implementation), 
 * which is not implemented but ancestor expects the method should be implemented.
 * @author terber
 */
public class UnimplementedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnimplementedException() {
		super("The method is not implemented");
	}
}