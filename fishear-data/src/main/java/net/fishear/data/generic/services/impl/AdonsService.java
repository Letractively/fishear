package net.fishear.data.generic.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.AdonsI;
import net.fishear.data.generic.services.AuditServiceI;

public class 
	AdonsService 
implements
	AdonsI
{
	private Logger log = LoggerFactory.getLogger(getClass());

	private volatile boolean auditServiceInitialized;

	private volatile AuditServiceI auditService;
	
	private DaoSourceI daoSource;

	public AdonsService(DaoSourceI daoSource) {
		this.daoSource = daoSource;
	}

	@Override
	public AuditServiceI getAuditService() {
		if(!auditServiceInitialized) {
			synchronized(this) {
				initializeAudit();
			}
		}
		return auditService;
	}

	@SuppressWarnings("unchecked")
	private synchronized void initializeAudit() {
		if(!auditServiceInitialized) {

			log.debug("Initializing audit");

			ClassLoader cl = getClass().getClassLoader();

			String auditServiceClass = "net.fishear.data.audit.services.AuditService";
			String auditHeaderEntityClass = "net.fishear.data.audit.entities.AuditHader";
			String auditDetailEntityClass = "net.fishear.data.audit.entities.AuditChange";
			String auditEntityClass = "net.fishear.data.audit.entities.AuditEntity";

			try {
				daoSource.registerEntity((Class<EntityI<?>>) cl.loadClass(auditHeaderEntityClass));
				daoSource.registerEntity((Class<EntityI<?>>) cl.loadClass(auditDetailEntityClass));
				daoSource.registerEntity((Class<EntityI<?>>) cl.loadClass(auditEntityClass));

				auditService = (AuditServiceI) cl.loadClass(auditServiceClass).newInstance();
				auditService.initForcedInstance();

				log.info("Instance of AuditService has been created");
			} catch(Exception ex) {
				log.info("Audit Service Class canot be loaded: {}. Cause: {}", auditServiceClass, ex.toString());
			}
			auditServiceInitialized = true;
		}
	}
		
}
