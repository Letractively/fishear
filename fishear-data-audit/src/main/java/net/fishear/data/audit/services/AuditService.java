package net.fishear.data.audit.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.fishear.FishearConstants;
import net.fishear.Interfaces.IdI;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.entities.AuditHader;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.AuditServiceI;
import net.fishear.data.generic.services.CurrentStateI;
import net.fishear.data.generic.services.GenericService;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.EntityUtils.Property;

public class 
	AuditService 
extends 
	GenericService<AuditHader> 
implements
	AuditServiceI
{

	private AuditedEntityService entityService;

	private AuditChangeService changeService;
	
	/** creates and returns audit entity that can be stored to database.
	 *  
	 * @param action action performed
	 * @param e1 first (source) entity 
	 * @param e2 second (target) entity
	 * @return null if no difference exists between entities, otherwise antity. 
	 */
	public AuditHader createAuditEntity(Action action, EntityI<?> e1, EntityI<?> e2) {

		log.debug("Auditing: action {}", action);
		List<Property> diflist;
		switch(action) {
		case DELETE:
			diflist = EntityUtils.fillDifferencies(e1, false);
			break;
		case INSERT:
			diflist = EntityUtils.fillDifferencies(e2, true);
			break;
		case UPDATE:
			if(e2 == null) {
				log.warn("UPDATE is performed but second entity ('e2') is null. Treated as insert.");
				diflist = EntityUtils.listDifferencies(e1, true);
			} else {
				diflist = EntityUtils.listDifferencies(e1, e2);
			}
			break;
		default:
			throw new IllegalArgumentException("'action' argument has unsupported value: " + action);
		}

		if(diflist.size() > 0) {
			log.trace("Entities differencies found: {}", diflist.size());
			AuditHader audit = newEntityInstance();
			audit.setEntity(entityService.getOrCreate((e1 == null ? e2 : e1).getClass()));
			audit.setChanges(toChanges(diflist, audit));
			CurrentStateI state = getCurrentState();
			Object user = state == null ? null : state.getCurrentUser();
			audit.setActionUser(user == null ? "(unknown)" : user.toString());
			audit.setAction(action);
			audit.setActionDate(new Date());
			return audit;
		} else {
			log.trace("No entities differencies found");
			return null;
		}
		
		
	}
	
	/** implementation of 
	 * @param action
	 * @param e1
	 * @param e2
	 */
	public void auditEntity(Action action, EntityI<?> e1, EntityI<?> e2) {
		AuditHader audit = createAuditEntity(action, e1, e2);
		if(audit != null) {
			save(audit);
		}
	}

	private String tos(Object o) {
		if(o == null) {
			return null;
		}
		if(o instanceof IdI<?>) {
			return tos(EntityUtils.getId((IdI<?>)o));
		} else if (o instanceof Date) {
			return FishearConstants.ANSI_DATETIME_FORMAT_MILLIS.format((Date)o);
		}
		return o.toString();
	}
	
	private List<AuditChange> toChanges(List<Property> diflist, AuditHader header) {
		List<AuditChange>  chlist = new ArrayList<AuditChange>();
		for(Property p : diflist) {
			AuditChange ch = new AuditChange();
			ch.setHeader(header);
			ch.setPropertyName(p.name);
			ch.setOldValue(tos(p.value1));
			ch.setNewValue(tos(p.value2));
			chlist.add(ch);
		}
		return chlist;
	}
	
	public Object save(AuditHader audit) {
		Object id = super.save(audit);
		changeService.save(audit.getChanges());		
		return id;
	}

	/**
	 * @return the entityService
	 */
	public AuditedEntityService getEntityService() {
		return entityService;
	}

	/**
	 * @param entityService the entityService to set
	 */
	public void setEntityService(AuditedEntityService entityService) {
		this.entityService = entityService;
	}

	/**
	 * @return the changeService
	 */
	public AuditChangeService getChangeService() {
		return changeService;
	}

	/**
	 * @param changeService the changeService to set
	 */
	public void setChangeService(AuditChangeService changeService) {
		this.changeService = changeService;
	}

	@Override
	public void initForcedInstance() {

		setChangeService(new AuditChangeService());
		setEntityService(new AuditedEntityService());
		log.debug("Forced instance initialized.");
	}
	
}
