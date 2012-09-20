package net.fishear.data.generic.services;



import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.AbstractEntity;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.StandardEntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.exceptions.TooManyRecordsException;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.exceptions.AppException;
import net.fishear.utils.Defender;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.EntityUtils.FillFlags;
import net.fishear.utils.Texts;

import org.apache.commons.beanutils.BeanUtilsBean;

/** The abstract implementation of service interface.
 * For each real service, it is sufficient to extend this service to provide basic CRUD opearations.
 * 
 * @author terber
 *
 * @param <K> entity this service serve
 */
public abstract class
	GenericService<K extends EntityI<?>>
implements
	ServiceI<K> 
{

    private final GenericDaoI<K> thisDao;

    public GenericService(GenericDaoI<K> genericDao) {
        this.thisDao = genericDao;
    }

    public GenericService() {
        this.thisDao = DaoSourceManager.createDao(findType(), getClass());
    }

    @SuppressWarnings("unchecked")
	public GenericService(Class<? extends EntityI<?>> entityType) {
        this.thisDao = (GenericDaoI<K>) DaoSourceManager.createDao(entityType, getClass());
    }

	@Override
    public K read(Object id) {
        K result = (K)getDao().read(id);
        return result;
    }

	@Override
    public void delete(K entity) {
        getDao().delete(entity);
    }

	@Override
    public boolean delete(Object id) {
		GenericDaoI<K> dao = getDao();
		K k = dao.read(id);
		if(k != null) {
	        dao.delete(k);
	        return true;
		}
		return false;
    }

	@Override
    public void deleteAll(Collection<K> list) {
    	for (K k : list) {
			delete(k);
		}
    }

    /** In dependency on ID's value, either creates new record or updates existing record.
     * If operation successes, ID of record is returned.
     * If id id null or zero value, creates new record.
     * This method call it's methods "create" or "update" (it DOES NOT call DAO operations directly), 
     * if subclass extends those methods, subclasses methods are called.
     * @param dataObject
     */
	@Override
    public Object save(K dataObject) {
		fillStandardEntity(dataObject);
		return getDao().save(dataObject);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void fillStandardEntity(K dataObject) {
		if(dataObject instanceof StandardEntityI) {
			Date date = new Date();
			CurrentStateI state = getCurrentState();
			Object user = state == null ? null : state.getCurrentUser();
			StandardEntityI stde = (StandardEntityI)dataObject;
			if(dataObject.isNew()) {
				if(stde.getCreateDate() == null) { stde.setCreateDate(date); }
				if(stde.getCreateUser() == null) { stde.setCreateUser(user); }
			}
			stde.setUpdateDate(date);
			stde.setUpdateUser(user);
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Q> List<Q> query(QueryConstraints constraints) {
    	GenericDaoI<K> dao = getDao();
    	List<Q> result = (List<Q>) dao.query(constraints == null ? QueryFactory.createDefault() : constraints);
        return result;
    }

	@Override
    public long queryCount(QueryConstraints constraints) {
    	QueryConstraints qc = QueryFactory.copyOrCreate(constraints);
        qc.results().addRowCount();
        return ((Number)query(qc).get(0)).intValue();
    }

    public GenericDaoI<K> getDao() {
        return thisDao;
    }

	@Override
    public void flushAll() {
		getDao().flush();
    }

	@Override
    public boolean isLazyLoaded(K entity, String propertyName) {
    	return getDao().isLazyLoaded(entity, propertyName);
    }

	@Override
	public List<K> list(QueryConstraints constraints) {
    	List<K> list = query(constraints == null ? QueryFactory.fullResult() : constraints);
    	return list == null ? new ArrayList<K>() : list;
    }

    @Override
    public K newEntityInstance() {
    	return getDao().newEntityInstance();
    }
    
    @Override
    public K syncRead(K entity) {
    	if(entity == null) {
    		return newEntityInstance();
    	}
    	if(entity.isNew()) {
    		return entity;
    	}
    	K nent = read(entity.getId());
    	if(nent == null) {
    		return entity;
    	}
    	EntityUtils.fillDestination(entity, nent, FillFlags.OVERWRITE_BY_NULLS);
    	return nent;
    }

	@Override
    public K read(QueryConstraints qc) throws TooManyRecordsException {
		List<K> list = list(qc);

		if(list.size() == 0) {
			return null;
		}
		if(list.size()  > 1) {
			throw new TooManyRecordsException("only-one-record-expected-but-more-found", list.size(), getDao().type(), qc.toString());
		}
		
		return list.get(0);
    }

	@Override
	public boolean existsEntity(K entity, String... propertyNames) {
		try {
			QueryConstraints qc = QueryFactory.createDefault();
			if(!entity.isNew()) {
				qc.add(Restrictions.notEqual("id", entity.getId()));
			}
			for (int i = 0; i < propertyNames.length; i++) {
				Object val = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(entity, propertyNames[i]);
				qc.add(Restrictions.equal(propertyNames[i], val));
			}
			List<K> list = list(qc);
			if(list.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	@Override
	public Class<K> getEntityType() {
		return getDao().type();
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
	public void validate(K entity) {
		
	}

	@Override
	public K fillNewEntity(K entity) {
		if(!entity.isNew()) {
			K e1 = read(entity.getId());
			EntityUtils.fillDestination(entity, e1);
			return e1;
		}
		return entity;
	}

	@Override
	public Map<?, String> getIdValueMap(QueryConstraints qc, String... attrNames) {
		return getIdValueMapInternal(qc, false, attrNames);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getIdStringValueMap(QueryConstraints qc, String... attrNames) {
		return (Map<String, String>) getIdValueMapInternal(qc, true, attrNames);
	}

	private Map<?, String> getIdValueMapInternal(QueryConstraints qc, boolean cast, String... attrNames) {
		Defender.notEmpty(attrNames, "attrNames");
		Map<Object, String> map = new HashMap<Object, String>();
		List<K> list = list(qc);
		for (K pt : list) {
			if(!(pt instanceof AbstractEntity)) {
				throw new IllegalStateException(String.format("Entity '%s' is not descendant of '%s'.", pt.getClass(), AbstractEntity.class));
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < attrNames.length; i++) {
				String s = Texts.tos(attrNames[i]);
				if((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("\'") && s.endsWith("\'"))) {
					sb.append(s.substring(1, s.length() - 1));
				} else {
					sb.append(((AbstractEntity)pt).getAttributeValue(s));
				}
			}
			if(cast)  {
				map.put(pt.getId().toString(), sb.toString());
			} else {
				map.put(pt.getId(), sb.toString());
			}
		}
		return map;
	}

	public CurrentStateI getCurrentState() {
		return null;
	}
}
