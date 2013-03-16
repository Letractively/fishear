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

		DELETE
	}
	
	void auditEntity(Action action, EntityI<?> e1, EntityI<?> e2);

	/**
	 * initializes internal services - creates instance of them.
	 * Called in case service is instantiate directly by call "new ... "
	 */
	void initForcedInstance();
	
}

