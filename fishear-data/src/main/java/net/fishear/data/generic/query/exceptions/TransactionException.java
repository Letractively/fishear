package net.fishear.data.generic.query.exceptions;

import net.fishear.exceptions.AppException;

public class 
	TransactionException 
extends 
	AppException 
{

	private static final long serialVersionUID = 1L;

	public TransactionException(Throwable cause) {
		super(cause.getMessage(), cause);
        setRootException(cause);
	}

	public TransactionException(String message, Throwable cause){
		super(message, cause);
        setRootException(cause);
	}

    public TransactionException(String message) {
        super(message);
    }
}
