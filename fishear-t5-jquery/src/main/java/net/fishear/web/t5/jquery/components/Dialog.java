package net.fishear.web.t5.jquery.components;

import javax.inject.Inject;

import net.fishear.utils.Texts;
import net.fishear.web.t5.base.ComponentBase;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.got5.tapestry5.jquery.components.DialogAjaxLink;
import org.got5.tapestry5.jquery.components.DialogLink;

/**
 * jQuery dialog that directly contains its content. 
 * Optionally it may render link to open the dialog - either using Ajaxc, or directly from the page.
 * 
 * parameter "event" decides about Ajax. If set, given event is sent to the server and its response (Zone content is expected) is shown. 
 * Otherwise (if "event" parameter is not specified), dialog content is shown directly (without Ajax event).
 * 
 * In case Ajax (= "event" is specified), "context" argument is sent to the server.
 * 
 * Dialog may be also opened using {@link net.fishear.web.t5.jquery.components.DialogLink} with different context.
 * 
 * @author raterwork
 *
 */
public class Dialog extends ComponentBase {

	/**
	 * if specified, it is shown as "open" link.
	 */
	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	Block label;
	
	/**
	 * JQuery Dialog parameters - JSon object.
	 */
	@Parameter(value="{width:750,draggable:true}", defaultPrefix=BindingConstants.LITERAL)
	@Property
	String params;
	
	@Inject
	JavaScriptSupport jsup;
	
	/**
	 * event context used in case Ajax. 
	 */
	@Parameter
	Object[] context;
	
	/**
	 * Event that is invoked on the server (context is passed). 
	 * Its return (Zone content is expected) is shown in the dialog zone.
	 */
	@Parameter(name="event", defaultPrefix=BindingConstants.LITERAL)
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

	/**
	 * event handler for action link (if {@link #label} is defined).
	 * Only calls {@link #raiseDialogEvent(Object[], String)} with {@link #context} and "event" (see {@link #ajaxEvent} )
	 * 
	 * 
	 * @return dialog zone returned by {@link #raiseDialogEvent(Object[], String)}
	 */
	public Object onActionFromAjaxDialog() {
		return raiseDialogEvent(getContext(), getAjaxEvent());
	}

	public Zone getDialogZone() {
		return (Zone)crsc.getEmbeddedComponent("dlgBaseZone");
	}
	
	/**
	 * If event is defined ({@link #ajaxEvent}, calls event handler from target component (where the dialog is used).
	 * 
	 * @param context the context passed to event (see {@link ComponentResources#triggerContextEvent(String, org.apache.tapestry5.EventContext, org.apache.tapestry5.ComponentEventCallback)}
	 * @param ajaxEvent event name
	 * @return dialog zone
	 */
	public Object raiseDialogEvent(Object[] context, String ajaxEvent) {
		if(Texts.tos(ajaxEvent).length() > 0) {
			crsc.getContainerResources().triggerEvent(ajaxEvent, context, null);
		}
		return getDialogZone().getBody();
	}

	/**
	 * @return the ajaxEvent
	 */
	public String getAjaxEvent() {
		return ajaxEvent;
	}

	/**
	 * @param ajaxEvent the ajaxEvent to set
	 */
	public void setAjaxEvent(String ajaxEvent) {
		this.ajaxEvent = ajaxEvent;
	}

}
