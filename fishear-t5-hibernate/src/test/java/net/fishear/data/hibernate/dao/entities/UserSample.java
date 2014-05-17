package net.fishear.data.hibernate.dao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import net.fishear.data.generic.entities.AbstractEntity;



@Entity
public class UserSample extends AbstractEntity
{

	@Column(name = "FIRST_NAME", length=64)
	private String firstName;

	@Column(name = "SUR_NAME", length=64)
	private String lastName;

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String surname) {
		this.lastName = surname;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
}
