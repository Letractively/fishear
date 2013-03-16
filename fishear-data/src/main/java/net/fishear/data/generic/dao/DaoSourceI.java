package net.fishear.data.generic.dao;

import javax.persistence.Entity;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.exceptions.EntityRegistrationNotSupportedException;
import net.fishear.data.generic.services.AdonsI;
import net.fishear.data.generic.services.CurrentStateI;
import net.fishear.data.generic.services.CurrentStateSourceI;

public interface DaoSourceI
{

	<T  extends EntityI<?>> GenericDaoI<T> createDao(Class<T> entityType);
	
	boolean supportsAutoregistrationEntities();

	boolean supportsAutoregistrationPackages();

	/**
	 * adds single {@link Entity} annotated entity to entity list.
	 * throws {@link EntityRegistrationNotSupportedException} in case operation is not permitted.
	 * @param entity the entity class
	 */
	void registerEntity(Class<EntityI<?>> entity);

	/** if the DAO source supports automaticall entity registration, adds all {@link Entity} annotated entities to list of available entities.
	 * throws {@link EntityRegistrationNotSupportedException} in case operation is not permitted.
	 * @param packageName package name is searched
	 */
	void registerPackage(String packageName);

	/**
	 * returns current state source, that provide current system state.
	 * 
	 * @return state source
	 */
	CurrentStateSourceI getCurrentStateSource();

	/**
	 * allows to set state source that provides current system state (user logged in, ...)
	 * @param currentStateSource
	 */
	void setCurrentStateSource(CurrentStateSourceI currentStateSource);

	/**
	 * @return additional utilities, that are related to database. Unlike {@link CurrentStateI} (which can be system-wide).
	 */
	AdonsI getAdons();
}
