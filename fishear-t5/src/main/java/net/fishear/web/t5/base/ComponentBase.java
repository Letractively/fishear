package net.fishear.web.t5.base;

import java.lang.annotation.Annotation;

import net.fishear.web.t5.annotations.ForZone;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ComponentBase {

	
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	protected Request request;
	
	@Inject
	protected AlertManager alerts;
	
	@Inject
	protected ComponentResources crsc;
	
	@Inject
	protected PageRenderLinkSource prsc;
	
	private <T extends Annotation> T getAn(Class<T> type) {
		Class<?> cl = getClass();
		T an;
		do {
			an = cl.getAnnotation(type);
		} while(an == null && (cl = cl.getSuperclass()) != Object.class);

		if(an == null) {
			throw new IllegalStateException(String.format("Class is expected to be annotated by '%s' annotation, but it is not", ForZone.class.getName()));
		}
		return an;
	}
	
	/**
	 * @return zone that is defined in {@link ForZone} annotation at class level. 
	 * The child class must be annotated by this annotation. 
	 */
	@Cached
	public Zone getForZone() {
		String an = getAn(ForZone.class).value();
		Component zz = crsc.getEmbeddedComponent(an);
		if(zz == null) {
			throw new IllegalStateException(String.format("Component '%s' must contain embedded zone '%s'", getClass().getName(), an));
		}
		if(!(zz instanceof Zone)) {
			throw new IllegalStateException(String.format("Embedded block '%s' is expected to be Zone, but it is '%s'", an, zz.getClass().getSuperclass().getName()));
		}
		return (Zone)zz;
	}
	
	public String getForZoneId() {
		Zone zz = getForZone();
		return zz == null ? null : zz.getClientId();
	}
	
	
	/**
	 * @return the page that will be rendered after event is proccessed.
	 */
	public Object getPageToRender() {
		return crsc.getPage();
	}
	
	/**
	 * @return depending on request type returns this instance or zone defined by {@link ForZone} annotation at class level.
	 */
	public Object getReturn() {
		return request.isXHR() ? getForZone() : getPageToRender();
	}
	
	public String translate(String key, Object... args) {
		return crsc.getMessages().format(key, args);
	}
	
	protected Link createPageLing(Class<?> pageClass, Object... context) {
		if(context == null || context.length == 0) {
			return prsc.createPageRenderLink(pageClass);
		} else {
			return prsc.createPageRenderLinkWithContext(pageClass, context);
		}
	}

	/**
	 * renders informal parameters toi the given element. 
	 * Can be used during any phase (usually {@link AfterRender}) to render those parameters to given (usually inner) element.
	 * 
	 * @param el
	 */
	protected void renderInformalParameters(Element el) {
		if(el != null) {
			for(String s : crsc.getInformalParameterNames()) {
				el.attribute(s, crsc.getInformalParameter(s, String.class));
			}
		} else {
			log.warn("Element for informal parameters writting is null");
		}
	}
}
