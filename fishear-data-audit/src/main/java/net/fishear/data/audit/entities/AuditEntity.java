package net.fishear.data.audit.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.fishear.data.audit.AuditConstants;
import net.fishear.data.generic.entities.GenericEntity;

@Entity
@Table(name=AuditConstants.AUDIT_ENTITY_TABLE_NAME)
public class AuditEntity extends GenericEntity<Integer> {


	private String className;
	
	private Integer id;

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the id
	 */
	@Id
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
