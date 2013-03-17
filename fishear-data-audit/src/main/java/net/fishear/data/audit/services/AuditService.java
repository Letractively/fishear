package net.fishear.data.audit.services;

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

	/** creates and returns audit entity that can be stored to database.
	 *  
	 * @param action action performed
	 * @param e1 first (source) entity 
	 * @param e2 second (target) entity
	 * @return null if no difference exists between entities, otherwise antity. 
	 */
	 Audit createAuditEntity(Action action, EntityI<?> e1, EntityI<?> e2);
	
}
