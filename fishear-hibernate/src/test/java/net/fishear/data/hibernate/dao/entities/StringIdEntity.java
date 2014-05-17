package net.fishear.data.hibernate.dao.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.fishear.data.generic.entities.AbstractStringIdEntity;


@Entity
@Table(name = "TESTSTRTABLE")
public class StringIdEntity extends AbstractStringIdEntity
{

	private String value;
	
	private Long cislo;
	
	@Column(name="XXXID")
	@Id
	public String getId() {
		return super.getId();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getCislo() {
		return cislo;
	}

	public void setCislo(Long cislo) {
		this.cislo = cislo;
	}
	
	
	
}
