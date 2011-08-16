package net.fishear.data.generic.services;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.fishear.data.generic.entities.EntityI;

/**
 * The class provides method to get Service instance (including underline DAO instance) at runtime, 
 * without need to have real interface. 
 * 
 * @author terber
 *
 * @param <K>
 */
public class 
	ServiceProxy<K extends EntityI<?>>
extends
	GenericService<K>
implements
	InvocationHandler
{
	
	ServiceProxy(Class<? extends EntityI<?>> entityType) {
		super(entityType);
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		return method.invoke(this, args);
	}
}
