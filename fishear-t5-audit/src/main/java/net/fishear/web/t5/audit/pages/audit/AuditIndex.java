package net.fishear.web.t5.audit.pages.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.entities.AuditedEntity;
import net.fishear.data.audit.services.AuditService;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.services.AuditServiceI.Action;
import net.fishear.web.t5.audit.components.AuditDetail;
import net.fishear.web.t5.base.ComponentBase;

public class AuditIndex extends ComponentBase {

	@Inject
	AuditService auditService;

	@Property
	Audit row;
	
	@Property
	@Persist
	String entityHash;
	
	@Property
	@Persist
	String selectedProperty;
	
	@Property
	@Persist
	Action selectedAction;
	
	@Property
	@Persist
	String selectedObjectId;
	
	@Property
	@Persist
	boolean isChanged;
	
	@Persist
	@Property
	Audit detail;
	
	@Persist
	@Property
	String searchpropertyValue;
	
	@Persist
	@Property
	Date changedFrom;

	@Persist
	@Property
	Date changedTo;
	
	@Component(parameters="audit=detail")
	AuditDetail auditDetail;

	public Map<String, String> getEntities() {
		Map<String, String> map = new TreeMap<String, String>();
		for(AuditedEntity en : auditService.getAuditedEntityService().list(null)) {
			map.put(en.getIdString(), getEntityShortName(en));
		}
		return map;
	}

	public List<Action> getActions() {
		List<Action> list = new ArrayList<Action>();
		list.add(Action.DELETE);
		list.add(Action.UPDATE);
		list.add(Action.INSERT);
		list.add(Action.VIRTUAL);
		return list;
	}
	
	
	public String getEntityShortName(AuditedEntity en) {
		String cn = en.getClassName();
		return cn.substring(cn.lastIndexOf('.') + 1);
	}
	
	AuditedEntity getAudiedEntity() {
		AuditedEntity en = auditService.getAuditedEntityService().read(Integer.parseInt(entityHash));
		return en;
	}
	
	public List<String> getAvailableProperies() {
		if(entityHash == null) {
			return new ArrayList<String>();
		} else {
			QueryConstraints qc = QueryFactory.create();
			qc.addJoin("audit", Restrictions.equal("auditedEntity", getAudiedEntity()));
			qc.projection().distinct("propertyName");
			return auditService.getAuditChangeService().query(qc);
		}
	}
	
	public List<Audit> getAudits() {
		
		QueryConstraints qc = QueryFactory.create();
		if(entityHash == null) {
			return new ArrayList<Audit>();
		} else {
			qc.add(Restrictions.equal("auditedEntity", getAudiedEntity()));
			if(selectedProperty != null) {
				if(searchpropertyValue != null) {
					qc.where().conditions().add(Restrictions.exists(AuditChange.class, "ac", Restrictions.and(
						Restrictions.equalProperty("this.id", "ac.audit.id"),
						Restrictions.equal("propertyName", selectedProperty),
						Restrictions.like("newValue", searchpropertyValue)
				)));
				} else {
					qc.where().conditions().add(Restrictions.exists(AuditChange.class, "ac", Restrictions.and(
						Restrictions.equalProperty("this.id", "ac.audit.id"),
						null,
						Restrictions.equal("propertyName", selectedProperty)
					)));
				}
			}
			if(selectedAction != null) {
				qc.add(Restrictions.equal("action", selectedAction));
			}
			if(selectedObjectId != null) {
				qc.add(Restrictions.equal("objectId", selectedObjectId));
			}
			qc.add(Restrictions.interval("actionDate", changedFrom, changedTo));

			return auditService.list(qc);
		}
	}

	void onHideDetail() {
		detail = null;
	}
	
	void onDetail(Long id) {
		detail = auditService.read(id);
	}
	
	void onClearSearch() {
		entityHash = null;
		
		selectedProperty = null;
		
		selectedAction = null;
		
		selectedObjectId = null;
		
		searchpropertyValue = null;
		
		changedFrom = null;
		
		changedTo = null;

		onHideDetail();
	}

	public void onSuccessFromSearch() {
		onHideDetail();
	}

//	@OnEvent(value = "provideCompletions")
	List<String> onIdSearch_(String fragment) {
		QueryConstraints qc = QueryFactory.create();
		qc.add(Restrictions.like("newValue", fragment));
		qc.add(Restrictions.equal("audit", detail));
		qc.add(Restrictions.equal("propertyName", selectedProperty));
		
		qc.projection().distinct("objectId");

		List<String> list = auditService.getAuditChangeService().query(qc);
		return list;
	}
	
	@OnEvent(value = "provideCompletions", component="selectedObjectId")
	List<String> onIdSearch(String fragment) {

		QueryConstraints qc = QueryFactory.create();
		qc.add(Restrictions.like("objectId", fragment));
		qc.add(Restrictions.equal("auditedEntity", auditService.getAuditedEntityService().read(Integer.parseInt(entityHash))));
		
		qc.projection().distinct("objectId");

		List<String> list = auditService.query(qc);
		return list;
	}
	
}
