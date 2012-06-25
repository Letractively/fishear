package net.fishear.utils.data;

import javax.persistence.Id;

public class TestEntity2 {

	private Long idNum;

	private String name;
	
	private String code;
	
	/**
	 * @return the id
	 */
	@Id
	public Long getIdNum() {
		return idNum;
	}

	/**
	 * @param id the id to set
	 */
	public void setIdNum(Long id) {
		this.idNum = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	
	
}
