package net.fishear.web.t5.context.services;

public interface T5ContextService {

	
	public static final T5ContextService[] singletonInstance = new T5ContextService[1];
	
	/** 
	 * @param beanClass class of the bean
	 * @return bean instance
	 */
	<T> T getService(Class<T> serviceInterface);

	/** returns bean of given type with given beanId
	 * @param beanId the ID
	 * @param beanClass nean class
	 * @return bean instance
	 */
	<T> T getService(String beanId, Class<T> serviceInterface);

	/** 
	 * @param beanClass class of the bean
	 * @return bean instance
	 */
	<T> T getService(String serviceInterfaceClassName);

	/** returns bean of given type with given beanId
	 * @param beanId the ID
	 * @param beanClass nean class
	 * @return bean instance
	 */
	<T> T getService(String beanId, String serviceInterfaceClassname);
	

	/**
	 * tests whether Spring's ApplicationContext is available.
	 * Never throws exception.
	 * @return true if applicatioContext is available, false otherwise.
	 */
	boolean springContextExists();
	
	/**
	 * @return instance of Spring's ApplicationContext. In case context app does not exist, throws runtime exception.
	 * @see #springContextExists()
	 */
	<T> T getSpringContext();

}
