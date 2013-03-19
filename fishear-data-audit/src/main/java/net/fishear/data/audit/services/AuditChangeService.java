package net.fishear.data.audit.services;

import java.util.List;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.generic.services.ServiceI;

public interface 
	AuditChangeService 
extends
	ServiceI<AuditChange>
{
	/**
	 * text returned in case no value is available.
	 */
	public static final String NA = "(N/A)";

	List<AuditChange> listForAudit(Audit audit);

	String getCurrentValue(Audit audit, String name);

	String getPreviousValue(Audit audit, String propertyName);
}
