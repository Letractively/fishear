package net.fishear.data.generic.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fishear.data.generic.annotations.Eager;
import net.fishear.data.generic.annotations.EagerLoads;
import net.fishear.data.generic.dao.DaoSourceI;
import net.fishear.data.generic.dao.DaoSourceManager;
import net.fishear.data.generic.dao.GenericDaoI;
import net.fishear.data.generic.entities.AbstractEntity;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.InitialStateI;
import net.fishear.data.generic.entities.StandardEntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.exceptions.TooManyRecordsException;
import net.fishear.data.generic.query.restrictions.Restrictions;
import net.fishear.data.generic.services.AuditServiceI.Action;
import net.fishear.exceptions.AppException;
import net.fishear.utils.Classes;
import net.fishear.utils.Defender;
import net.fishear.utils.EntityUtils;
import net.fishear.utils.EntityUtils.FillFlags;
import net.fishear.utils.Texts;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private Object DUMMY_ENTITY;
	
	private static final Class<Annotation> auditable = getAuditable();
	
    private final GenericDaoI<K> thisDao;

    protected final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> eagerProps;
	
	@SuppressWarnings("unchecked")
	private K dummyEntity() {
		if(DUMMY_ENTITY == null) {
			DUMMY_ENTITY = newEntityInstance();
		}
		return (K) DUMMY_ENTITY;
	}
	
	@SuppressWarnings("unchecked")
	private static Class<Annotation> getAuditable() {
		String auditableClass = "net.fishear.data.audit.annotations.Auditable";
		try {
			return (Class<Annotation>) GenericService.class.getClassLoader().loadClass(auditableClass);
		} catch(Exception ex) {
			Logger log = LoggerFactory.getLogger(GenericService.class);
			String emsg = String.format("Auditable annotation class '%s' is not available. Auditing is disabled.", auditableClass);
			log.debug(emsg + " The cause:", ex);
			log.warn(emsg);
			System.err.println(emsg);
			return null;
		}
	}
	
    public GenericService(GenericDaoI<K> genericDao) {
        this.thisDao = genericDao;
        register();
        log.debug("Service instance created with DAO '{}'", this.thisDao);
    }

    public GenericService() {
        this.thisDao = DaoSourceManager.createDao(findType(), getClass());
        register();
        log.debug("Service instance created with DAO '{}'", this.thisDao);
    }

    @SuppressWarnings("unchecked")
	public GenericService(Class<? extends EntityI<?>> entityType) {
        this.thisDao = (GenericDaoI<K>) DaoSourceManager.createDao(entityType, getClass());
        register();
        log.debug("Service instance created for entity type '{}', DAO '{}'", entityType, this.thisDao);
    }

    
	private void register() {
		ServiceHolder.getInstance().registerService(this);
	}

	@Override
    public K read(Object id) {
		K e = read_(id);
		loadEager(e, getEagerProps(null));
		return modifyEntity(e);
	}

	@Override
    public K readOrCreate(Object id) {
		K entity = null;
		if(id != null) {
			log.trace("readOrCreate: ID is null");
			entity = read_(id);
		}
		if(entity == null) {
			log.trace("Entity for id {} is null. Creating new instance.", id);
			entity = newEntityInstance();
		}
		loadEager(entity, getEagerProps(null));
		return modifyEntity(entity);
	}

    private K read_(Object id) {
    	if(checkId(id)) {
    		return (K)getDao().read(id);
    	} else {
    		return null;
    	}
    }

	private boolean checkId(Object id) {
    	if(id == null) {
    		log.warn("Entity ID is null");
    		return false;
    	}
    	if(!id.getClass().equals(dummyEntity().getIdType())) {
    		throw new IllegalStateException(String.format("ID is wrong type. Expected type: %s, passed type: %s", Classes.getShortClassName(dummyEntity().getIdType()), id.getClass()));
    	}
    	return true;
	}

	@Override
    public void delete(K entity) {
		if(entity == null) {
			log.warn("Deleting entity {}: Entity is null");
		} else {
			log.trace("Deleting entity {}", entity);
		}
        getDao().delete(entity);
		checkAudit(entity, AuditServiceI.Action.DELETE);
		log.debug("Entity {} with ID={} has been deleted", entity, entity.getId());
    }

	@Override
    public boolean delete(Object id) {
		log.trace("Deleting entity by ID={}", id);
		if(checkId(id)) {
			GenericDaoI<K> dao = getDao();
			K k = dao.read(id);
			if(k != null) {
				delete(k);
		        return true;
			} else {
				log.debug("Deleting entity by ID: object with ID='{}' not found", id);
			}
		}
		return false;
    }

	@Override
    public void deleteAll(Collection<K> list) {
		if(list == null) {
			return;
		}
    	for (K k : list) {
			delete(k);
		}
    }

    /** In dependency on ID's value, either creates new record or updates existing record.
     * If operation successes, ID of record is returned.
     * If id id null or zero value, creates new record.
     * This method call it's methods "create" or "update" (it DOES NOT call DAO operations directly), 
     * if subclass extends those methods, subclasses methods are called.
     * @param entity the entity to be saved
     */
	@Override
    public Object save(K entity) {
		boolean newEntity = entity.isNew();
		log.debug("Saving entity {} with ID {}", entity, entity.getId());
		fillStandardEntity(entity, newEntity);
		Object reto = getDao().save(entity);
		checkAudit(entity, newEntity ? AuditServiceI.Action.INSERT : AuditServiceI.Action.UPDATE);
		checkSaveState(entity);
		return reto;
    }

	private void checkSaveState(K entity) {
		if(entity instanceof InitialStateI) {
			((InitialStateI)entity).saveInitialState();
		}
	}

	public void saveAll(Collection<K> list) {
    	for (K k : list) {
			save(k);
		}
	}

	private void fillStandardEntity(K entity, boolean newEntity) {
		if(entity instanceof StandardEntityI) {
			Date date = new Date();
			CurrentStateI state = getCurrentState();
			Object user = state == null ? null : state.getCurrentUser();
			StandardEntityI stde = (StandardEntityI)entity;
			log.trace("Entity {} is instance of 'StandardEntityI', filling update date=current / user='{}'", entity, user);
			if(newEntity) {
				log.trace("Entity is new object, '{}', filling create date=current / user='{}'", entity, user);
				if(stde.getCreateDate() == null) { stde.setCreateDate(date); }
				if(stde.getCreateUser() == null && user != null) {
					stde.setCreateUser(user.toString()); 
				}
			}
			stde.setUpdateDate(date);
			if(user != null) {
				stde.setUpdateUser(user.toString());
			}
		}
	}

	private void checkAudit(K entity, Action action) {
		if(isAuditable(entity)) {
			log.debug("Entity {} is auditable, performing audit.", entity.getClass());
			if(!(entity instanceof InitialStateI)) {
				log.warn("Entity annotated as Auditable must be instance of InitialStateI");
				return;
			}
			AdonsI adons;
			if((adons = getDao().getDaoSource().getAdons()) != null) {
				AuditServiceI aus;
				if((aus = adons.getAuditService()) != null) {
					if(action == Action.DELETE) {
						aus.auditEntity(action, entity, this);
					} else if(action == Action.INSERT) {
						aus.auditEntity(action, entity, this);
					} else {
						aus.auditEntity(action, entity, this);
					}
				}
			} else {
				log.debug("DaoSource '{}' does not have the addons set. Audit skipped.", getDao().getDaoSource());
			}
		}
	}

	/** 
	 * Checks whether entity is annotated by Auditable annotation.
	 * There is not direct reference to the "fishear-data-audit" module (all references are done by reflection).
	 * So in case those classes are not in classpath, all objects are generally NOT aufitable without rexception.
	 * 
	 * @param entity the entity
	 * @return true if entity is annottated by Auditable annotation.
	 */
	public static boolean isAuditable(Object entity) {
		if(auditable == null) {
			return false;
		} else {
			return entity.getClass().getAnnotation(auditable) != null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Q> List<Q> query(QueryConstraints constraints) {
		log.debug("Querying data for QueryConstraints={}", constraints);
    	GenericDaoI<K> dao = getDao();
    	List<Q> result = (List<Q>) dao.query(constraints == null ? QueryFactory.create() : constraints);
        return result;
    }

	@Override
    public long queryCount(QueryConstraints constraints) {
		log.debug("Querying data count for QueryConstraints={}", constraints);
    	QueryConstraints qc = QueryFactory.copyOrCreate(constraints);
		qc.results().addRowCount();
		List<Object> list = query(qc);
		return list.size() == 0 ? 0L : ((Number)list.get(0)).intValue();
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
    	List<K> list = query(constraints == null ? QueryFactory.create() : constraints);
    	List<K> list2 = modifyList(list == null ? new ArrayList<K>() : list, constraints);
    	return list2;
    }

    private List<K> modifyList(List<K> list, QueryConstraints qc) {
    	List<String> lazy = getEagerProps(qc);
    	for(K entity : list) {
    		loadEager(entity, lazy);
    		modifyEntity(entity);
    	}
		return list;
	}

	private List<String> getEagerProps(QueryConstraints qc) {

		if(this.eagerProps == null) {
			synchronized(this) {
				if(this.eagerProps == null) {
					List<String> eagerProps;
					if(qc == null || (eagerProps = qc.getEagerLoad()) == null) {
						eagerProps = getEagerAnnotated();
						if(eagerProps == null && getEntityType().getAnnotation(EagerLoads.class) != null) {
							eagerProps = Arrays.asList(Texts.removeEmpty(getEntityType().getAnnotation(EagerLoads.class).value()));
						}
					}
					if(eagerProps == null) {
						eagerProps = Collections.emptyList();
					}
					this.eagerProps = eagerProps;
				}
			}
		}
		return this.eagerProps;
	}

	/**
	 * @return list of property names that are annotated by {@link Eager} annotation => should be loaded eagerly.
	 */
	private List<String> getEagerAnnotated() {
		Method[] ma = getEntityType().getMethods();

		List<String> list = null;

		for(Method m : ma) {
			if(m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
				if(m.getAnnotation(Eager.class) != null) {
					if(list == null) {
						list = new ArrayList<String>();
					}
					list.add(EntityUtils.toFieldName(m.getName()));
				}
			}
		}
		return list;
	}

	/**
	 * @param entity
	 * @param qc
	 * @param eagerProps 
	 * @return
	 */
	private K modifyEntity(K entity) {
		if(entity != null) {
			if(entity instanceof InitialStateI && isAuditable(entity) ) {
				log.trace("Initial state of entity {} stored");
				((InitialStateI)entity).saveInitialState();
			}
		}
		return entity;
 	}

	private void loadEager(K entity, List<String> eagerProps) {
		log.trace("Loading eagerliy: entity {}, eagerProps: {}", entity, eagerProps);
		if(entity != null) {
			// loads lazily loaded properties 
			try {
				if(eagerProps != null) {
					for(String propName : eagerProps) {
						log.trace("Loading lazy property {}", propName);
						if(propName != null) {
							getDao().loadLazy(entity, propName);
						}
					}
				}
			} catch(Exception ex) {
				throw new IllegalStateException(ex);
			}
		}
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
    	K nent = read_(entity.getId());
    	if(nent == null) {
    		return entity;
    	}
		loadEager(entity, getEagerProps(null));
    	EntityUtils.fillDestination(entity, nent, FillFlags.OVERWRITE_BY_NULLS, FillFlags.DO_NOT_CHECK_CLASSES, FillFlags.IGNORE_COLLECTIONS);
    	return modifyEntity(nent);
    }

	@Override
    public K read(QueryConstraints qc) throws TooManyRecordsException {
		log.trace("Getting single data row with given QueryConstraints");
		List<K> list = list(qc);

		if(list.size() == 0) {
			return null;
		}
		if(list.size()  > 1) {
			throw new TooManyRecordsException("only-one-record-expected-but-more-found", list.size(), getDao().type(), qc.toString());
		}
		
		K e = list.get(0);
		
		loadEager(e, getEagerProps(qc));
		return modifyEntity(e);
    }

	@Override
	public List<K> listExisting(K entity, String... propertyNames) {
		if(log.isTraceEnabled()) {
			log.trace("Checking whether any data exist for entity {}, properties {}", entity, Arrays.asList(propertyNames));
		}
		try {
			QueryConstraints qc = QueryFactory.createDefault();
			if(!entity.isNew()) {
				qc.add(Restrictions.notEqual("id", entity.getId()));
			}
			for (int i = 0; i < propertyNames.length; i++) {
				Object val = BeanUtilsBean.getInstance().getPropertyUtils().getProperty(entity, propertyNames[i]);
				qc.add(Restrictions.equal(propertyNames[i], val));
			}
			return list(qc);
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	@Override
	public boolean existsEntity(K entity, String... propertyNames) {
		return listExisting(entity, propertyNames).size() > 0;
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
		if(entity.isNew()) {
			return entity;
		} else {
			K e1 = read_(entity.getId());
			loadEager(entity, getEagerProps(null));
			EntityUtils.fillDestination(entity, e1);
			return modifyEntity(e1);
		}
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
		DaoSourceI ds;
		CurrentStateSourceI css;
		if((ds = getDao().getDaoSource()) != null && (css = ds.getCurrentStateSource()) != null) {
			return css.getCurrentState();
		} else {
			return null;
		}
	}

	public K refresh(K entity) {
		K e = getDao().refresh(entity);
		loadEager(e, getEagerProps(null));
		return modifyEntity(e);
	}
}
