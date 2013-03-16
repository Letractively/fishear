package net.fishear.t5.hibernate;

import javax.inject.Inject;

import net.fishear.data.hibernate.HibernateConfigurationSourceI;
import net.fishear.data.hibernate.HibernateContext;
import net.fishear.data.hibernate.SessionSourceI;
import net.fishear.utils.Globals;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;

/** The main class to hold global application informations.
 * @author terber
 */
@EagerLoad
public class 
	T5HibernateSessionSource
implements 
	SessionSourceI,
	HibernateConfigurationSourceI
{

	@Inject
	HibernateSessionSource sessionSource;
	
	private static final Logger log = Globals.getLogger();
	
	private HibernateSessionManager hibernateSessionManager;

	public T5HibernateSessionSource(HibernateSessionManager hibernateSessionManager) {
		super();
		this.hibernateSessionManager = hibernateSessionManager;
		if(HibernateContext.getSessionSource() == null || HibernateContext.getSessionSource() != this) {
			HibernateContext.setSessionSource(this);
		} else {
			log.warn("Hibernate session source already set to {}", HibernateContext.getSessionSource().getClass().getName());
		}
	}

	/** returns Hibernate session associated to current thread. 
	 * This is done in AppModule's 
	 * @return
	 */
	public Session getSession() {
		return hibernateSessionManager.getSession();
	}

	@Override
	public void releaseSession() {
		// do nothing
	}

	public HibernateSessionManager getHibernateSessionManager() {
		return hibernateSessionManager;
	}

	@Override
	public Configuration getConfiguration() {
		return sessionSource.getConfiguration();
	}

}
