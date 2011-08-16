package net.fishear.data.hibernate.dao;

import net.fishear.data.generic.dao.ClassBasedDaoSource;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.utils.Exceptions;

public class HibernateDaoSource extends ClassBasedDaoSource
{

	public HibernateDaoSource(Class<? extends AbstractHibernateDao<? extends EntityI<?>>> daoClass) {
		super(daoClass);
	}

	public static void init() {
		init(HibernateDao.class);
	}

	@SuppressWarnings("unchecked")
	public static void init(Class<?> daoClass) {
		try {
			DaoSourceManager.registerDaoSource(new HibernateDaoSource((Class<? extends AbstractHibernateDao<? extends EntityI<?>>>) daoClass), "hibernateDefault");
		} catch(Exception ex) {
			throw Exceptions.runtime(ex);
		}
	}

	@Override
	public boolean supportsAutoregistrationEntities() {
		return false;
	}

	@Override
	public void registerEntity(Class<EntityI<?>> entity) {
		
	}

	@Override
	public void registerPackage(String packageName, int subclassLevel) {

	}

	@Override
	public boolean supportsAutoregistrationPackages() {
		return false;
	}
}
