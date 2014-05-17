package net.fishear.t5.hibernate;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.hibernate.HibernateContext;
import net.fishear.data.hibernate.dao.AbstractHibernateDao;

import org.apache.tapestry5.hibernate.HibernateSessionManager;

public class 
	T5HibernateDao<K extends EntityI<?>>
extends
	AbstractHibernateDao<K>
{

	public T5HibernateDao() {
		super();
	}

	public T5HibernateDao(Class<K> type) {
		super(type);
	}

	private HibernateSessionManager hctx() {
		return ((T5HibernateSessionSource) HibernateContext.getSessionSource()).getHibernateSessionManager();
	}

	public void transaction() {

	}

	public void commit() {
		hctx().commit();
	}

	public void rollback() {
		hctx().abort();
	}

}
