package net.fishear.data.audit.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.fishear.FishearConstants;
import net.fishear.Interfaces.IdI;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.services.AuditChangeService;
import net.fishear.data.audit.services.AuditService;
import net.fishear.data.audit.services.AuditedEntityService;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.StandardEntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.results.Functions;
import net.fishear.data.generic.services.CurrentStateI;
import net.fishear.data.generic.services.GenericService;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.EntityUtils.Property;

public class 
	AuditServiceImpl 
extends 
	GenericService<Audit> 
implements
	AuditService
{

	private AuditedEntityService auditedEntityService;

	private AuditChangeService auditChangeService;
	
	public Audit createAuditEntity(Action action, EntityI<?> e1, EntityI<?> e2) {

		log.debug("Auditing: action {}", action);
		List<Property> diflist;
		boolean forceSave = false;
		switch(action) {
		case DELETE:
			//diflist = EntityUtils.fillDifferencies(e1, false);
			diflist = new ArrayList<EntityUtils.Property>();
			forceSave = true;
			break;
		case INSERT:
			diflist = EntityUtils.fillDifferencies(e2, true);
			break;
		case UPDATE:
			if(e2 == null) {
				log.warn("UPDATE is performed but second entity ('e2') is null. Full log is performed.");
				diflist = EntityUtils.listDifferencies(e1, true);
			} else {
				diflist = EntityUtils.listDifferencies(e1, e2);
			}
			break;
		default:
			throw new IllegalArgumentException("'action' argument has unsupported value: " + action);
		}

		if(forceSave || diflist.size() > 0) {
			log.trace("Entities differencies found: {}", diflist.size());

			Audit audit = newEntityInstance();
			audit.setChanges(toChanges(diflist, audit, (e2 == null ? e1 : e2) instanceof StandardEntityI));

			if(forceSave || audit.getChanges().size() > 0) {

				audit.setAuditedEntity(auditedEntityService.getOrCreate((e1 == null ? e2 : e1).getClass()));
				audit.setObjectId(e2 == null ? e1.getIdString() : e2.getIdString());
				audit.setChangeNumber(getNextChangeNumber(audit));
	
				CurrentStateI state = getCurrentState();
				Object user = state == null ? null : state.getCurrentUser();
				audit.setActionUser(user == null ? "(unknown)" : user.toString());
				audit.setAction(action);
				audit.setActionDate(new Date());
				
				return audit;
			} else {
				log.trace("No entities differencies found in second step");
			}
		} else {
			log.trace("No entities differencies found");
		}
		return null;
		
	}
	
	private Long getNextChangeNumber(Audit audit) {
		QueryConstraints qc = QueryFactory.andEquals("objectId", audit.getObjectId(), "auditedEntity", audit.getAuditedEntity());
		qc.results().add("changeNumber",  Functions.MAX);
		Long ll = (Long) super.query(qc).get(0);
		return ll == null ? 1L : ll + 1L;
	}

	public void auditEntity(Action action, EntityI<?> e1, EntityI<?> e2) {
		Audit audit = createAuditEntity(action, e1, e2);
		if(audit != null) {
			save(audit);
		}
	}

	private String tos(Object o) {
		if(o == null) {
			return null;
		} else {
			if(o instanceof IdI<?>) {
				return tos(EntityUtils.getId((IdI<?>)o));
			} else if (o instanceof Date) {
				return FishearConstants.ANSI_DATETIME_FORMAT_MILLIS.format((Date)o);
			} else if (o instanceof Timestamp) {
				return FishearConstants.ANSI_DATETIME_FORMAT_MILLIS.format((Timestamp)o);
			} else {
				return o.toString();
			}
		}
	}
	
	private List<AuditChange> toChanges(List<Property> diflist, Audit header, boolean stdEntity) {
		List<AuditChange>  chlist = new ArrayList<AuditChange>();
		for(Property p : diflist) {
			// in case standard entity, user and date of update are ignored
			if(stdEntity && ("updateDate".equals(p.getName()) || "updateUser".equals(p.getName()))) {
				continue;
			}
			AuditChange ch = new AuditChange();
			ch.setAudit(header);
			ch.setPropertyName(p.getName());
			ch.setNewValue(tos(p.getValue2()));
			chlist.add(ch);
		}
		return chlist;
	}

	public Object save(Audit audit) {
		Object id = super.save(audit);
		auditChangeService.saveAll(audit.getChanges());
		return id;
	}

	/**
	 * @return the auditedEntityService
	 */
	public AuditedEntityService getAuditedEntityService() {
		return auditedEntityService;
	}

	/**
	 * @param auditedEntityService the auditedEntityService to set
	 */
	public void setAuditedEntityService(AuditedEntityService entityService) {
		this.auditedEntityService = entityService;
	}

	/**
	 * @return the auditChangeService
	 */
	public AuditChangeService getAuditChangeService() {
		return auditChangeService;
	}

	/**
	 * @param auditChangeService the auditChangeService to set
	 */
	public void setAuditChangeService(AuditChangeService changeService) {
		this.auditChangeService = changeService;
	}

	@Override
	public void initForcedInstance() {

		setAuditChangeService(new AuditChangeServiceImpl());
		setAuditedEntityService(new AuditedEntityServiceImpl());
		log.debug("Forced instance initialized.");
	}
	
}
