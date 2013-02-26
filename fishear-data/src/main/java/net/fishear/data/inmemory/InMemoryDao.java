package net.fishear.data.inmemory;


import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.DatabaseDaoI;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryParser;
import net.fishear.data.generic.services.CurrentStateSourceI;
import net.fishear.data.inmemory.query.InMemoryQueryParser;
import net.fishear.exceptions.AppException;



public class 
	InMemoryDao<K extends EntityI<?>>
implements 
	GenericDaoI<K>,
	DatabaseDaoI
{

	final List<K> dataList = new ArrayList<K>();
	
	private QueryParser<QueryConstraints, InMemoryCriteria> queryParser = new InMemoryQueryParser();

	private Class<K> type;

	private DaoSourceI daoSource;

	public InMemoryDao() {
		this.type = findType();
	}

	public InMemoryDao(Class<K> type) {
		this.type = type;
	}
	
	private void generateId(K entity) {
		if(entity.getId() == null) {
			try {
				entity.setIdString(new Long((long)(((double)Math.random() * (double)Math.random()) * (double)Long.MAX_VALUE)).toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public K read(Object id) {
		for(K entity : dataList) {
			if(entity.getId().equals(id)) {
				return entity;
			}
		}
		return null;
	}

	public void delete(K entity) {
		K e = read(entity.getId());
		if(e != null) {
			dataList.remove(e);
		}
	}

	public Object save(K entity) {
		entity.beforeSave();
		if(entity.isNew()) {
			generateId(entity);
		} else {
			K e = read(entity.getId());
			if(e != null) {
				dataList.remove(e);
			}
		}
		dataList.add(entity);
		return entity.getId();
	}

	@Override
	public void flush() {

	}

	@Override
	public boolean isLazyLoaded(Object entity, String propertyName) {
		return false;
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
	public List<?> query(QueryConstraints qc) {

		return parseQueryConstraints(qc).list();
		
	}

	@Override
	public Class<K> type() {
		return type;
	}

	private InMemoryCriteria createCriteria() {
		return new InMemoryCriteria(this);
	}

	private InMemoryCriteria parseQueryConstraints(QueryConstraints qc) {
		InMemoryCriteria criteria = createCriteria();
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
	public void commit() {

	}

	@Override
	public void rollback() {

	}

	@Override
	public void transaction() {

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
	public Integer executeUpdate(String query, Map<String, Object> paramsMap) {
		throw new IllegalStateException("Method is not implemented");
	}

	@Override
	public K refresh(K entity) {
		return entity;
	}

	@Override
	public List<?> executeQuery(String query, Map<String, Object> paramsMap) {
		throw new IllegalStateException("Method is not implemented");
	}

	@Override
	public List<?> executeSqlQuery(String query, Map<String, Object> paramsMap) {
		throw new IllegalStateException("Method is not implemented");
	}

	@Override
	public Iterator<?> iterateQuery(String query, Map<String, Object> paramsMap) {
		throw new IllegalStateException("Method is not implemented");
	}

	@Override
	public Iterator<?> iterateSqlQuery(String query, Map<String, Object> paramsMap) {
		throw new IllegalStateException("Method is not implemented");
	}

	@Override
	public Integer executeSqlUpdate(String query, Map<String, Object> paramsMap) {
		throw new IllegalStateException("Method is not implemented");
	}
}
