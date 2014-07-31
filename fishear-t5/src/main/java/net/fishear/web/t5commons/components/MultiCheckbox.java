package net.fishear.web.t5commons.components;

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
public class MultiCheckbox<T> {

	@Inject
	ComponentResources crsc;
	
	@Parameter(required=true, allowNull=true)
	List<T> list;
	
	@Parameter(name="key", allowNull=true, required=true)
	T id;

	@SetupRender
	void setup(MarkupWriter wr) {
		if(list == null) {
			list = new ArrayList<T>();
		}
		wr.element("span");
		crsc.renderInformalParameters(wr);
	}

	@AfterRender
	void finish(MarkupWriter wr) {
		wr.end();
	}

	public void setId(T id) {
		this.id = id;
	}
	
	public T getId() {
		return id;
	}
	
	public boolean getState() {
		return list.contains(id);
	}
	
	public void setState(boolean fl) {
		if(fl) {
			if(!list.contains(id)) {
				list.add(id);
			}
		} else {
			if(list.contains(id)) {
				list.remove(id);
			}
		}
	}
	
}
