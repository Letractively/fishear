package net.fishear.exceptions;


/**
 * General exception for application. Must be used as parent for all application exception.
 * The message (returned by {@link #getMessage()} method) with params (returned by {@link #getParams()} method) can be
 * localized using an appropriate toolkit or a framework mechanism.
 */
public class 
	AppException 
extends 
	GeneralException
{

    private Throwable rootException;

	protected static final long serialVersionUID = 1L;

    private static final Object[] EOA = new Object[0];
    private Object[] params;

    /**
     * @param message
     * @param e
     * @param params  - used for formatting localized messages
     */
    public AppException(String message, Exception e, Object... params) {
        super(message, e);
        this.params = params == null ? EOA : params;
    }

    public AppException(Exception e) {
        super(e);
        params = EOA;

    }

    public AppException(String message, Object... params) {
        super(message);        
		this.params = params == null ? EOA : params;
		for (int i = 0; i < this.params.length; i++) {
			if(this.params[i] == null) {
				this.params[i] = "(null)";
			}
		}
    }

    public Object[] getParams() {
        return params;
    }

    public Throwable getRootException() {
        return rootException;
    }

    public void setRootException(Throwable rootException) {
        this.rootException = rootException;
    }
}
