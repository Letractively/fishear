package net.fishear.data.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.exceptions.DataException;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryParser;
import net.fishear.data.hibernate.HibernateContext;
import net.fishear.data.hibernate.query.HibernateQueryParser;
import net.fishear.exceptions.AppException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;



public abstract class 
	AbstractHibernateDao<K extends EntityI<?>>
implements 
	GenericDaoI<K>
{

	private QueryParser<QueryConstraints, Criteria> queryParser = new HibernateQueryParser();

	private Class<K> type;

	public AbstractHibernateDao() {
		this.type = findType();
	}

	public AbstractHibernateDao(Class<K> type) {
		this.type = type;
	}

	public Object create(K entity) {
		entity.beforeSave();
		return getSession().save(entity);
	}

	@SuppressWarnings("unchecked")
	public K read(Object id) {
		if(!(id instanceof Serializable)) {
			throw new DataException(String.format("Entity ID must implement Serializable interface. Entity: %s", type.getName()));
		}
		return (K) getSession().get(type, (Serializable) id);
	}

	public void update(K entity) {
		getSession().update(entity);
	}

	public void delete(K entity) {
		getSession().delete(entity);
	}

	public Session getSession() {
		return HibernateContext.getSession();
	}

	public Object save(K entity) {
		entity.beforeSave();
		return getSession().save(entity);
	}

	@Override
	public void flush() {
		getSession().flush();
	}

	@Override
	public boolean isLazyLoaded(Object entity, String propertyName) {
		return Hibernate.isPropertyInitialized(entity, propertyName);
	}

	@Override
	public K newEntityInstance() {
		try {
			K entity = type.newInstance();
			return entity;
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	@Override
	public List<?> query(QueryConstraints queryConstraints) {
		return parseQueryConstraints(queryConstraints).list();
	}

	@Override
	public Class<K> type() {
		return type;
	}

	private Criteria createCriteria() {
		Criteria result = getSession().createCriteria(type);
		return result;
	}

	private Criteria parseQueryConstraints(QueryConstraints qc) {
		Criteria criteria = createCriteria();
		queryParser.parse(qc, criteria);
		return criteria;
	}

	@SuppressWarnings("unchecked")
	private Class<K> findType() {
		Class<?> clazz = this.getClass();
		while(clazz != Object.class) {
			Object gscl = clazz.getGenericSuperclass();
			if(ParameterizedType.class.isAssignableFrom(gscl.getClass())) {
				ParameterizedType pt = (ParameterizedType)gscl;
				Object[] oa = pt.getActualTypeArguments();
				if(oa != null && oa.length > 0) {
					return (Class<K>)oa[0];
				}
			}
			clazz = clazz.getSuperclass();
		}
		throw new AppException("Subclass does not parametrize generic superclass.");
	}

}
