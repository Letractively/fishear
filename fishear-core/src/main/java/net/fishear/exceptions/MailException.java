package net.fishear.exceptions;



/**
 * General exception for mail service
 */
public class MailException extends AppException {

	/**
	 * Default serialVersionUID
	 */
	protected static final long serialVersionUID = 1L;

	public MailException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public MailException(String message, Throwable cause){
		super(message, cause);
	}


}

