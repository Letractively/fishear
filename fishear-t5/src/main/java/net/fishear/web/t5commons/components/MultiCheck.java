package net.fishear.web.t5commons.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;

public class MultiCheck {

	
	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	String selector;
	
	@Inject
	ComponentResources crsc;
	
	@SetupRender
	void setup(MarkupWriter wr) {
		wr.element("input", "type", "checkbox", "onclick", String.format("{var fl = this.checked; jQuery('%s').each(function(){this.checked=fl;})}", selector));
		crsc.renderInformalParameters(wr);
		wr.end();
	}
	
}
