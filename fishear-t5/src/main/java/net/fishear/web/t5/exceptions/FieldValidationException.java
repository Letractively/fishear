package net.fishear.web.t5.exceptions;

import net.fishear.exceptions.ValidationException;

import org.apache.tapestry5.Field;


/** this is a variant {@link ValidationException} with field passed directly to constructor.
 * If the superclass has two constructors (one with field and one without it), there is an ambiguity of those constructors.
 * @author terber
 */
public class 
	FieldValidationException 
extends
	ValidationException
{

	private static final long serialVersionUID = 1L;
	private final Field field;

	public FieldValidationException(Field field, String message, Object... params) {
		super(message, params);
		this.field = field;
	}

	/** returns field associated to thiv validation error (if any).
	 * Returns null if no field was associated.
	 */
	public Field getField() {
		return field;
	}
}
