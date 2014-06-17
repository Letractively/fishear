package net.fishear.web.t5.components;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;

@SupportsInformalParameters
public class MultiCheckbox {

	@Inject
	ComponentResources crsc;
	
	@Parameter(required=true, allowNull=true)
	List<?> list;
	
	@Parameter(name="key", allowNull=true, required=true)
	Object id;

	@SetupRender
	void setup(MarkupWriter wr) {
		if(list == null) {
			list = new ArrayList<Object>();
		}
		wr.element("span");
		crsc.renderInformalParameters(wr);
	}

	@AfterRender
	void finish(MarkupWriter wr) {
		wr.end();
	}

	public void setId(Object id) {
		this.id = id;
	}
	
	public Object getId() {
		return id;
	}
	
	public boolean getState() {
		return list.contains(id);
	}
	
	@SuppressWarnings("unchecked")
	public void setState(boolean fl) {
		if(fl) {
			if(!list.contains(id)) {
				((List<Object>)list).add(id);
			}
		} else {
			if(list.contains(id)) {
				list.remove(id);
			}
		}
	}
	
}
