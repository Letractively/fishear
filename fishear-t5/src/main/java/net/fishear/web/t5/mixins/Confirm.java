package net.fishear.web.t5.mixins;

import javax.inject.Inject;

import net.fishear.utils.Texts;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A simple mixin for attaching a javascript confirmation box to the onclick
 * event of any component that implements ClientElement.
 * 
 * Informal pamameter "confirmMessage" can set message text.
 * 
 */
@Import(library = "confirm.js")
@SupportsInformalParameters
public class Confirm {

//	@Parameter(value = "message:are-you-sure-question", defaultPrefix = BindingConstants.PROP)
//	private String confirmMessage;

	@Inject
	ComponentResources crsc;

	@Inject
	private JavaScriptSupport js;

	@InjectContainer
	private ClientElement element;
	
	@Inject @Value("jquery.alias")	// should be reference to constant, but there is no reference to T5 JQuery libraries
	String jqPath;

	@AfterRender
	public void afterRender() {
		
		String msg = confirmMessage();

		if(!Texts.isEmpty(msg)) {
			if(Texts.isEmpty(jqPath)) {
				js.addScript(String.format("new Confirm('%s', '%s');", element.getClientId(), msg));
			} else {
				js.addInitializerCall("confirmation", new JSONObject("id", this.element.getClientId(), "message",  msg));
			}
		}
    }

	protected String confirmMessage() {
		return crsc.getInformalParameter("confirmMessage", String.class);
	}
}
