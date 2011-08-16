package net.fishear.web.entities;

import javax.persistence.Transient;

import net.fishear.data.generic.entities.AbstractEntity;



//@Entity
//@Table(name="property")
public class 
	AppProperty
extends
	AbstractEntity
{

	private String key;

	private String value;
	
	private Boolean active;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getActive() {
		return active;
	}

	@Transient
	public boolean isActive() {
		return active == null ? true : active.booleanValue();
	}
}
