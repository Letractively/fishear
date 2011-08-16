package net.fishear.web.t5.base.bindings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Binding;
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
	ListBindingFactory
implements 
	BindingFactory
{

	@SuppressWarnings("unused")
	private final BindingSource bindingSource;

	public ListBindingFactory(BindingSource source) {
		this.bindingSource = source;
	}

	public Binding newBinding(
			String description, 
			ComponentResources container,
			ComponentResources component, 
			String expression, 
			Location location
	) {
		List<String> delegates = new ArrayList<String>();
		String[] entries = expression.split(",");
		boolean invariant = true;

		for (String entry : entries) {
			delegates.add(entry);
		}

		return new ListBinding(delegates, invariant);
	}


	public static class 
		ListBinding
	extends
		AbstractBinding
	{

		private final List<String> delegates;
		private final boolean invariant;

		public ListBinding(List<String> delegates, boolean invariant) {
			this.delegates = delegates;
			this.invariant = invariant;
		}

		public Object get() {
			List<String> values = new ArrayList<String>(delegates.size());
			for (String entry : delegates) {
				values.add(entry);
			}
			return values;
		}

		public boolean isInvariant() {
			return invariant;
		}

		@SuppressWarnings({ "rawtypes" })
		public Class<List> getBindingType() {
			return List.class;
		}
	}
}