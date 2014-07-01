package net.fishear.exceptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Class is designed to inform a caller in case some error occurs 
 * Multiple validation exceptions may be passed. The <code>ExceptionHandledComponentBase</code> 
 * components handles these exceptions automatically and produce multiple error messages.
 * inside validations, or some other situation caused any error.
 * 
 * @author terber
 */
public class 
	ValidationExceptionMultiple 
extends 
	ValidationException 
{

	private List<ValidationException> exceptions;
	
	private static final long serialVersionUID = 1L;
	
	public ValidationExceptionMultiple() {
		super(null);
	}

	public ValidationExceptionMultiple(ValidationException... exList) {
		super(null);
		add(exList);
	}

	/**
     * Constructs message with optional parameters.
	 * @param message message text.
	 * @param params params - may miss. Any parameter (if it is passed) must not be null.
	 */
	public ValidationExceptionMultiple(String message, Object... params) {
        super(message, params);
	}

	/**
	 * @return the exceptions
	 */
	@SuppressWarnings("unchecked")
	public List<ValidationException> getExceptions() {
		return (List<ValidationException>) (exceptions == null ? Collections.emptyList() : exceptions);
	}

	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(List<ValidationException> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * Adds one or more exceptions to the list.
	 * 
	 * @param exList exceptions
	 * @return the instance
	 */
	public ValidationExceptionMultiple add(ValidationException... exList) {
		if(exList != null && exList.length > 0) {
			for(ValidationException ex : exList) {
				if(ex != null) {
					if(exceptions == null) {
						exceptions = new ArrayList<ValidationException>();
					}
					exceptions.add(ex);
				}
			}
		}
		return this;
	}

	/**
	 * creates new instance ov {@link ValidationException} that is described by the code and arguments and adds one exception to the list.
	 * 
	 * @param code exception message code
	 * @param args optional arguments
	 * @return the instance 
	 */
	public ValidationExceptionMultiple add(String code, Object... args) {
		
		add(new ValidationException(code, args));
		return this;
	}
	
	/**
	 * if exception list is not empty, throws this instance. 
	 * If the list IS EMPTY, does nothing.
	 */
	public void throwNotEmpty() {
		if(getExceptions().size() > 0) {
			throw this;
		}
	}

	/**
	 * if exception list is not empty, returns this instance. 
	 * If the list IS EMPTY, returns null.
	 */
	public ValidationExceptionMultiple returnNotEmpty() {
		if(getExceptions().size() > 0) {
			return this;
		} else {
			return null;
		}
	}
	
}
