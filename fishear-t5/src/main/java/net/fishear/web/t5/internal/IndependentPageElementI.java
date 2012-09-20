package net.fishear.web.t5.internal;

/** provides method to interact control page (usually menu or tab page) with this component.
 * @author terber
 */
public interface 
	IndependentPageElementI 
{

	/** returns localized page title. 
	 * Returned value will be displayed in page header in case this component is selected as main page's component.
	 * @return
	 */
	public abstract String getPageTitle();
	
	public abstract void setInitialState();
}
