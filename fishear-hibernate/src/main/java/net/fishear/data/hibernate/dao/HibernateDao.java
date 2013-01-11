package net.fishear.data.hibernate.dao;

import java.util.Map;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.CurrentStateSourceI;

import org.hibernate.Transaction;



public class 
	HibernateDao<K extends EntityI<?>>
extends 
	AbstractHibernateDao<K>
{

	private Transaction tx;

	public HibernateDao() {
		super();
	}

	public HibernateDao(Class<K> type) {
		super(type);
	}

	@Override
	public void commit() {
		if(tx != null) {
			tx.commit();
			tx = null;
		}
	}

	@Override
	public void rollback() {
		if(tx != null) {
			tx.rollback();
			tx = null;
		}
	}

	@Override
	public void transaction() {
		if(tx == null) {
			this.tx = getSession().beginTransaction();
		} else if(!tx.isActive()) {
			this.tx = getSession().beginTransaction();
		}
	}
}
