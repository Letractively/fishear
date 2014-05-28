package net.fishear.web.t5.base;

import java.util.ArrayList;
import java.util.List;

import net.fishear.web.t5.components.Tabs;
import net.fishear.web.t5.internal.TabItem;
import net.fishear.web.t5.internal.TabList;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;

//@SupportsInformalParameters
public abstract class 
	AbstractTabComponent
extends
	ComponentBase
{

	TabList tabList = newTabList();

	protected abstract void initTabs(TabList tabList);

	
	/**
	 * designed to be overriden by descendant to allow extended tab list items
	 * 
	 * @return tab list
	 */
	protected TabList newTabList() {
		return new TabList();
	}


	@Parameter(name="zone", allowNull=true, defaultPrefix=BindingConstants.LITERAL)
	@Property
	String zoneName;
	
	@InjectComponent("tabs")
	Tabs tabs;

	Tabs getTabs() {
		return tabs;
	}
	
	@SetupRender
	public void setupRender() {
		getTabs().setTabList(getTabList());
	}

	@Cached
	public TabList getTabList() {
		tabList.clear();
		initTabs(tabList);
		return tabList;
	}
	
	public List<String> getClassNames() {
		List<String> list = new ArrayList<String>();
		for(TabItem tab : getTabList()) {
			list.add(tab.getPageClass().getName());
		}
		return list;
	}
	
	public void selectTab(String tabCode) {
		getTabs().onSelect(tabCode);
	}

	/**
	 *  provides an option for descendants to change default css class 
	 * 
	 * @return the class name. If empty, default is used.
	 */
	public String getMainCssClass() {
		return null;
	}
}
