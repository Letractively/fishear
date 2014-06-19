package net.fishear.web.t5.jquery.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import net.fishear.web.t5.base.ComponentBase;

public class DialogLink extends ComponentBase {

	@Parameter(required=true, allowNull=false, defaultPrefix=BindingConstants.LITERAL)
	String dialog;
	
	@Parameter
	@Property
	Object[] context;
		
	
	private Dialog getDialog() {
		return (Dialog) crsc.getContainerResources().getEmbeddedComponent(dialog);
	}
	
	public String getDialogId() {
		return getDialog().getClientId();
	}

	public Object onActionFromOpenDialog(Object[] context) {
		return getDialog().raiseDialogEvent(context);
	}
	
}
