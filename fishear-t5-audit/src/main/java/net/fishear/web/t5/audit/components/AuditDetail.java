package net.fishear.web.t5.audit.components;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Parameter;

import net.fishear.data.audit.entities.Audit;
import net.fishear.data.audit.services.AuditService;
import net.fishear.web.t5.base.ComponentBase;

public class AuditDetail extends ComponentBase {

	@Inject
	AuditService auditService;
	
	@Parameter
	private Audit audit;

	/**
	 * @return the audit
	 */
	public Audit getAudit() {
		return audit;
	}

	/**
	 * @param audit the audit to set
	 */
	public void setAudit(Audit audit) {
		this.audit = audit;
	}

}
