package net.fishear.web.t5.context.services.impl;

import javax.inject.Inject;

import net.fishear.web.t5.context.services.ApplicationContextService;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EagerLoad
public class 
	ApplicationContextServiceImpl 
implements 
	ApplicationContextService
{
	
	public static final String SPRING_CONTEXT_CLASS_NAME = "org.springframework.context.ApplicationContext";

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
	private ApplicationGlobals applicationGlobals;

	
	public ApplicationContextServiceImpl() {
		if(singletonInstance[0] == null) {
			singletonInstance[0] = this;
			log.info("Singleton context instance has been set.");
		} else {
			log.warn("Singleton context instance is already set to {}.", singletonInstance[0]);
		}
	}
	
	public Registry getRegistry() {
		Registry registry = (Registry) applicationGlobals.getServletContext().getAttribute(TapestryFilter.REGISTRY_CONTEXT_NAME);
		return registry;
	}
	
	private Class<?> getClass(String className) {
		try {
			return getClass().getClassLoader().loadClass(className);
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	@Override
	public <T> T getService(Class<T> serviceInterface) {
		return getRegistry().getService(serviceInterface);
	}

	@Override
	public <T> T getService(String serviceId, Class<T> serviceInterface) {
		return getRegistry().getService(serviceId, serviceInterface);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getService(String serviceInterfaceClassName) {
		return (T) getService(getClass(serviceInterfaceClassName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getService(String serviceId, String serviceInterfaceClassName) {
		return (T) getService(serviceId, getClass(serviceInterfaceClassName));
	}

	@Override
	public boolean springContextExists() {
		try {
			return getSpringContext() != null;
		} catch (Exception ex) {
			return false;
		}
	}

	@Override
	public <T> T getSpringContext() {
		return getService(SPRING_CONTEXT_CLASS_NAME);
	}

}
