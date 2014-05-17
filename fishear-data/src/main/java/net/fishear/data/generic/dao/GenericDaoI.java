package net.fishear.data.generic.dao;

import java.util.List;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;

public interface GenericDaoI<K extends EntityI<?>> {

	/**
	 * reads single entity by ID.
	 * @param id entity ID. never should be nul, but in case it is nukll no exception is throws - null entity is returned instead (null ID should be logged).
	 * @return
	 */
	K read(Object id);

	/**
	 * saves entity to persistent storage. Generates ID if needed. Tries to merge with persistent entity instance, if such exists.
	 * @param entity the entity
	 * @return entity ID (either new or existing).
	 */
	Object save(K entity);

	/** 
	 * deletes entity from persistent storage.
	 * 
	 * @param entity
	 */
	void delete(K entity);

	/**
	 * should decide whether propertz is layz loaded
	 * @param entity affected entitz
	 * @param propertyName the name
	 * @return true or false
	 */
	boolean isLazyLoaded(K entity, String propertyName);
	
	/** 
	 * forces underlying layer to load lazily initialized object.
	 * If property is not lazily initialized or is already initialized, does nothing.
	 * @param entity the entity
	 * @param propertyName the property
	 * @return property's value
	 */
	void loadLazy(K entity, String propertyName);

	/**
	 * creates new entity instance, but MUST NOT connect persistent layer.
	 * @return new entity instance
	 */
	K newEntityInstance();

	/**
	 * @return entitz class that the service is created for
	 */
	Class<K> type();

	/** generic query, that can return any return type (not exactly the entity list or entity)
	 * @param queryConstraints
	 * @return query return
	 */
	List<?> query(QueryConstraints queryConstraints);

	/**
	 * synchronize internal state with cache, but but does not commit transaction.
	 */
	void flush();

	/**
	 * If transaction has been started, commits it. Otherwise do nothing.
	 */
	void commit();

	/**
	 * If transaction has been started, rollbacks it. Otherwise do nothing.
	 */
	void rollback();

	/**
	 * If dao supports transactions and transaction not started yet, begins int.
	 * Otherwise do nothing.
	 */
	void transaction();

	/**
	 * @return current state source if set, or null if not.
	 */
	DaoSourceI getDaoSource();

	/** refreshes the entity state, so entity is reloaded from database.
	 * @param entity
	 */
	K refresh(K entity);
}
