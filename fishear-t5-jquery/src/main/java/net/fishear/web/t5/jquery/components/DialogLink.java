package net.fishear.web.t5.jquery.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.runtime.Component;

import net.fishear.web.t5.base.ComponentBase;

public class DialogLink extends ComponentBase {

	@Parameter(required=true, allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	String dialog;
	
	@Parameter
	@Property
	Object[] context;
		
	
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
		return getDialog().raiseDialogEvent(context);
	}
	
}
