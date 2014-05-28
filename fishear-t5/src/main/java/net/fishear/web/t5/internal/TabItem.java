package net.fishear.web.t5.internal;

import javax.xml.bind.annotation.XmlAttribute;

public class TabItem {

	private String title;

	private Class<?> pageClass;

	private String name;

	private Object pageObject;
	
	private String screenId;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the pageClass
	 */
	public Class<?> getPageClass() {
		return pageClass;
	}

	/**
	 * @param pageClass the pageClass to set
	 */
	public void setPageClass(Class<?> pageClass) {
		this.pageClass = pageClass;
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
	 * @return the pageObject
	 */
	public Object getPageObject() {
		return pageObject;
	}

	/**
	 * @param pageObject the pageObject to set
	 */
	public void setPageObject(Object pageObject) {
		this.pageObject = pageObject;
	}

	/**
	 * @return the screenId
	 */
	@XmlAttribute(name="screenId")
	public String getScreenId() {
		return screenId;
	}

	/**
	 * @param screenId the screenId to set
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

}
