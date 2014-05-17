package net.fishear.data.generic.services;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.utils.Exceptions;

public class ServiceFactory
{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends EntityI<?>> ServiceI<T> createService(Class<T> entityType) {
	
		try {
	
			ServiceProxy proxy = new ServiceProxy(entityType);
	
			Class<?> proxyClass = (Class<?>) Proxy.getProxyClass(ServiceProxy.class.getClassLoader(), ServiceI.class);
			ServiceI<T> service = (ServiceI<T>) proxyClass.getConstructor(InvocationHandler.class).newInstance(proxy);
	
			return service;
	
		} catch(Exception ex) {
			throw Exceptions.runtime(ex);
		}
	
	}

	
}
