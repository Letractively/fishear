package net.fishear.exceptions;

public class 
	GeneralException
extends 
	RuntimeException 
{

	private static final long serialVersionUID = 1L;

	public GeneralException() {
		
	}

	public GeneralException(String message) {
		super(message);
	}

	public GeneralException(Throwable cause) {
		super(cause);
	}

	public GeneralException(String message, Throwable cause) {
		super(message, cause);
	}

}
