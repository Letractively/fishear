package net.fishear.data.audit.services;

import java.util.List;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.AuditServiceI;
import net.fishear.data.generic.services.ServiceI;

public interface 
	AuditService 
extends 
	ServiceI<Audit>,
	AuditServiceI
{

	/**
	 * creates and returns audit entity that can be stored to database.
	 * 
	 * @param action
	 *            action performed
	 * @param e1
	 *            first (source) entity
	 * @param e2
	 *            second (target) entity
	 * @param targetService
	 * 			  the service that manipulates the entity. May be null
	 * @return null if no difference exists between entities, otherwise antity.
	 */
	Audit createAuditEntity(Action action, EntityI<?> e1, EntityI<?> e2, ServiceI<?> targetService);

	/**
	 * @return enity list service
	 */
	AuditedEntityService getAuditedEntityService();

	/**
	 * @return change service
	 */
	AuditChangeService getAuditChangeService();
	
	/**
	 * returns list of changes for given entity.
	 * 
	 * @param entity the entity that changes are required for
	 * @return list of changes
	 */
	List<Audit> listForEntity(EntityI<?> entity);
	
	/** 
	 * returns latest change for given object
	 * @param entity the entity that changes are required for
	 * @return latest change, or null if does not exist
	 */
	Audit getLatestChange(EntityI<?> entity);
}
