package net.fishear.data.generic.dao;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.exceptions.EntityRegistrationNotSupportedException;
import net.fishear.data.generic.services.CurrentStateSourceI;
import net.fishear.exceptions.AppException;



/**
 * This class expects the DAO class (passed to it's constructor) as superclass for each DAO.
 * This DAO class has constructor with one parameter = the required entity class.
 * This constructor is looked for and it is called out to create new DAO instance.
 * 
 * @author terber
 *
 */
public class ClassBasedDaoSource implements DaoSourceI
{

	Logger log = LoggerFactory.getLogger(getClass());
	
	private Class<?> daoClass;

	private CurrentStateSourceI currentStateSource;

	public ClassBasedDaoSource(Class<?> daoClass) {
		this.daoClass = daoClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EntityI<?>> GenericDaoI<T> createDao(Class<T> entityType) {
		Class<?> clazz = daoClass;
		try {
			Constructor<?> constru = clazz.getConstructor(Class.class);
			GenericDaoI<T> dao = (GenericDaoI<T>) constru.newInstance(entityType);
			try {
				// try to set DAO source 
				clazz.getMethod("setDaoSource", DaoSourceI.class).invoke(dao, this);
			} catch(Exception ex) {
				log.warn("Cannot set daoSource to the DAO: {}", ex.toString());
			}
			return dao;
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	@Override
	public boolean supportsAutoregistrationEntities() {
		return false;
	}

	@Override
	public void registerEntity(Class<EntityI<?>> entity) {
		throw new EntityRegistrationNotSupportedException(String.format("Required entity: %s", entity.getClass().getName()));
	}

	@Override
	public void registerPackage(String packageName, int subclassLevel) {
		throw new EntityRegistrationNotSupportedException(String.format("Required package: %s", packageName));
	}

	@Override
	public boolean supportsAutoregistrationPackages() {
		return false;
	}

	@Override
	public CurrentStateSourceI getCurrentStateSource() {
		return currentStateSource;
	}

	@Override
	public void setCurrentStateSource(CurrentStateSourceI currentStateSource) {
		this.currentStateSource = currentStateSource;
	}

}
