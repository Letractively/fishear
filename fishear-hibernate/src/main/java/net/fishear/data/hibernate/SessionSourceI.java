package net.fishear.data.hibernate;

import org.hibernate.Session;

public interface SessionSourceI
{

	Session getSession();

	void releaseSession();

}
