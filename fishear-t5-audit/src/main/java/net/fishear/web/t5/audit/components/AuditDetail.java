package net.fishear.web.t5.audit.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.entities.AuditedEntity;
import net.fishear.data.audit.services.AuditChangeService;
import net.fishear.data.audit.services.AuditService;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.AuditServiceI.Action;
import net.fishear.utils.EntityUtils;
import net.fishear.web.t5.audit.pages.audit.AuditIndex;
import net.fishear.web.t5.base.ComponentBase;

public class AuditDetail extends ComponentBase {

	@Inject
	AuditService auditService;
	
	@Parameter
	@Property
	private Audit audit;

	@Property
	DspProp row;

	@Cached(watch="row")
	public String getLinkEntityId() {
		if(row.property.getValueType() != null && EntityI.class.isAssignableFrom(row.property.getValueType())) {
			String chto = row.getCurrentValue();
			if(chto != null && chto.trim().length() > 0 && !AuditChangeService.NA.equals(chto)) {
				return chto;
			}
			String cur = row.getCurrentValue();
			if(cur != null && cur.trim().length() > 0 && !AuditChangeService.NA.equals(cur)) {
				return cur;
			}
		}
		return null;
	}
	
	@Cached
	public List<DspProp> getDisplayProeprties() {

		List<DspProp> list = new ArrayList<AuditDetail.DspProp>();
		Map<String, String> pp = getPropertyValues();

		for(net.fishear.utils.EntityUtils.Property p : getAllProperties()) {
			DspProp dp = new DspProp();
			list.add(dp);
			dp.setPropertyName(p.getName());
			dp.setNewValue(pp.containsKey(p.getName()) ? pp.get(p.getName()) : AuditChangeService.NA);
			dp.setCurrentValue(getCurretnValue(p.getName()));
			dp.setOldValue(auditService.getAuditChangeService().getPreviousValue(audit, p.getName()));
			dp.property = p;
		}

		return list;
	}
	
	private String getCurretnValue(String propName) {
		if(audit.getAction() == Action.DELETE) {
			return AuditChangeService.NA;
		} else {
			return auditService.getAuditChangeService().getCurrentValue(audit, propName);
		}
	}
	
	@Cached
	public List<net.fishear.utils.EntityUtils.Property> getAllProperties() {
		List<net.fishear.utils.EntityUtils.Property> diffs = null;
		try {
			diffs = EntityUtils.fillDifferencies(
				getClass().getClassLoader().loadClass(audit.getAuditedEntity().getClassName()).newInstance(), 
				true
			);
		} catch(Exception ex) {
//			ex.printStackTrace();
			diffs = new ArrayList<net.fishear.utils.EntityUtils.Property>();
			Map<String, String> pp = getPropertyValues();
			for(Entry<String, String> p : pp.entrySet()) {
				diffs.add(new net.fishear.utils.EntityUtils.Property(p.getKey(), null, null, null));
			}
		}
		return diffs;
	}

	@Cached
	public Map<String, String> getPropertyValues() {
		Map<String, String> map = new TreeMap<String, String>();
		for(AuditChange ch : auditService.getAuditChangeService().listForAudit(audit)) {
			map.put(ch.getPropertyName(), ch.getNewValue());
		}
		return map;
	}

	public Link onFollowEntity(String entityHash, String entityId) {
		return prsc.createPageRenderLinkWithContext(AuditIndex.class, entityHash, entityId);
	}

	public String getEntityShortName(AuditedEntity en) {
		String cn = en.getClassName();
		return cn.substring(cn.lastIndexOf('.') + 1);
	}

	public static class DspProp {

		private net.fishear.utils.EntityUtils.Property property;

		private String propertyName;

		private String oldValue;

		private String newValue;
		
		private String currentValue;

		/**
		 * @return the propertyName
		 */
		public String getPropertyName() {
			return propertyName;
		}

		/**
		 * @param propertyName the propertyName to set
		 */
		public void setPropertyName(String key) {
			this.propertyName = key;
		}

		/**
		 * @return the oldValue
		 */
		public String getOldValue() {
			return oldValue;
		}

		/**
		 * @param oldValue the oldValue to set
		 */
		public void setOldValue(String oldValue) {
			this.oldValue = oldValue;
		}

		/**
		 * @return the newValue
		 */
		public String getNewValue() {
			return newValue;
		}

		/**
		 * @param newValue the newValue to set
		 */
		public void setNewValue(String newValue) {
			this.newValue = newValue;
		}

		/**
		 * @return the currentValue
		 */
		public String getCurrentValue() {
			return currentValue;
		}

		/**
		 * @param currentValue the currentValue to set
		 */
		public void setCurrentValue(String currentValue) {
			this.currentValue = currentValue;
		}

		public String getEntityHash() {
			return property.getValueType() == null ? null : Integer.toString(property.getValueType().getName().hashCode());
		}
		
	}
	
}
