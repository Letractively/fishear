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

	private static final String NOT_FOUND = new String("NOT_FOUND");
	
	ComponentResources parent;
	
	public Binding newBinding(
			final String description, 
			final ComponentResources parent, 
			final ComponentResources component, 
			final String expression, 
			final Location location
	) {
		
		String[] ka = Texts.trimAll(expression.split(","), "");

		this.parent = parent;
		
		ComponentResources crsc = component;
		ComponentResources prev = null;

		// search for propert resources with existing key
		while(crsc.getContainerResources() != null && !crsc.getContainerResources().equals(crsc) && !crsc.getMessages().contains(ka[0]) && crsc != prev) {
			prev = crsc;
			crsc = component.getContainerResources();
		}

		return new XBinding(location, description, ka, crsc, component);
	}

	public class XBinding extends AbstractBinding {

		private ComponentResources component;
	    private String description;
	    private String[] ka;
	    private ComponentResources crsc;

	    public XBinding(Location location, String description, String[] ka, ComponentResources crsc, ComponentResources component) {
	        super(location);
	        this.component = component;
	        this.description = description;
	        this.ka = ka;
	        this.crsc = crsc;
	    }

	    public Object get() {
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
							nka[i - 1] = newBinding(description, parent, crsc, ka[i].substring(4).trim(), getLocation()).get().toString();
						} else if(ka[i].startsWith("literal:")) {
							nka[i - 1] = ka[i].substring(8);
						} else {
							String key;
							if(ka[i].startsWith("prop:")) {
								key = ka[i].substring(5);
							} else {
								key = ka[i];
							}
							String val = EntityUtils.getTextValue(key, component.getComponent(), NOT_FOUND);
							nka[i - 1] = val == NOT_FOUND ? String.format("[KEY '%s' NOT FOUND]", key) : val;
						}
					}
					messageValue = crsc.getMessages().format(ka[0], (Object[])nka);
				} catch(Exception ex) {
					throw new IllegalStateException(ex);
				}
			}
			return messageValue;
	    }

	    @Override
	    public String toString() {
	        return String.format("MsgBinding[%s: %s]", description, "(dynamic value)");
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
