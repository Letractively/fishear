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

	List<AuditChange> listForAudit(Audit audit);
	
}
