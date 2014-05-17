package net.fishear.exceptions;


/**
 * Class is designed to inform a caller in case some error occurs 
 * inside validations, or some other situation caused any error.
 * 
 * @author terber
 */
public class 
	ValidationException 
extends 
	AppException 
{

	private static final long serialVersionUID = 1L;

	/**
     * Constructs message with optional parameters.
	 * @param message message text.
	 * @param params params - may miss. Any parameter (if it is passed) must not be null.
	 */
	public ValidationException(String message, Object... params) {
        super(message, params);
	}

}
