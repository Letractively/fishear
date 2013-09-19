package net.fishear.utils.data;

import javax.persistence.Transient;

public class TestEntity {
	
	private String id;
	
	private String val1;
	
	private String val2;
	
	private String val3;

	private Long num1;
	
	private Long num2;
	
	private Long num3;
	
	private boolean bool1;
	
	private Boolean bool2;
	
	private TestEntity2 testEntity2;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the val1
	 */
	public String getVal1() {
		return val1;
	}

	/**
	 * @param val1 the val1 to set
	 */
	public void setVal1(String val1) {
		this.val1 = val1;
	}

	/**
	 * @return the val2
	 */
	public String getVal2() {
		return val2;
	}

	/**
	 * @param val2 the val2 to set
	 */
	public void setVal2(String val2) {
		this.val2 = val2;
	}

	/**
	 * @return the val3
	 */
	public String getVal3() {
		return val3;
	}

	/**
	 * @param val3 the val3 to set
	 */
	public void setVal3(String val3) {
		this.val3 = val3;
	}

	/**
	 * @return the num1
	 */
	public Long getNum1() {
		return num1;
	}

	/**
	 * @param num1 the num1 to set
	 */
	public void setNum1(Long num1) {
		this.num1 = num1;
	}

	/**
	 * @return the num2
	 */
	public Long getNum2() {
		return num2;
	}

	/**
	 * @param num2 the num2 to set
	 */
	public void setNum2(Long num2) {
		this.num2 = num2;
	}

	/**
	 * @return the testEntity2
	 */
	public TestEntity2 getTestEntity2() {
		return testEntity2;
	}

	/**
	 * @param testEntity2 the testEntity2 to set
	 */
	public void setTestEntity2(TestEntity2 testEntity2) {
		this.testEntity2 = testEntity2;
	}

	/**
	 * @return the bool1
	 */
	public boolean isBool1() {
		return bool1;
	}

	/**
	 * @param bool1 the bool1 to set
	 */
	public void setBool1(boolean bool1) {
		this.bool1 = bool1;
	}

	/**
	 * @return the num3
	 */
	@Transient
	public Long getNum3() {
		return num3;
	}

	/**
	 * @param num3 the num3 to set
	 */
	public void setNum3(Long num3) {
		this.num3 = num3;
	}

	/**
	 * @return the bool2
	 */
	public Boolean getBool2() {
		return bool2;
	}

	/**
	 * @param bool2 the bool2 to set
	 */
	public void setBool2(Boolean bool2) {
		this.bool2 = bool2;
	} 
	

}
