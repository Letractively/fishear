package net.fishear.web.t5.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * The form item with label and content.
 * 
 * @author terber
 *
 */
public class Item
{

	/**
	 * CSS class (optional).
	 */
	@Parameter(name = "class", defaultPrefix = BindingConstants.LITERAL)
	private String cssClass;

	/**
	 * the component ID (optional).
	 */
	@Parameter(name = "id", defaultPrefix = BindingConstants.LITERAL)
	private String id;

	/**
	 * Label's "for" (optional)
	 */
	@Parameter(name = "for", defaultPrefix = BindingConstants.LITERAL)
	private String forId;

	/**
	 * if true, the label part and the content part ale split to tho <td>...</td> tags.
	 * Otherwise, they are rendered as divs and label.
	 */
	@Parameter
	private Boolean inTable;

	/**
	 * optional, if rendered as table cells, the colspan value.
	 */
	@Parameter
	private String colspan;
	
	/**
	 * the label text. 
	 */
	@Parameter(defaultPrefix = BindingConstants.MESSAGE, allowNull = true)
	private String label;
	
	@Inject
	private ComponentResources crsc;
	
	@BeforeRenderBody
	void beforeRenderBody(MarkupWriter wr) {
		setInTable(wr);
		String id = this.id == null || this.id.trim().length() == 0 ? null : this.id.trim();
		if(inTable) {
			wr.element("td");
			if(id != null) { wr.attributes("id", id.concat("_lbl")); }
			wr.attributes("class", cssClass);
			crsc.renderInformalParameters(wr);
		} else {
			wr.element("div");
			wr.attributes("id", id, "class", cssClass);
			crsc.renderInformalParameters(wr);
		}
		
		if(label != null) {
			wr.element("label");
			if(id != null) {
				wr.attributes("id", id.concat("_label"));
			}
			wr.attributes("class", cssClass, "id", id, "for", forId);
			wr.writeRaw(label);
			wr.end();
		}
		if(inTable) {
			wr.end();
			wr.element("td");
			if(id != null) {
				wr.attributes("id", id.concat("_val"));
			}
			wr.attributes("colspan", colspan);
		}
	}

	private void setInTable(MarkupWriter wr) {
		Element firstTable = null;
		Element firstForm = null;
		int distance = -1;
		if(inTable == null) {
			Element doc = wr.getElement();
			Element par = doc.getContainer();
			while(par != null && (firstTable == null || firstForm == null)) {
				String eln = par.getName();
				if(distance >= 0) { distance++; }
				if(eln.equals("table") && firstTable == null) {
					firstTable = par;
					if(distance < 0) distance = 0;
				} else if(eln.equals("form") && firstForm == null) {
					firstForm = par;
					if(distance < 0) distance = 0;
				}
				String s = par.getAttribute("inTable");
				if((s) != null && Boolean.valueOf(s)) {
					inTable = true;
					return;
				}
				par = par.getContainer();
			}
		}
		inTable = false;
		if(firstTable != null && firstForm != null) {
			if(distance == 1) {
				inTable = true;
			}
		}
	}

	@AfterRenderBody
	void AfterRenderBody(MarkupWriter wr) {
		wr.end();
	}
}
