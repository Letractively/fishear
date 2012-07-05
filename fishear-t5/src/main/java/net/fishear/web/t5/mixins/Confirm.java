package net.fishear.web.t5.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
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
	private JavaScriptSupport renderSupport;

	@InjectContainer
	private ClientElement element;

	@AfterRender
	public void afterRender() {
		renderSupport.addScript(
			String.format("new Confirm('%s', '%s');", element.getClientId(), confirmMessage())
		);
    }

	protected String confirmMessage() {
		return confirmMessage;
	}
}
