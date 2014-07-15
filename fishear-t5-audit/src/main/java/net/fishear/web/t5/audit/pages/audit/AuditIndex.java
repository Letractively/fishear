package net.fishear.web.t5.audit.pages.audit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
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
import net.fishear.data.generic.query.results.Functions;
import net.fishear.data.generic.services.AuditServiceI.Action;
import net.fishear.web.t5.audit.components.AuditDetail;
import net.fishear.web.t5.base.ComponentBase;
import net.fishear.web.t5.data.PagingDataSource;

@Import(stylesheet="AuditIndex.css")
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
	boolean lastValueOnly;
	
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
	
	@Property
//	@Persist(PersistenceConstants.FLASH)
	String changeFrom;

	@Cached(watch="row")
	public Audit getLastForRow() {
		Audit audit = this.row; 
		if(lastValueOnly) {
			return auditService.getLastValueForEntity(audit);
		} else {
			return audit;
		}
	}
	
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
		if(en == null) {
			return null;
		} else {
			String cn = en.getClassName();
			return cn.substring(cn.lastIndexOf('.') + 1);
		}
	}
	
	public AuditedEntity getAudiedEntity() {
		if(entityHash == null) {
			return null;
		} else {
			AuditedEntity en = auditService.getAuditedEntityService().read(Integer.parseInt(entityHash));
			return en;
		}
	}
	
	public List<String> getAvailableProperies() {
		if(entityHash == null) {
			return new ArrayList<String>();
		} else {
			QueryConstraints qc = QueryFactory.create();
			qc.addJoin("audit", Restrictions.equal("auditedEntity", getAudiedEntity()));
			qc.projections().distinct("propertyName");
			return auditService.getAuditChangeService().query(qc);
		}
	}

//	public List<Audit> getAudits() {
	public PagingDataSource getAudits() {

		PagingDataSource pds = new PagingDataSource(auditService);
		QueryConstraints qc = QueryFactory.create();
		pds.setQueryConstraint(qc);
		if(entityHash == null) {
			qc.add(Restrictions.FALSE);
		} else {
			if(getAudiedEntity() != null) {
				qc.add(Restrictions.equal("auditedEntity.id", getAudiedEntity().getId()));
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
				if(lastValueOnly) {
					qc.add(Restrictions.equal("changeNumber", 1L));
				}
			}
		}
		
		return pds;
	}
	
	public Object onException(Throwable ex) {
		return this;
	}

	void onHideDetail() {
		detail = null;
	}

	void onDetail() {
		
	}

	void onDetail(Long id) {
		if(id == null) {
			alerts.warn("Auditted objectId not passed");
		} else {
			detail = auditService.read(id);
		}
	}

	void onSelectObjectId(String objectId) {
		this.selectedObjectId = objectId;
		if(objectId != null) {
			selectedAction = null;
			lastValueOnly = false;
		}
	}
	
	@OnEvent(EventConstants.ACTIVATE)
	public void onActivate(String entityHash, String entityId) {
		onClearSearch();
		this.entityHash = entityHash;
		this.selectedObjectId = entityId;
		try {
			List<Audit> list = auditService.listForEntity(auditService.getAuditedEntityService().read(Integer.parseInt(entityHash)));
			if(list.size() > 0) {
				detail = list.get(0);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void onClearSearch() {
		entityHash = null;
		
		selectedProperty = null;
		
		selectedAction = null;
		
		selectedObjectId = null;
		
		searchpropertyValue = null;
		
		changedFrom = null;
		
		changedTo = null;

		lastValueOnly = true;
		
		detail = null;

		onHideDetail();
	}

	public void onSuccessFromSearch() {

		if(selectedAction != null && "ACTION".equals(changeFrom)) {
			lastValueOnly = false;
		}
		if(entityHash != null && "ENTITY".equals(changeFrom)) {
			lastValueOnly = true;
			selectedProperty = null;
			selectedObjectId = null;
		}
		if(selectedProperty != null && searchpropertyValue != null && !"SELONLY".equals(changeFrom)) {
			lastValueOnly = false;
			selectedAction = null;
		}
		changeFrom = null;
		onHideDetail();
	}

	@OnEvent(value = "provideCompletions", component="selectedObjectId")
	List<String> onIdSearch(String fragment) {

		QueryConstraints qc = QueryFactory.create();
		qc.add(Restrictions.like("objectId", fragment));
		qc.add(Restrictions.equal("auditedEntity", auditService.getAuditedEntityService().read(Integer.parseInt(entityHash))));
		
		qc.projections().distinct("objectId");

		List<String> list = auditService.query(qc);
		return list;
	}
	
	void onClrObjId() {
		selectedObjectId = null;
		lastValueOnly = true;
	}
	
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	public String formatDate(Date date) {
		return date == null ? "" : SDF.format(date);
	}
	
}
