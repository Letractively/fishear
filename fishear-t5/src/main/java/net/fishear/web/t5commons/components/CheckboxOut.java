package net.fishear.web.t5commons.components;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;

public class CheckboxOut {

	@Parameter(required=true)
	boolean value;

	
	@Inject
	ComponentResources crsc;
	
	public void setupRender(MarkupWriter wr) {
		Element el;
		if(value) {
			el = wr.element("input", "type", "checkbox", "disabled", "true", "checked", "true");
		} else {
			el = wr.element("input", "type", "checkbox", "disabled", "true");
		}
		crsc.renderInformalParameters(wr);
		wr.end();
	}
	
}
