package net.fishear.web.t5.base.bindings;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.BindingSource;

/**
 * Factory for map bindings. Map bindings parse comma-delimited lists of
 * key=value pairs into {@link Map maps}. Keys are always string literals while
 * the values can be any binding expression, for which the default prefix is
 * prop.
 */
public class 
	MapBindingFactory
implements 
	BindingFactory
{

	private final BindingSource bindingSource;

	public MapBindingFactory(BindingSource source) {
		this.bindingSource = source;
	}

	public Binding newBinding(
			String description, 
			ComponentResources container,
			ComponentResources component, 
			String expression, 
			Location location
	) {
		Map<String, Binding> delegates = new HashMap<String, Binding>();
		String[] entries = expression.split(",");
		boolean invariant = true;

		for (String entry : entries) {
			String[] parts = entry.split("=", 2);
			if (parts.length != 2) {
				throw new RuntimeException(String.format("Entry '%s' is malformed", entry));
			}
			String name = parts[0];
			String value = parts[1];

			Binding binding = bindingSource.newBinding(description, container, component, BindingConstants.PROP, value, location);
			invariant = invariant && binding.isInvariant();
			delegates.put(name, binding);
		}

		return new MapBinding(delegates, invariant);
	}

/* ************************************************************************************* */
/* the binding */	
	public static class 
		MapBinding
	extends
		AbstractBinding
	{
	
		private final Map<String, Binding> delegates;
		private final boolean invariant;
	
		public MapBinding(Map<String, Binding> delegates, boolean invariant) {
			this.delegates = delegates;
			this.invariant = invariant;
		}
	
		public Object get() {
			return new TreeMap<String, Object>(delegates);
		}
	
		public boolean isInvariant() {
			return invariant;
		}
	
		@SuppressWarnings({ "rawtypes" })
		public Class<Map> getBindingType() {
			return Map.class;
		}
	}
}