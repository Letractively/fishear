package net.fishear.data.generic.dao;

import java.util.List;
import java.util.Map;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.CurrentStateSourceI;

public interface GenericDaoI<K extends EntityI<?>> {

	K read(Object id);

	Object save(K entity);

	void delete(K entity);

	boolean isLazyLoaded(Object entity, String propertyName);

	K newEntityInstance();

	Class<K> type();

	List<?> query(QueryConstraints queryConstraints);

	/**
	 * synchronize internal state with cache, but bo not commit transaction.
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

	/**
	 * executes SQL update query in JPA-implementation specific manner.
	 * Its implementation is NOT MANDATORRY, IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param parameters pairs of key-value for named parameters. Each even value must be string (= key), each odd value is key's value (any object)
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	Integer executeUpdate(String query, Object... parameters);

	/**
	 * executes SQL update query in JPA-implementation specific manner.
	 * Its implementation is NOT MANDATORRY, IT SHOULD BE USED WITH CARE.
	 * @param query query string 
	 * @param paramsMap key-value pairs for nammed parameters
	 * @return number of affected records, or null = number of records is unknown
	 * @throws RuntimeException in case any error occurs, or method is not implemented.
	 */
	Integer executeUpdate(String query, Map<String, Object> paramsMap);

	/** refreshes the entity state, so entity is reloaded from database.
	 * @param entity
	 */
	K refresh(K entity);
}
