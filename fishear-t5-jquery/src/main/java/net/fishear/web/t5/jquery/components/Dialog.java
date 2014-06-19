package net.fishear.web.t5.jquery.components;

import javax.inject.Inject;

import net.fishear.utils.Texts;
import net.fishear.web.t5.base.ComponentBase;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.components.DialogAjaxLink;
import org.got5.tapestry5.jquery.components.DialogLink;

public class Dialog extends ComponentBase {

	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	Block label;
	
	@Parameter(value="")
	@Property
	String params;
	
	@Inject
	JavaScriptSupport jsup;
	
	@Parameter
	Object[] context;
	
	@Parameter(name="event", defaultPrefix=BindingConstants.LITERAL)
	@Property
	String ajaxEvent;

	@Parameter
	String clientId;

	@Component(id="ajaxDialog")
	DialogAjaxLink ajaxDialog;
	
	@Component(id="nonAjax")
	DialogLink dialog;


	/**
	 * @return the context
	 */
	public Object[] getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Object[] context) {
		this.context = context;
	}
	
	@Cached
	public String getClientId() {
		if(Texts.tos(clientId).length() == 0) {
			return jsup.allocateClientId(crsc);
		} else  {
			return clientId;
		}
	}

	/**
	 * @return the label
	 */
	public Block getLabel() {
		return label;
	}

	public Object onActionFromAjaxDialog() {
		return raiseDialogEvent(context);
	}

	public Object raiseDialogEvent(Object[] context) {
		if(Texts.tos(ajaxEvent).length() > 0) {
			crsc.getContainerResources().triggerEvent(ajaxEvent, context, null);
		}
		return ((Zone)crsc.getEmbeddedComponent("dlgBaseZone")).getBody();
	}

}
