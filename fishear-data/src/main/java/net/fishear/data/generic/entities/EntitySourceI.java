package net.fishear.data.generic.entities;

/**
 * very common interface suit for passing entities among components.
 * 
 * Usually one component keeps entity instance and passing this single instance to nested components.
 * Tha is most common usage of this interface.
 * 
 * @author ffyxrr
 *
 */
public interface EntitySourceI<T extends EntityI<?>> {

	T getEntity();
	
	void setEntity(T entity);
 	
	
}
