package net.fishear.data.generic.dao.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.exceptions.AppException;



public class 
	DynamicProxyDaoHandler
implements
	InvocationHandler
{
	
	Logger log = LoggerFactory.getLogger(DynamicProxyDaoHandler.class);

	private GenericDaoI<? extends EntityI<?>> wrapped;
	
	private Class<GenericDaoI<? extends EntityI<?>>> daoClass;

	public DynamicProxyDaoHandler(GenericDaoI<? extends EntityI<?>> r) {
		wrapped = r;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		if ("workOn".equals(method.getName())) {
//			args[1] = Tool.RATCHET;
//		}
		return method.invoke(wrapped, args);
	}

	@SuppressWarnings("unchecked")
	public <T extends EntityI<?>> GenericDaoI<T> createDao(GenericDaoI<T> toWrap) {
		try {
            log.debug("Creating DAO for {}", toWrap);
			Method met = DynamicProxyDaoHandler.class.getMethod("createDao", daoClass);
			Type gpType = met.getGenericReturnType();
			ParameterizedType aType = (ParameterizedType) gpType;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
	        for(Type parameterArgType : parameterArgTypes){
	            Class<?> parameterArgClass = (Class<?>) parameterArgType;
	            log.debug("parameterArgClass = {}", parameterArgClass);
	        }
			ClassLoader cl = daoClass.getClassLoader();
			Object newObject = (Proxy.newProxyInstance(cl,
					new Class[] { daoClass }, 
					new DynamicProxyDaoHandler(toWrap))
			);
			return (GenericDaoI<T>)newObject;
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}
}
