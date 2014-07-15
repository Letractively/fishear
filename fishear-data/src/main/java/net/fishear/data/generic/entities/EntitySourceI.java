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

	/**
	 * Implementation of this interface manages persistent entity (for example in session). 
	 * Value that is set by {@link #setEntity(EntityI)} must be returned by any subseqwuent call of {@link #getEntity()}.
	 * The exception is setEntity(null), when subsequent getEntity() call returns new entity instance.
	 * 
	 * @return entity instance (either new instance - not persisted, or loaded from persistent storage). <b>NEVER RETURNS NULL</b>. If null has been set as entity by previous setEntity() call,  creates new entity instance. 
	 */
	T getEntity();
	
	/**
	 * sets required entity. 
	 * 
	 * @param entity the new entity value. May be null = new entity instance is created during subsequent {@link #getEntity()} call. 
	 */
	void setEntity(T entity);
 	
	
}
