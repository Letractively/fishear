package net.fishear.web.t5.base;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.Numbers;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;

/**
 * Base class for selects, that provide direct select into target entity of given type.
 * That means the {@link #value} if the entity itself - not an ID or code. Entity type passed as class parameter.
 * 
 * @author raterwork
 *
 * @param <T>
 */
@SupportsInformalParameters
public abstract class SelectBase<T extends EntityI<?>> {

	@Parameter
	@Property
	boolean isSubmit;
	
	@Inject
	ComponentResources crsc;

	@Parameter(defaultPrefix=BindingConstants.LITERAL) 
	@Property
	String zone;
	
	@Parameter(name="value")
	T value;

	@Parameter(allowNull=false)
	private boolean disabled;

	protected abstract String getVisibleText(T en);
	
	protected abstract ServiceI<T> getService();


	@SetupRender
	void setupRender() {
		if(zone != null) {
			isSubmit = true;
		}
	}

	/**
	 * @return list of sites for combo
	 */
	public Map<String, String> getData() {
		QueryConstraints qc = QueryFactory.create();
		
		modifyConstraints(qc);

		Map<String, String> map = new TreeMap<String, String>();
		
		for(T en : getService().list(qc)) {
			map.put(en.getIdString(), getVisibleText(en));
		}
		
		return map;
	}

	/**
	 * for successors to allow change default query constraints before query persistent storage.
	 * 
	 * @param qc default consteraints
	 */
	protected void modifyConstraints(QueryConstraints qc) {
		
	}

	/**
	 * @return the site
	 */
	public String getTheId() {
		return value == null ? null : value.getIdString();
	}

	/**
	 * @param site the site to set
	 */
	public void setTheId(String id) {
		this.value = getService().read(Numbers.tol(id));
	}

	void afterRender(MarkupWriter wr) {
		Element el = wr.getElement().find("select");
		if(disabled) {
			el.attribute("disabled", "true");
		}
		if(isSubmit) {
			el.attribute("onChange", "form.submit()");
		}
		for(String key : crsc.getInformalParameterNames()) {
			el.attribute(key, crsc.getInformalParameter(key, String.class));
		}
	}
	
	public void selectFirst() {
		setTheId(getData().keySet().iterator().next());
	}

	public void select(int cnt) {

		Iterator<String> it = getData().keySet().iterator();
		
		while(cnt-- > 0 && it.hasNext()) {
			it.next();
		}
		if(it.hasNext()) {
			setTheId(it.next());
		}
	}

	public void selectLast() {
		Iterator<String> it = getData().keySet().iterator();
		String key = null;
		while(it.hasNext()) {
			key = it.next();
		}
		if(key != null) {
			setTheId(key);
		}
	}
}
