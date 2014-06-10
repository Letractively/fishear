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

	public enum Type {
		WARN,
		ERROR,
		INFO,
		OK
	}
	
	private static final long serialVersionUID = 1L;
	
	private Type type = Type.ERROR;

	/**
     * Constructs message with optional parameters.
	 * @param message message text.
	 * @param params params - may miss. Any parameter (if it is passed) must not be null.
	 */
	public ValidationException(String message, Object... params) {
        super(message, params);
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 * @return this instance
	 */
	public ValidationException setType(Type type) {
		this.type = type;
		return this;
	}

}
