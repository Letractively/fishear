package net.fishear.data.generic.services;

//import net.fishear.data.generic.query.exceptions.ValidationException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.exceptions.TooManyRecordsException;
import net.fishear.exceptions.AppException;


public interface 
	ServiceI<K extends EntityI<?>> 
{

    /**
     * Reads object from a database by specified identifier.
     * If audit module is available and entity is annotates as Auditable, initial state is saved for auditing purpose.
     *
     * @param id Identifier value.
     * @return Return object with given identifier.
     */
    K read(Object id);

    /**
     * Reads none or single object from a database by specified condition.
     * Conditions must be created to return none or exactly one record.
     * If audit module is available and entity is annotates as Auditable, initial state is saved for auditing purpose.
     *
     * @param qc query constraints to select one record.
     * @return Return null if no record met {@link QueryConstraints} 'qc', or loaded record. If more than one record is loaded, the {@link net.fishear.data.generic.query.exceptions.TooManyRecordsException} is thrown.
     * @throws TooManyRecordsException
     */
    K read(QueryConstraints qc) throws TooManyRecordsException;

    /**
     * Save entity permanently.
     * Either creates new object or updates existing object - in dependency on ID's value.
     * If ID is null, zero or -1, creates new one; otherwise updates existing object with given ID.
     * If audit module is available, entity is audited before save (if annnotates).
     *
     * @param object Inserted entity object.
     * @return Returns primary key of inserted or updated object.
     */
    Object save(K object);

    /** 
     * Saves all entities in list. 
     * If audit module is available, each entity is audited before save.
     * 
     * @param list list of entities to be saved
     */
    void saveAll(Collection<K> list);
    
    /**
     * Delete object from persistent storage.
     * If audit module is available, entity is audited before save (if annnotates).
     *
     * @param object Deleted object (must not be null)
     */
    void delete(K object);

    /**
     * Delete object from persistent storage by specified identifier.
     * If entity with given ID does not exist, do nothing.
     *
     * @param id ID of deleted object (must not be null).
     * @return true if record was deleted (= existed previously), false if did not exist.
     */
    boolean delete(Object id);

    /**
     * Performs database query and returns the result.
     * The result can be any type (it may not necessarily be the entity list).
     * @param constraints refining for db query
     * @return list of objects by constraints
     */
    <Q> List<Q> query(QueryConstraints constraints);

    /** Performs database query and returns the result as list of entities of given type {@link K}.
     * Never returns null - returns empty list in case no record if found.
     * It behaves similarly as {@link #query(QueryConstraints)}, but it is type safe. 
     * It cannot be used for general purpose queries too (only entity lists) and never returns null.
     * {@link QueryConstraints#projection()} in 'constraints' must  not be set in case this call (otherwise exception is thrown).
     * If audit module is available and entity is annotates as Auditable, initial state of each entity in list is saved for auditing purpose.
     * 
     * @param constraints constraint to restrict and modify the result. If it is null, the "full result" constraints are used (see {@link QueryFactory#fullResult()}).
     * @return list of entities by constraints
     */
    List<K> list(QueryConstraints constraints);

    /**
     * returns number of records whose will be returned using given query constraints.
     *
     * @param constraints if null, returns count of all available records.
     * Otherwise, returns count of records satisfy those constraints.
     *
     * @return number of records
     */
    long queryCount(QueryConstraints constraints);

    /** flushed all changes in all entities made in current session to database (but does NOT make commit).
     * This method can be called on whichever service you want with the same result.
     */
    void flushAll();
    
    /** checks whether given property is lazy loaded.
     * Useful only by joined properties.
     * @param entity the entity
     * @param propertyName tested property
     * @return true if property is lazy loaded, false otherwise
     */
    boolean isLazyLoaded(K entity, String propertyName);

    /** creates new, empty instance of entity.
     * Service implementation must create it in proper way.
     * This method is independent way to create new entity instance without care of entity type.
     */
    K newEntityInstance();

    /** delete all entities contained in list.
     */
    void deleteAll(Collection<K> list);

	/** Does persistent storage query to existence entity, which has given properties filled out by values from 'entity'.
	 * If the 'entity' has non-zero not null ID, such entity is EXCLUDED from query (e.g. if only those entity exists, method returns false).
	 * The method is suit for verifying codes and names that are treated to be unique on "application level", but not limited by constraints.
	 * @param entity the source values entity 
	 * @param propertyNames list of property names. 'entity' must contain each one, otherwise exception is thrown.
	 * @return true if there exist such entity, false if does not.
     * @throws AppException as wrapped exception, if any occurs.
	 */
	boolean existsEntity(K entity, String... propertyNames);
	
	/** Does persistent storage query to existence entities, which have given properties filled out by the same values as the 'entity'.
	 * If the 'entity' has non-zero not null ID, such entity is EXCLUDED from the query (e.g. if only those entity exists, method returns empty list).
	 * The method is suit for verifying codes and names that are treated to be unique on "application level", but not limited by constraints.
	 * @param entity the source values entity 
	 * @param propertyNames list of property names. 'entity' must contain each one, otherwise exception is thrown.
	 * @return list of duplicities of given entity.
     * @throws AppException as wrapped exception, if any occurs.
	 */
	List<K> listExisting(K entity, String... propertyNames);
	
	Class<K> getEntityType();
    
	/** checks if the entgity is valid. 
	 * In case it is not, the method throws {@link ValidationException} with appropriate NON-LOCALIZED message (= message code).
	 */
	void validate(K entity);
	
	/** if the 'entity' has ID already generated, loads that entity from persistent storage, fills all not-null values from it 
	 * to loaded entity and returns it. If the 'entity' has no ID (e.g. it has not been loaded from DB), do nothing.
	 * @return either loaded and filled entity (in case already exists), or unchanged entity. The returned value could be directly saved (see {@link #save(EntityI)}, it is guaranteed there will be no data conflict.
	 */
	K fillNewEntity(K entity) ;

	/** returns map containing entityID and as key and single string as value.
	 * The text (= the key's value) is built from given attribute values and / or text constants.
	 * Text constant is enclosed to apostrophes or double quotes and it is put "as is" to result. Otherwise, attribute name is expected - the corresponding entity attribute must exist.
	 * Dotted notation to referenced entities is allowed.
	 * Texts or attr values are put to result in order they are passed. 
	 */
	Map<?, String> getIdValueMap(QueryConstraints qc, String... attrNames);
	
	/** provides basic informations about environment.
	 * 
	 * @return Current environment. May return null => environment is unknown.
	 */
	CurrentStateI getCurrentState();

	/**
	 * reads fresh state from the persistent storage. 
	 * @param entity the entity
	 * @return the same entity with current state (it may, or may not be the same entity instance, but must have the same ID)
	 */
	K refresh(K entity);

    /**
     * @return DAO instance for this service.
     */
    GenericDaoI<K> getDao();

	/** Synchronize entity in persistent storage with given entity.
	 * If the 'entity' exists in the persistent storage, reads it and synchronize read one with 'entity'. 
	 * Otherwise ('entity' is null, no ID, does not exist...), returns original entity or new instance. 
	 * @param entity the "source" entity
	 * @return entity instance (never returns null)
	 */
	K syncRead(K entity);

	/**
	 * Reads entity with given ID ore (if such one does not exist in underlying storage) creates new instance.
	 * 
	 * @param id ID of entity. May be null.
	 * @return read or newly created entity instance. You can uyse "isNew()" method call to decide which type is returned.
	 */
	K readOrCreate(Object id);

	/** returns map containing entityID as String  and as key and single string as value.
	 * Behaves the same way as {@link #getIdValueMap(QueryConstraints, String...)}, but keys are as strings.
	 */
	Map<String, String> getIdStringValueMap(QueryConstraints qc, String... attrNames);

}
