package net.fishear.exceptions;

public class 
	CoercerNotFoundException
extends
	AppException
{
	private static final long serialVersionUID = 1L;

	public CoercerNotFoundException(String msg, Object... params) {
		super(msg, params);
	}

}
