package net.fishear.web.t5.components;

import net.fishear.utils.EntityUtils;
import net.fishear.utils.Texts;
import net.fishear.web.t5.base.AbstractTabComponent;
import net.fishear.web.t5.base.ComponentBase;
import net.fishear.web.t5.internal.TabItem;
import net.fishear.web.t5.internal.TabList;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.util.UnknownValueException;
import org.apache.tapestry5.runtime.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Layout component for pages of application dashboard-skeleton.
 */

public class Tabs extends ComponentBase
{

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static final TabItem  DUMMY = new TabItem();
	
	@Persist
	private String selected;

	@Parameter(name="zone", defaultPrefix=BindingConstants.LITERAL, allowNull=true)
	String zoneName_;
	
	@Parameter(name="class", allowNull=false)
	String mainCssClass;

	@Property
	TabItem tab;

	@Parameter(allowNull=false, cache=true, defaultPrefix=BindingConstants.PROP)
	private TabList tabList;

	@Parameter(allowNull=false, autoconnect=true)
	String title;

	@SetupRender
	public void setupRender() {
		if(selected == null || selected.trim().length() == 0) {
			TabList tl = getTabList();
			if(tl == null || tl.size() == 0) {
				selected = "no_tab";
			} else {
				selected = tl.get(0).getName();
			}
		}
	}
	
	@Cached
	public String getZoneName() {
		if(zoneName_ != null) {
			return zoneName_;
		}
		return EntityUtils.getValue("zoneName", crsc.getContainer(), null);
	}
	
	public TabItem getSelectedTab() {
		TabList tl = getTabList();
		if(tl == null || tl.size() == 0) {
			return DUMMY;
		}
		TabItem tab = tl.get(0);
		if(selected != null && selected.trim().length() > 0) {
			String sel = selected.trim();
			for(TabItem ti : getTabList()) {
				if(sel.equalsIgnoreCase(ti.getName())) {
					tab = ti;
					break;
				}
			}
		}
		return tab;
	}

	public Object getSelected() {
		return getSelectedTab().getPageObject();
	}

	/**
	 * @return the zone with 'mainContentZone' component, if such exists in component hierarchy up to root page. 
	 */
	public Zone getContentZone() {

		ComponentResources crsc = this.crsc;

		Component page = crsc.getPage();
		Component comp = crsc.getComponent();
		
		Component zone = null;
		
		while(comp != page && zone == null) {
			try {
				zone = crsc.getEmbeddedComponent(getZoneName());
			} catch(UnknownValueException ex) { }
			crsc = crsc.getContainerResources();
		}
		return (Zone) zone;
	}
	
	public Object onSelect(String what) {
		log.debug("Select called for tab code '{}'. Zone name is '{}'", what, getZoneName());
		selected = what;
		Zone z;
		if(getZoneName() != null && request.isXHR() && (z = getContentZone()) != null) {
			return z.getBody();
		}
		return null;
	}

	public String getCssClass() {
		return tab != null && tab.getName().equalsIgnoreCase(selected) ? " current" : "";
	}
	
	public String getTabTitle() {
		return translate(tab.getName().concat("-label"));
	}

	public String getPageTitle() {
		if(title != null) {
			return title;
		}
		return translate(getSelectedTab().getName().concat("-title"));
	}

	/**
	 * @return the tabList
	 */
	public TabList getTabList() {
		if(tabList == null) {
			if(crsc.getContainer() instanceof AbstractTabComponent) {
				tabList = ((AbstractTabComponent)crsc.getContainer()).getTabList();
			} else {
				throw new IllegalStateException(String.format("The 'tabList' is null. Pass 'tabList' parameter to the TabLayout, or let page to extend '%s'", AbstractTabComponent.class.getName()));
			}
		}
		return tabList;
	}

	/**
	 * @param tabList the tabList to set
	 */
	public void setTabList(TabList tabList) {
		this.tabList = tabList;
	}

	public String getScreenId() {
		return getSelectedTab().getScreenId();
	}

	/**
	 * @param zoneName the zoneName_ to set
	 */
	public void setZoneName(String zoneName) {
		this.zoneName_ = zoneName;
	}

	/**
	 * @return the mainCssClass
	 */
	public String getMainCssClass() {
		if(mainCssClass != null) { 
			return mainCssClass;
		}
		return Texts.tos(EntityUtils.getValue("mainCssClass", crsc.getContainer(), null), "t5-tab-panel");
	}

	/**
	 * @param mainCssClass the mainCssClass to set
	 */
	public void setMainCssClass(String mainCssClass) {
		this.mainCssClass = mainCssClass;
	}
}
