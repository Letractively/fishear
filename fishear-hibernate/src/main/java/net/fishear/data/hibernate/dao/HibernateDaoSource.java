package net.fishear.data.hibernate.dao;

import net.fishear.data.generic.dao.ClassBasedDaoSource;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.exceptions.EntityRegistrationNotSupportedException;
import net.fishear.data.hibernate.HibernateConfigurationSourceI;
import net.fishear.utils.Exceptions;

public class HibernateDaoSource extends ClassBasedDaoSource
{

	private HibernateConfigurationSourceI configurationSource;
	
	public HibernateDaoSource(Class<? extends AbstractHibernateDao<? extends EntityI<?>>> daoClass) {
		super(daoClass);
	}

	public static void init(HibernateConfigurationSourceI conf) {
		init(HibernateDao.class, conf);
	}

	@SuppressWarnings("unchecked")
	public static void init(Class<?> daoClass, HibernateConfigurationSourceI conf) {
		try {
			HibernateDaoSource daosrc = new HibernateDaoSource((Class<? extends AbstractHibernateDao<? extends EntityI<?>>>) daoClass);
			daosrc.configurationSource = conf;
			DaoSourceManager.registerDaoSource(daosrc, "hibernateDefault");
		} catch(Exception ex) {
			throw Exceptions.runtime(ex);
		}
	}

	@Override
	public boolean supportsAutoregistrationEntities() {
		return false;
	}

	@Override
	public void registerEntity(Class<?> entity) {
		if(configurationSource == null) {
			throw new IllegalStateException("'configurationSource' is null; autoregistration is not supported");
		}
		configurationSource.getConfiguration().addAnnotatedClass(entity);
	}

	@Override
	public void registerEntityName(String entityClassName) {
		try {
			registerEntity(getClass().getClassLoader().loadClass(entityClassName));
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void registerPackage(String packageName) {
		if(configurationSource == null) {
			throw new IllegalStateException("'configurationSource' is null; autoregistration is not supported");
		}
		configurationSource.getConfiguration().addPackage(packageName);
	}

	@Override
	public boolean supportsAutoregistrationPackages() {
		return false;
	}

	/**
	 * @return the configurationSource
	 */
	public HibernateConfigurationSourceI getConfigurationSource() {
		return configurationSource;
	}

	/**
	 * @param configurationSource the configurationSource to set
	 */
	public void setConfigurationSource(HibernateConfigurationSourceI conf) {
		this.configurationSource = conf;
	}
}
