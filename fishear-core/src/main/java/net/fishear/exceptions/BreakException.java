package net.fishear.exceptions;

public class BreakException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private boolean rollback;

	public BreakException(boolean rollback) {
		this.rollback = rollback;
	}

	public BreakException() {
		super();
	}

	public BreakException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BreakException(String arg0) {
		super(arg0);
	}

	public BreakException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @return the rollback
	 */
	public boolean isRollback() {
		return rollback;
	}

	/**
	 * @param rollback the rollback to set
	 */
	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}
	
	

	
}
