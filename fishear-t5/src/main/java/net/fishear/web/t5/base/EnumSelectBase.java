package net.fishear.web.t5.base;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import net.fishear.utils.Texts;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;

/**
 * Base cass for selects, that provide select to target enum value.
 * Main benefit is that you need not implement body of the select and may place all logic 
 * connected to the select outside main business class.
 * 
 * @author raterwork
 *
 * @param <T>
 */
@SupportsInformalParameters
public abstract class EnumSelectBase<T> {

	@Parameter
	boolean disabled;
	
	@Parameter
	@Property
	T value;

	@Inject
	ComponentResources crsc;
	
	protected abstract T[] getValues();

	public Map<T, String> getData() {
		try {
			Map<T, String> map = new TreeMap<T, String>();
			T[] vals = getValues();
			Class<? extends Object> clazz = vals[0].getClass();
			
			String fldn = Texts.tos(getFieldname(), null);
			if(fldn == null) {
				for(T val : vals) {
					map.put(val, val.toString());
				}
			} else {
				Field desc = clazz.getDeclaredField("desc");
				for(T val : vals) {
					map.put(val, (String) desc.get(val));
				}
			}
			return map;
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	
	/**
	 * @return name of the field that is used as visible text.
	 * If returns null, "toString()" method is used.
	 */
	protected String getFieldname() {
		return "desc";
	}

	void afterRender(MarkupWriter wr) {
		Element el = wr.getElement().find("select");
		if(el != null) {
			if(disabled) {
				el.attribute("disabled", "true");
			}
			for(String pn : crsc.getInformalParameterNames()) {
				el.attribute(pn, crsc.getInformalParameter(pn, String.class));
			}
		}
		
	}
}
