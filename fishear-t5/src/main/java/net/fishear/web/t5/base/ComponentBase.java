package net.fishear.web.t5.base;

import java.lang.annotation.Annotation;

import net.fishear.web.t5.annotations.ForZone;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Cached;
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

//		if(an == null) {
//			throw new IllegalStateException(String.format("Class is expected to be annotated by '%s' annotation, but it is not", ForZone.class.getName()));
//		}
		return an;
	}
	
	/**
	 * @return zone that is defined in {@link ForZone} annotation at class level. 
	 * The child class must be annotated by this annotation. 
	 */
	@Cached
	protected Zone getForZone() {
		try {
			org.apache.tapestry5.runtime.Component dlg = crsc.getEmbeddedComponent("detailDialog");
			return (Zone) dlg.getClass().getMethod("getDialogZone").invoke(dlg);
		} catch(Exception ex) {
			return getOldForZone();
		}
	}
	
	private Zone getOldForZone() {
		log.warn("Deprecated 'ForZone' annotation is used. replace this annotation with standard approach (either 'fe.dialog', or direct zone ID reference.");
		ForZone anot = getAn(ForZone.class);
		if(anot == null) {
			return null;
		}
		String an = anot.value();
		Component zz = crsc.getEmbeddedComponent(an);
		if(zz == null) {
			return null;
//			throw new IllegalStateException(String.format("Component '%s' must contain embedded zone '%s'", getClass().getName(), an));
		}
		if(!(zz instanceof Zone)) {
			throw new IllegalStateException(String.format("Embedded block '%s' is expected to be Zone, but it is '%s'", an, zz.getClass().getSuperclass().getName()));
		}
		return (Zone)zz;
	}

	/**
	 * @return zone ID or null.
	 * @deprecated Standard ^ zone reference should be used instead
	 */
	@Deprecated
	public String getForZoneId() {
		log.warn("Deprecated 'ForZoneId' attribute reference is used. Replace it with '^'.");
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

	/**
	 * calls for return value during form submission. Default implementation calls {@link #getReturn()}, but may be overriden to return different value..
	 * 
	 * @return form handler return value.
	 */
	protected Object getFormReturn() {
		return getReturn();
	}
	
	
	
	public String translate(String key, Object... args) {
		
		
		ComponentResources crsc = this.crsc;

		while(crsc != null && !crsc.getMessages().contains(key)) {
			crsc = crsc.getContainerResources();
		}
		if(crsc == null) {
			return this.crsc.getMessages().format(key, args);
		} else {
			return crsc.getMessages().format(key, args);
		}
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
