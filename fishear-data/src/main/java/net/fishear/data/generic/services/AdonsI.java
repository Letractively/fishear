package net.fishear.data.generic.services;

/**
 * aditional utilitiesrelated to database
 * 
 * @author ffyxrr
 *
 */
public interface AdonsI {

	
	/**
	 * provides implementation of AuditService, which MUST implement method auditEntity({@link EntityI} e1, {@link EntityI} e2)
	 * 
	 * @return implementation of AuditService
	 */
	AuditServiceI getAuditService();
	
	
}
