package net.fishear.web.t5.base;

import org.apache.tapestry5.annotations.Persist;

import java.util.Enumeration;
import java.util.Hashtable;

/** class represents page or component, which is designed to control other components and switch among them 
 * (for example master - detail page etc).
 * Page which contains (and control) those fragments should extends this abstract class.
 * @author terber
 */
public abstract class 
	AbstractController 
extends 
	AbstractComponent 
implements 
	IndependentPageElementI 
{

	private final Hashtable<String, Object> components = new Hashtable<String, Object>(); 

	private Object currentComponent;
	
	@Persist
	private String lastSelected;
	
	/** returns kry for regostered component 'o', or null if the component is not registered.
	 */
	private String getKeyForComponent(Object o) {
		String key;
		Enumeration<String> en = components.keys();
		while (en.hasMoreElements()) {
			if(components.get(key = en.nextElement()) == o) {
				return key;
			}
		}
		return null;
	}
	
	/** adds (= registers) component 'component' under code 'key'.
	 * The first-time added component is initially set as current component.
	 * @param key
	 * @param component
	 * @return component previously registered under code 'key' (if any) or null.
	 */
	private Object addComponent(String key, Object component) {
		if(component == null) {
			throw new IllegalArgumentException("Component must not be null");
		}
		if(key == null || (key = key.trim().toLowerCase()).length() == 0) {
			throw new IllegalArgumentException("Key must not be empty");
		}
		if(components.contains(component)) {
			String code = getKeyForComponent(component);
			throw new IllegalStateException("Tlhe component " + component + " is allready added under code '"+code+"'");
		}
		Object o = components.put(key, component);
		if(lastSelected == null || lastSelected.equals(key)) {
			// sets firstly added component as current if no component was selected previously
			setCurrentComponentByKey(key);
		}
		return o;
	}
	
	/** adds component to page and register itself into component (if the components implements {@link ControlledI}.
	 * Firstly calls {@link #addComponent(String, Object)} to add it, then (if the page implements {@link ControlledI}) calls {@link ControlledI#setControlPage(this)} calls
	 * @see #addComponent(String, Object) 
	 */
	public Object registerComponent(String key, Object component) {
		Object o = addComponent(key, component);
		if(component instanceof ControlledI) {
			((ControlledI)component).setControlPage(this);
		}
		return o;
	}
	
	/** returns component which is assiciated to code 'componentCode', or null if component with code 'componentCode' is not registered.
	 * @param componentCode 
	 */
	public Object getComponent(String componentCode) {
		return components.get(componentCode);
	}
	
	/** gets component associated to key 'pageType' and sets this component as 'current'.
	 * The current component is returned by method {@link }
	 * @param pageType key of required component. It must be registered via {@link #addComponent(String, Object)} method
	 */
	private Object setCurrentComponentByKey(String iKey) {
		String key = iKey.trim().toLowerCase();
		Object comp = getComponent(key);
		if(comp == null) {
			throw new IllegalArgumentException("Under key '"+key+"' is not registered any component.");
		}
		this.currentComponent = comp;
		this.lastSelected = key;
		return comp;
	}
	
	/** Sets and returns current component. 
	 * Behavior is different in dependency to type of parameter 'componentOrCode'
	 * @param componentOrCode if this is the String, method assumes the code and searches for component with this code. The component with this code must be registered using {@link #addComponent(String, Object)}  
	 * In any other case, method assumes that 'componentOrCode' is the component directly. It is set as current component. Component must be registered (as in case of String) using{@link #addComponent(String, Object)}.  
	 * @return currently set component.
	 * @throws IllegalArgumentException in case the component or its code is not registered
	 */
	public Object setCurrentComponent(Object componentOrCode) {
		if(componentOrCode instanceof String) {
			return setCurrentComponentByKey((String)componentOrCode);
		}
		String key = getKeyForComponent(componentOrCode);
		if(key == null) {
			throw new IllegalArgumentException("The component " + componentOrCode + "does not exists among components registered in this page.");
		}
		return setCurrentComponentByKey(key);
	}
	
	/** synonym for {@link #setCurrentComponent(Object)}
	 */
	public Object selectComponent(Object componentOrCode) {
		return setCurrentComponent(componentOrCode);
	}
	
	/** returns current component. 
	 * This one is set by call of {@link #setCurrentComponent(String)}, or {@link #setCurrentComponent(Object)} 
	 * @return current component
	 * @throws  IllegalStateException in case current component is not set yet (it must be set using {@link #setCurrentComponent(Object)} os {@link #setCurrentComponent(Object)}
	 */
	public Object getCurrentComponent() {
		
		if(currentComponent == null) {
			if(lastSelected == null) {
				throw new IllegalStateException("The current component wan'nt set yet. ");
			}
			setCurrentComponent(lastSelected);
		}
		return currentComponent;
	}

	/** clears registered components and prepare page for new rendering cycle.
	 * This method must be called from within method bound as @PageAttached
	 */
	public void reinit() {
		components.clear();
	}

//	/** clears registered components and prepare page for new rendering cycle.
//	 * This method must be called from within method bound as @PageAttached
//	 * TODO: neni dodelano
//	 */
//	public void reinit(String... as) {
//		components.clear();
//		for (int i = 0; i < as.length; i++) {
//			
//		}
//	}
}
