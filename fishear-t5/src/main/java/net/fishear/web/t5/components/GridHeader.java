package net.fishear.web.t5.components;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SupportsInformalParameters
public class GridHeader {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	ComponentResources crsc;
	
	@Inject
	JavaScriptSupport js;

	@Parameter(name="for", defaultPrefix=BindingConstants.LITERAL, required=true, allowNull=false)
	String forGrid;

	@SetupRender
	void setup(MarkupWriter wr) {
		wr.element("div", "id", getClientId());
		crsc.renderInformalParameters(wr);
	}
	
	void AfterRender(MarkupWriter wr) {
		wr.end();
		moveToGridHeader(wr);
	}
	
	public void moveToGridHeader(MarkupWriter wr) {

		try {
			Element grid = wr.getDocument().getElementById(forGrid);
			if(grid == null) {
				log.debug("Grid not dound. This {} component must be places AFTER parent grid in template, the grid must have client ID assigned to that is passed to this component.", getClass().getName());
//				throw new IllegalStateException(String.format("Grid not dound. This %s component must be places AFTER parent grid in template, the grid must have client ID assigned to that is passed to this component.", getClass().getName()));
			} else {
				Element expButs = wr.getDocument().getElementById(getClientId());
		
				Element tr = grid.find("thead").elementAt(-1, "tr");
				Element td = tr.element("th", "colspan", "999", "class", "t-extra-header");
				expButs.moveToBottom(td);
			}
	
		} catch (Exception ex) {
			log.warn("Cannot move buttons to grid header", ex);
		}
	}

	@Cached
	String getClientId() {
		return js.allocateClientId(crsc);
	}

}
