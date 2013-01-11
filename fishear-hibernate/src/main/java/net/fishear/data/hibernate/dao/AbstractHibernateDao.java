package net.fishear.data.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.exceptions.DataException;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryParser;
import net.fishear.data.generic.services.CurrentStateSourceI;
import net.fishear.data.hibernate.HibernateContext;
import net.fishear.data.hibernate.query.HibernateQueryParser;
import net.fishear.exceptions.AppException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class 
	AbstractHibernateDao<K extends EntityI<?>>
implements 
	GenericDaoI<K>
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

	@Override
	public Integer executeUpdate(String query, Object... parameters) {

		Map<String, Object> map = new HashMap<String, Object>();

		if(parameters != null) {

			if(parameters.length %2 == 1) {
				throw new IllegalArgumentException("Parameter count must be even number");
			}
			
			for(int i = 0; i < parameters.length; i += 2) {
				if(parameters[i] == null || !(parameters[i] instanceof String)) {
					throw new IllegalArgumentException(String.format("Each even parameter must be not null string value, but parameter at index %s ('%s)' is not.", i, parameters[i]));
				}
				String key = parameters[i].toString();
				Object val = parameters[i + 1];
				map.put(key, val);
			}
		}
		return executeUpdate(query, map);
	}

	@Override
	public Integer executeUpdate(String query, Map<String, Object> paramsMap) {
		log.debug("Execute bulk query: {}, params: {}", query, paramsMap);
		Query q = getSession().createQuery(query);
		if(paramsMap != null && paramsMap.size() > 0) {
			for(String key : paramsMap.keySet()) {
				Object val = paramsMap.get(key);
				log.trace("Setting parameter '{}' to value '{}'", key, val);
				q.setParameter(key, val);
			}
		}
		Integer ret = q.executeUpdate();
		log.debug("Bulk query {} affected %s records", ret);
		return ret;
	}
}
