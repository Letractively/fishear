package net.fishear.web.t5.audit.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.entities.AuditChange;
import net.fishear.data.audit.entities.AuditedEntity;
import net.fishear.data.audit.services.AuditService;
import net.fishear.utils.EntityUtils;
import net.fishear.web.t5.base.ComponentBase;

public class AuditDetail extends ComponentBase {

	@Inject
	AuditService auditService;
	
	@Parameter
	@Property
	private Audit audit;

	@Cached
	public List<DspProp> getDisplayProeprties() {
		
		List<DspProp> list = new ArrayList<AuditDetail.DspProp>();
		Map<String, String> pp = getPropertyValues();

		for(net.fishear.utils.EntityUtils.Property p : getAllProperties()) {
			DspProp dp = new DspProp();
			list.add(dp);
			dp.setKey(p.getName());
			dp.setNewValue(pp.get(p.getName()));
		}

		return list;
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
			ex.printStackTrace();
			diffs = new ArrayList<net.fishear.utils.EntityUtils.Property>();
			Map<String, String> pp = getPropertyValues();
			for(Entry<String, String> p : pp.entrySet()) {
				diffs.add(new net.fishear.utils.EntityUtils.Property(p.getKey(), null, null));
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

	public String getEntityShortName(AuditedEntity en) {
		String cn = en.getClassName();
		return cn.substring(cn.lastIndexOf('.') + 1);
	}

	public static class DspProp {
		
		private String key;

		private String oldValue;

		private String newValue;
		
		private String user;

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
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
		 * @return the user
		 */
		public String getUser() {
			return user;
		}

		/**
		 * @param user the user to set
		 */
		public void setUser(String user) {
			this.user = user;
		}

	}
	
}
