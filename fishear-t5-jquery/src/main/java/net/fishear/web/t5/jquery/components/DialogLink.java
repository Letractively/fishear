package net.fishear.web.t5.jquery.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.runtime.Component;

import net.fishear.web.t5.base.ComponentBase;

/**
 * Control that can "open" {@link Dialog}.
 * 
 * Alternative context may be passed to the server handler, then is specified at oridinal dialog.
 * The event (that is invoked on the server) is specified at the dialog. 
 * 
 * It ia also possible to specify alternative event. 
 * In such case different event handler is called for dialog's own button and different for this link.
 * 
 * @author ffyxrr
 *
 */
public class DialogLink extends ComponentBase {

	@Parameter(required=true, allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	String dialog;
	
	/**
	 * context that is passed to server event handler.
	 */
	@Parameter
	@Property
	Object[] context;

	@Parameter(name="event")
	String ajaxEvent;
	
	
	/**
	 * searches for dialog in component hierarchy.
	 * 
	 * @return the dialog
	 * @throws re-thrown T5 exception from {@link ComponentResources#getEmbeddedComponent(String)} method if dialog not found
	 */
	private Dialog getDialog() {
		
		ComponentResources crsc = this.crsc;

		Component page = crsc.getPage();
		
		RuntimeException lastex = null;
		
		while(crsc.getComponent() != page) {
			try {
				return (Dialog) crsc.getContainerResources().getEmbeddedComponent(dialog);
			} catch(RuntimeException ex) {
				if(lastex == null) {
					lastex = ex;
				}
			}
			crsc = crsc.getContainerResources();
		}
		throw lastex;
	}
	
	public String getDialogId() {
		return getDialog().getClientId();
	}

	public Object onActionFromOpenDialog(Object[] context) {
		Dialog dlg = getDialog();
		if(ajaxEvent != null) {
			return dlg.raiseDialogEvent(context, ajaxEvent);
		} else {
			return dlg.raiseDialogEvent(context, dlg.getAjaxEvent());
		}
	}
	
}
