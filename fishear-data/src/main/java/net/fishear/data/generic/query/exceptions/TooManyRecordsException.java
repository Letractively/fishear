package net.fishear.data.generic.query.exceptions;

import net.fishear.exceptions.AppException;

public class 
	TooManyRecordsException
extends
	AppException
{

	private static final long serialVersionUID = 1L;

	public TooManyRecordsException(String message, Object...params) {
		super(message, params);
	}

}
