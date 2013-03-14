package net.fishear.utils.data;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

public class EqualsAndDiffEntity {

	@Id
	private Long id;
	
	private String str1;
	
	private String str2;
	
	private Integer intO;
	
	private int intP;
	
	private Long longO;
	
	private long longP;
	
	private Double dblO;
	
	private double dblP;
	
	private Date date1;
	
	private Date date2;

	private Type type;
	
	@Transient
	private String transientField;
	
	private String transientGetter;
	
	
	
	enum Type {
		
		ENTYPE1,
		ENTYPE2
		
	}

	/**
	 * @return the str1
	 */
	public String getStr1() {
		return str1;
	}

	/**
	 * @param str1 the str1 to set
	 */
	public void setStr1(String str1) {
		this.str1 = str1;
	}

	/**
	 * @return the str2
	 */
	public String getStr2() {
		return str2;
	}

	/**
	 * @param str2 the str2 to set
	 */
	public void setStr2(String str2) {
		this.str2 = str2;
	}

	/**
	 * @return the intO
	 */
	public Integer getIntO() {
		return intO;
	}

	/**
	 * @param intO the intO to set
	 */
	public void setIntO(Integer intO) {
		this.intO = intO;
	}

	/**
	 * @return the intP
	 */
	public int getIntP() {
		return intP;
	}

	/**
	 * @param intP the intP to set
	 */
	public void setIntP(int intP) {
		this.intP = intP;
	}

	/**
	 * @return the longO
	 */
	public Long getLongO() {
		return longO;
	}

	/**
	 * @param longO the longO to set
	 */
	public void setLongO(Long longO) {
		this.longO = longO;
	}

	/**
	 * @return the longP
	 */
	public long getLongP() {
		return longP;
	}

	/**
	 * @param longP the longP to set
	 */
	public void setLongP(long longP) {
		this.longP = longP;
	}

	/**
	 * @return the dblO
	 */
	public Double getDblO() {
		return dblO;
	}

	/**
	 * @param dblO the dblO to set
	 */
	public void setDblO(Double dblO) {
		this.dblO = dblO;
	}

	/**
	 * @return the dblP
	 */
	public double getDblP() {
		return dblP;
	}

	/**
	 * @param dblP the dblP to set
	 */
	public void setDblP(double dblP) {
		this.dblP = dblP;
	}

	/**
	 * @return the date1
	 */
	public Date getDate1() {
		return date1;
	}

	/**
	 * @param date1 the date1 to set
	 */
	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	/**
	 * @return the date2
	 */
	public Date getDate2() {
		return date2;
	}

	/**
	 * @param date2 the date2 to set
	 */
	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the transientField
	 */
	public String getTransientField() {
		return transientField;
	}

	/**
	 * @param transientField the transientField to set
	 */
	public void setTransientField(String transientField) {
		this.transientField = transientField;
	}

	/**
	 * @return the transientGetter
	 */
	@Transient
	public String getTransientGetter() {
		return transientGetter;
	}

	/**
	 * @param transientGetter the transientGetter to set
	 */
	public void setTransientGetter(String transientGetter) {
		this.transientGetter = transientGetter;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	

	public static EqualsAndDiffEntity[] fillEntities() {
		EqualsAndDiffEntity te1 = new EqualsAndDiffEntity();
	
		te1.setStr1("AAAA");
		te1.setStr2("BBBB");
		te1.setIntO(111);
		te1.setIntP(222);
		te1.setLongO(1111L);
		te1.setLongP(2222L);
		te1.setDblO(111.5D);
		te1.setDblP(222.5D);
		te1.setDate1(new Date(100000L));
		te1.setDate2(new Date(10000000L));
	
		EqualsAndDiffEntity te2 = new EqualsAndDiffEntity();
		te2.setStr1("aaaa");
		te2.setStr2("bbbb");
		te2.setIntO(99111);
		te2.setIntP(99222);
		te2.setLongO(991111L);
		te2.setLongP(992222L);
		te2.setDblO(99111.5D);
		te2.setDblP(99222.5D);
		te2.setDate1(new Date(99100000L));
		te2.setDate2(new Date(9910000000L));
		
		return new EqualsAndDiffEntity[] { te1, te2 };
	}


}
