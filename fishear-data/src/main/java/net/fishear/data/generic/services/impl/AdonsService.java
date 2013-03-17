package net.fishear.data.generic.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fishear.data.FishearDataConstants.Classes;
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

			try {
				daoSource.registerEntity((Class<EntityI<?>>) cl.loadClass(Classes.AUDIT));
				daoSource.registerEntity((Class<EntityI<?>>) cl.loadClass(Classes.AUDIT_CHANGE));
				daoSource.registerEntity((Class<EntityI<?>>) cl.loadClass(Classes.AUDIT_ENTITY));

				log.info("Instance of AuditService has been created");
			} catch(Exception ex) {
				log.info("Exception during registering audit entity: {}. Audit framework may not work properly", ex.toString());
			}

			try {

				auditService = (AuditServiceI) cl.loadClass(Classes.AUDIT_SERVICE_IMPL).newInstance();
				auditService.initForcedInstance();

				log.info("Instance of AuditService has been created");
			} catch(Exception ex) {
				log.info("Audit Framework canot be initialized. Cause: {}", ex.toString());
			}
			auditServiceInitialized = true;
		}
	}
		
}
