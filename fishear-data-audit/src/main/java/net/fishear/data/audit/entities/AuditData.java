package net.fishear.data.audit.entities;

public class AuditData {

	public enum Action {
		INSERT,
		
		UPDATE,
		
		DELETE
	}
	
	private AuditHader header;
	
	private String propertyName;

	private Object oldValue;
	
	private Object newValue;

	private Action action;

	/**
	 * @return the header
	 */
	public AuditHader getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(AuditHader header) {
		this.header = header;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	
}
