package net.fishear.web.t5.internal;

import java.util.ArrayList;

public class TabList extends ArrayList<TabItem> {

	private static final long serialVersionUID = 1L;

	/**
	 * creates new tab item, sets name and component, adds it to list and returns it to allow cubsequent set of any other property.
	 * @param name the name
	 * @param component the componetn instance
	 * @return newly created instance
	 */
	public TabItem add(String name, Object component) {
		TabItem ti = new TabItem();
		ti.setName(name);
		ti.setPageObject(component);
		super.add(ti);
		return ti;
	}
	
	public TabItem add(String name, Object component, String screenId) {
		TabItem ti = add(name, component);
		ti.setScreenId(screenId);
		return ti;
	}
}