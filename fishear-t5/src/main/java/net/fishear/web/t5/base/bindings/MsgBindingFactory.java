package net.fishear.web.t5.base.bindings;

import net.fishear.utils.EntityUtils;
import net.fishear.utils.Texts;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.AbstractBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;

/**
 * binds "msg:" key to search message hierarchically from component to the root page.
 * Also additional parameters are evaluated.
 * 
 */
public class 
	MsgBindingFactory
implements 
	BindingFactory
{

	public Binding newBinding(
			String description, 
			ComponentResources parent, 
			ComponentResources component, 
			String expression, 
			Location location
	) {
		
		String[] ka = Texts.trimAll(expression.split(","), "");

		ComponentResources crsc = component;
		ComponentResources prev = null;

		// search for propert resources with existing key
		while(crsc.getContainerResources() != null && !crsc.getContainerResources().equals(crsc) && !crsc.getMessages().contains(ka[0]) && crsc != prev) {
			prev = crsc;
			crsc = component.getContainerResources();
		}
		String messageValue;
		if(ka.length == 1) {
			messageValue = crsc.getMessages().get(ka[0]);
		} else {
			try {
				String[] nka = new String[ka.length - 1];
				for(int i = 1; i < ka.length; i++) {
					if(ka[i].startsWith("message:")) {
						nka[i - 1] = component.getMessages().get(ka[i].substring(8).trim());
					} else if(ka[i].startsWith("msg:")) {
						nka[i - 1] = newBinding(description, parent, crsc, ka[i].substring(4).trim(), location).get().toString();
					} else if(ka[i].startsWith("literal:")) {
						nka[i - 1] = ka[i].substring(8);
					} else {
						String key;
						if(ka[i].startsWith("prop:")) {
							key = ka[i].substring(5);
						} else {
							key = ka[i];
						}
						nka[i - 1] = String.valueOf(EntityUtils.getValue(component, key, null));
					}
				}
				messageValue = crsc.getMessages().format(ka[0], (Object[])nka);
			} catch(Exception ex) {
				throw new IllegalStateException(ex);
			}
		}

		return new XBinding(location, description, messageValue);
	}

	public static class XBinding extends AbstractBinding {

	    private final String description;

	    private final Object value;

	    public XBinding(Location location, String description, Object value) {
	        super(location);
	        this.description = description;
	        this.value = value;
	    }

	    public Object get() {
	        return value;
	    }

	    @Override
	    public String toString() {
	        return String.format("LiteralBinding[%s: %s]", description, value);
	    }

		@Override
		public boolean isInvariant() {
			return false;
		}

	    public Class<?> getBindingType() {
	        return String.class;
	    }
	}
}
