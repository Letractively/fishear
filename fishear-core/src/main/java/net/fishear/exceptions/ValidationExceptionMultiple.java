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
}
