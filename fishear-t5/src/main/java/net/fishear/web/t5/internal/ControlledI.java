package net.fishear.web.t5.internal;

import net.fishear.web.t5.base.AbstractController;


/** interface should implements all components whose are designed to be controlled by {@link AbstractController}.
 * If any class implements this interface, it can be controlled.
 * @author terber1
 */
public interface 
	ControlledI 
{

	/** sets "main" page, which is responsible for activating and / or switching component currently visible.
	 * @param control
	 * @see #getControlPage()
	 */
	public void setControlPage(AbstractController control);

	/** returns control page, which is set by {@link #setControlPage(AbstractController)} 
	 */
	public AbstractController getControlPage();

}
