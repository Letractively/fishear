package net.fishear.data.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.DatabaseDaoI;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.exceptions.DataException;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryParser;
import net.fishear.data.hibernate.HibernateContext;
import net.fishear.data.hibernate.query.HibernateQueryParser;
import net.fishear.exceptions.AppException;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.Maps;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class 
	AbstractHibernateDao<K extends EntityI<?>>
extends 
	Maps
implements 
	GenericDaoI<K>,
	DatabaseDaoI
{

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private QueryParser<QueryConstraints, Criteria> queryParser = new HibernateQueryParser();

	private Class<K> type;

	private DaoSourceI daoSource; 

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
		if(id == null) {
			log.warn("'read': entity ID is null");
			return null;
		}
		if(!(id instanceof Serializable)) {
			throw new DataException(String.format("Entity ID must implement Serializable interface. Entity: '%s', ID class '%s', ID value %s", type.getName(), id.getClass().getName(), id));
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
	public boolean isLazyLoaded(K entity, String propertyName) {
		return Hibernate.isPropertyInitialized(entity, propertyName);
	}

	@Override
	public void loadLazy(K entity, String propertyName) {
		Hibernate.initialize(EntityUtils.getRawValue(propertyName, entity, null));
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

	private Criteria createCriteria(QueryConstraints qc) {
		String alias = qc.getAlias();
		if(alias == null || alias.trim().length() == 0) {
			alias = "this";
		}
		log.trace("");
		Criteria result = getSession().createCriteria(type, alias);
		return result;
	}

	private Criteria parseQueryConstraints(QueryConstraints qc) {
		Criteria criteria = createCriteria(qc);
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

	@Override
	public DaoSourceI getDaoSource() {
		return this.daoSource;
	}

	/**
	 * @param daoSource the daoSource to set
	 */
	public void setDaoSource(DaoSourceI daoSource) {
		this.daoSource = daoSource;
	}

	private Query setParams(Query q, Map<String, Object> paramsMap) {
		if(paramsMap != null && paramsMap.size() > 0) {
			for(String key : paramsMap.keySet()) {
				Object val = paramsMap.get(key);
				log.trace("Query parameter ':{}' is set to '{}'", key, val);
				q.setParameter(key, val);
			}
		}
		return q;
	}
	
	@Override
	public Integer executeUpdate(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk update: {}, params: {}", query, paramsMap);
		Integer ret = setParams(getSession().createQuery(query), paramsMap).executeUpdate();
		log.debug("JPA update {} affected {} records", query, ret);
		return ret;
	}

	@Override
	public Integer executeSqlUpdate(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk update: {}, params: {}", query, paramsMap);
		Integer ret = setParams(getSession().createSQLQuery(query), paramsMap).executeUpdate();
		log.debug("Native SQL update {} affected {} records", query, ret);
		return ret;
	}

	@Override
	public List<?> executeQuery(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk query: {}, params: {}", query, paramsMap);
		List<?> ret = setParams(getSession().createQuery(query), paramsMap).list();
		log.debug("JPA query {} returned {} records", query, ret.size());
		return ret;
	}


	@Override
	public List<?> executeSqlQuery(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk query: {}, params: {}", query, paramsMap);
		List<?> ret = setParams(getSession().createSQLQuery(query), paramsMap).list();
		log.debug("Native SQL query {} returned {} records", query, ret.size());
		return ret;
	}


	@Override
	public Iterator<?> iterateQuery(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk query: {}, params: {}", query, paramsMap);
		Iterator<?> ret = setParams(getSession().createQuery(query), paramsMap).iterate();
		log.debug("JPA query {} returned iterator", query);
		return ret;
	}


	@Override
	public Iterator<?> iterateSqlQuery(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk query: {}, params: {}", query, paramsMap);
		Iterator<?> ret = setParams(getSession().createSQLQuery(query), paramsMap).iterate();
		log.debug("Native SQL query {} returned iterator", query);
		return ret;
	}

	@Override
	public K refresh(K entity) {
		if(entity.isNew()) {
			log.warn("Attempt to refresh new (insaved) entity: {}", entity);
		} else {
			log.debug("Refreshing entity: {}", entity);
			getSession().refresh(entity);
		}
		return entity;
	}
}
