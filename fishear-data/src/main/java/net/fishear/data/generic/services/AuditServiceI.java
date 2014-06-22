package net.fishear.data.generic.services;

import net.fishear.data.generic.entities.EntityI;

/**
 * provides convenient way to audit changes in entities.
 * 
 * @author ffyxrr
 *
 */
public interface AuditServiceI {
	
	public enum Action {
		INSERT,

		UPDATE,

		DELETE,
		
		VIRTUAL
	}
	
	/** compares entities regarding action and creates the result, which is stored to audit tables later on.
	 * @param action the action
	 * @param e1 first (source) entity instance
	 * @param e2 second (target) entity instance
	 */
	void auditEntity(Action action, EntityI<?> e1, ServiceI<?> service);

	/**
	 * initializes internal services - creates instance of them.
	 * Called in case service is instantiate directly by call "new ... "
	 */
	void initForcedInstance();
	
}

