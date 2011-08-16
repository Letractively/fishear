package net.fishear.data.hibernate;

import net.fishear.utils.Globals;

import org.hibernate.Session;
import org.slf4j.Logger;

/** The main class to hold global application informations.
 * @author terber
 */
public class 
	HibernateContext
{
	private static final Logger log = Globals.getLogger();

	private static SessionSourceI sessionSource;

	public static SessionSourceI getSessionSource() {
		return sessionSource;
	}

	public static void setSessionSource(SessionSourceI sessionSource) {
		HibernateContext.sessionSource = sessionSource;
		log.info("Hibernate session has been set to {}", sessionSource == null ? null : sessionSource.getClass().getName());
	}

	public static Session getSession() {
		return sessionSource.getSession();
	}

}
