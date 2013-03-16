package net.fishear.data.hibernate;

import net.fishear.utils.Defender;
import net.fishear.utils.Globals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;

/**
 * Classis Hibernate Session source, ThreadLocal bases.
 * Session is held in thread local variable, therefore the session is valis per thread.
 * 
 * @author terber
 *
 */
public class 
	HibernateSessionSource
implements 
	SessionSourceI,
	HibernateConfigurationSourceI
{
	private static final Logger log = Globals.getLogger();

	private final ThreadLocal<Session> sessionHolder = new ThreadLocal<Session>();
	
	private Configuration configuration;

	private SessionFactory sessionFactory;

	public HibernateSessionSource(Configuration configuration) {
		Defender.notNull(configuration, "configuration");
		this.configuration = configuration;
	}

	/** returns Hibernate session associated to current thread. 
	 * If session has been already created, returns this one.
	 * @return session
	 */
	@Override
	public Session getSession() {

		if(sessionFactory == null) {
			this.sessionFactory = buildSessionFactory();
		}
		
		Session ses = sessionHolder.get();
		if(ses == null) {
			ses = sessionFactory.openSession();
			sessionHolder.set(ses);
		}
		return ses;
	}

	private synchronized SessionFactory buildSessionFactory() {
		return configuration.buildSessionFactory();
	}

	@Override
	public void releaseSession() {
		sessionHolder.set(null);
	}

	public static void init(Configuration conf) {
		if(HibernateContext.getSessionSource() == null) {
			HibernateContext.setSessionSource(new HibernateSessionSource(conf));
		} else {
			log.warn("Hibernate session source already set to {}", HibernateContext.getSessionSource().getClass().getName());
		}
	}

	/**
	 * @return the configuration
	 */
	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(Configuration configuration) {
		Defender.notNull(configuration, "configuration");
		this.configuration = configuration;
	}

}
