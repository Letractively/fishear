package net.fishear.web.t5.audit.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
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
	
	@Persist
	@Property
	Audit detail;
	
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
		QueryConstraints qc = QueryFactory.create();
		if(entityHash != null) {
			qc.addJoin("audit", Restrictions.equal("auditedEntity", getAudiedEntity()));
		}
		qc.projection().distinct("propertyName");
		return auditService.getAuditChangeService().query(qc);
	}
	
	public List<Audit> getAudits() {
		
		QueryConstraints qc = QueryFactory.create();
		if(entityHash != null) {
			qc.add(Restrictions.equal("auditedEntity", getAudiedEntity()));
		}
		if(selectedProperty != null) {
			qc.where().conditions().add(Restrictions.exists(AuditChange.class, "ac", Restrictions.and(
					Restrictions.equalProperty("this.id", "ac.audit.id"),
					Restrictions.equal("propertyName", selectedProperty)
			)));
		}
		if(selectedAction != null) {
			qc.add(Restrictions.equal("action", selectedAction));
		}
		if(selectedObjectId != null) {
			qc.add(Restrictions.equal("objectId", selectedObjectId));
		}
		return auditService.list(qc);
		
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

		onHideDetail();
	}

	public void onSuccessFromSearch() {
		onHideDetail();
	}
	
}
