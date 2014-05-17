package net.fishear.data.audit.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.fishear.data.audit.AuditConstants;
import net.fishear.data.generic.entities.AbstractEntity;
import net.fishear.data.generic.entities.EntityConstants;
import net.fishear.data.generic.services.AuditServiceI.Action;

@Entity
@Table(name=AuditConstants.AUDIT_HEADERS_TABLE_NAME)
public class Audit extends AbstractEntity {

	private Action action;

	private List<AuditChange> changes;
	
	private AuditedEntity auditedEntity;

	private Date actionDate;
	
	private String actionUser;
	
	private String objectId;
	
	/**
	 * change number in the same entity and onject ID
	 */
	private Long changeNumber;

	/**
	 * @return the propertiesChanged
	 */
	@OneToMany(mappedBy="audit", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<AuditChange> getChanges() {
		return changes;
	}

	/**
	 * @param propertiesChanged the propertiesChanged to set
	 */
	public void setChanges(List<AuditChange> propertiesChanged) {
		this.changes = propertiesChanged;
	}
	
	/**
	 * @return the action
	 */
	@Column(name="ACTION")
	@Enumerated(EnumType.STRING)
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	
	public void setActionDate(Date createDate) {
		this.actionDate = createDate;
	}

	@Column(name = AuditConstants.AUDIT_COLUMN_ACTION_DATE)
	public Date getActionDate() {
		return actionDate;
	}

	public void setActionUser(String createUserId) {
		this.actionUser = createUserId;
	}

	@Column(name = AuditConstants.AUDIT_COLUMN_ACTION_USER, length=EntityConstants.USERID_LENGTH)
	public String getActionUser() {
		return actionUser;
	}

	/**
	 * @return the auditedEntity
	 */
	@ManyToOne
	@JoinColumn(name="ENTITY_CLASS_ID")
	public AuditedEntity getAuditedEntity() {
		return auditedEntity;
	}

	/**
	 * @param auditedEntity the auditedEntity to set
	 */
	public void setAuditedEntity(AuditedEntity entity) {
		this.auditedEntity = entity;
	}

	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the changeNumber
	 */
	public Long getChangeNumber() {
		return changeNumber;
	}

	/**
	 * @param changeNumber the changeNumber to set
	 */
	public void setChangeNumber(Long changeNumber) {
		this.changeNumber = changeNumber;
	}
}
