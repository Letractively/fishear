package net.fishear.data.hibernate;

import org.hibernate.Session;

public interface SessionSourceI extends HibernateConfigurationSourceI
{

	Session getSession();

	void releaseSession();

}
