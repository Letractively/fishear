package net.fishear.data.audit.services.impl;

import java.util.List;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.services.AuditChangeService;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.restrictions.Restrictions;
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
	
	private AuditChange getLatest(List<AuditChange> list) {
		Long maxBuild = -1L;
		AuditChange current = null;
		for(AuditChange row : list) {
			if(row.getAudit().getChangeNumber() > maxBuild) {
				current = row;
				maxBuild = row.getAudit().getChangeNumber();
			}
		}
		return current;
	}

	@Override
	public String getCurrentValue(Audit audit, String propertyName) {
		QueryConstraints qc = QueryFactory.create();
		qc.join("audit", Restrictions.equal("objectId", audit.getObjectId()));
		qc.add(Restrictions.equal("propertyName", propertyName));
		AuditChange current = getLatest(list(qc));
		return current == null ? NA : current.getNewValue();
	}

	@Override
	public String getPreviousValue(Audit audit, String propertyName) {
		QueryConstraints qc = QueryFactory.create();
		qc.join("audit", Restrictions.and(
			Restrictions.equal("objectId", audit.getObjectId()),
			Restrictions.lessThan("changeNumber", audit.getChangeNumber())
		) );
		qc.add(Restrictions.equal("propertyName", propertyName));
		AuditChange current = getLatest(list(qc));
		return current == null ? NA : current.getNewValue();
	}

}
