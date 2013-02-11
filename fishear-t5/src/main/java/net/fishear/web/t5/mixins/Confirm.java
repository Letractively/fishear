package net.fishear.web.t5.mixins;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A simple mixin for attaching a javascript confirmation box to the onclick
 * event of any component that implements ClientElement.
 */
@Import(library = "confirm.js")
public class Confirm {

	@Parameter(value = "message:are-you-sure-question", defaultPrefix = BindingConstants.PROP)
	private String confirmMessage;

	@Inject
	private JavaScriptSupport js;

	@InjectContainer
	private ClientElement element;
	
	@Inject @Value("jquery.alias")	// should be reference to constant, but there is no reference to T5 JQuery libraries
	String jqPath;

	@AfterRender
	public void afterRender() {
		if(jqPath == null || jqPath.trim().length() == 0) {
			js.addScript(String.format("new Confirm('%s', '%s');", element.getClientId(), confirmMessage()));
		} else {
			js.addInitializerCall("confirmation", new JSONObject("id", this.element.getClientId(), "message",  confirmMessage()));
		}
    }

	protected String confirmMessage() {
		return confirmMessage;
	}
}
