package net.fishear.data.hibernate;

import net.fishear.utils.Defender;
import net.fishear.utils.Globals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;

/**
 * Classis Hibernate Session source, ThreadLocal bases.
 * Session is held in thread local variable, therefore the session is valis per thread.
 * 
 * @author terber
 *
 */
public class ClassisSessionSource
	implements SessionSourceI
{
	private static final Logger log = Globals.getLogger();

	private final ThreadLocal<Session> sessionHolder = new ThreadLocal<Session>();

	private SessionFactory sessionFactory;

	public ClassisSessionSource(SessionFactory sessionFactory) {
		Defender.notNull(sessionFactory, "sessionFactory");
		this.sessionFactory = sessionFactory;
	}

	/** returns Hibernate session associated to current thread. 
	 * If session has been already created, returns this one.
	 * @return session
	 */
	@Override
	public Session getSession() {

		Session ses = sessionHolder.get();
		if(ses == null) {
			ses = sessionFactory.openSession();
			sessionHolder.set(ses);
		}
		return ses;
	}

	@Override
	public void releaseSession() {
		sessionHolder.set(null);
	}

	public static void init(SessionFactory sessionFactory) {
		if(HibernateContext.getSessionSource() == null) {
			HibernateContext.setSessionSource(new ClassisSessionSource(sessionFactory));
		} else {
			log.warn("Hibernate session source already set to {}", HibernateContext.getSessionSource().getClass().getName());
		}
	}

}
