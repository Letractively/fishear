package net.fishear.data.audit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.fishear.data.audit.AuditConstants;
import net.fishear.data.generic.entities.AbstractEntity;

@Entity
@Table(name=AuditConstants.AUDIT_CHANGES_TABLE_NAME)
public class AuditChange extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="HEADER_ID")
	private Audit audit;
	
	private String propertyName;

	private String newValue;

	/**
	 * @return the audit
	 */
	@ManyToOne
	@JoinColumn(name="AUDIT_ID")
	public Audit getAudit() {
		return audit;
	}

	/**
	 * @param audit the audit to set
	 */
	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	/**
	 * @return the propertyName
	 */
	@Column(name="PROPERTY_NAME")
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
	 * @return the newValue
	 */
	@Column(name="NEW_VALUE")
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
