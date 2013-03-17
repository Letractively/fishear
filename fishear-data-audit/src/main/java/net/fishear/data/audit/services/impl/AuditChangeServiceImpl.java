package net.fishear.data.audit.services.impl;

import java.util.List;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.services.AuditChangeService;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.AbstractService;

public class 
	AuditChangeServiceImpl 
extends 
	AbstractService<AuditChange>
implements
	AuditChangeService
{

	@Override
	public List<AuditChange> listForAudit(Audit audit) {
		return list(QueryFactory.equals("audit", audit));
	}

}
