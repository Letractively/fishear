package net.fishear.web.t5.base;

import java.lang.reflect.ParameterizedType;

import net.fishear.Interfaces.EntityTypeI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.EntitySourceI;
import net.fishear.data.generic.services.ServiceHolder;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.data.generic.services.ServiceSourceI;
import net.fishear.exceptions.AppException;
import net.fishear.exceptions.BreakException;
import net.fishear.exceptions.ValidationException;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class
	GenericDetailComponent<T extends EntityI<?>> 
extends 
	ComponentBaseHandled
implements
	EntityTypeI,
	ServiceSourceI<T>,
	EntitySourceI<T>
{

	Logger log = LoggerFactory.getLogger(getClass());

	private Class<T> entityType;

	@Persist
	private T entity;
	
	/**
	 * @return the service that manages entities for this 
	 */
	@Override
	public ServiceI<T> getService() {
		@SuppressWarnings("unchecked")
		ServiceI<T> svc = (ServiceI<T>) ServiceHolder.getInstance().findServiceForEntity(getEntityType());
		if(svc == null) {
System.err.println(ServiceHolder.getInstance().listRegisteredServices());
			log.error("Method 'getService()' is not overriden, but service for entity {} seems to be unregisteres. Override the 'getService()' method to provide correct service.", getEntityType().getName());
		}
		return svc;
	}

	@Cached
	private ServiceI<T> service() {
		ServiceI<T> svc = getService();
		if(svc == null) {
			throw new IllegalStateException(String.format("method 'getService' returned null. This method must return service for entity type '%s'.", getEntityType().getName()));
		}
		return svc;
	}

	/**
	 * called in case form is submitted.
	 * Prepares entity to be saved to database.
	 * May throw {@link BreakException}, that causes update process breaking. If exception's 'rollback' flag is set, database rollback is performed; otherwise database commit status stay unchanged.
	 * 
	 * @param entity the entity. Either new instance (call {@link EntityI#isNew()} or existing one read from DB.
	 */
	protected abstract void beforeSave(T entity);

	/**
	 * called after entity is successfully saved. 
	 * May throw {@link BreakException}, that causes update process breaking. If exception's 'rollback' flag is set, database rollback is performed; otherwise database commit status stay unchanged.
	 * 
	 * @param entity the entity that had been saved. Suppoed it is always persistent, but session is not commited yet.
	 */
	protected void afterSave(T entity) {
		
	}

	/** prepares new entity instance.
	 * 
	 * @param entity
	 */
	protected void newEntityInstance(T entity) {

	}

	/** called before record is deleted. 
	 * May throw {@link BreakException}, that causes update process breaking. If exception's 'rollback' flag is set, database rollback is performed; otherwise database commit status stay unchanged.
	 * 
	 * @param id
	 */
	protected void beforeDelete(Object id) {

	}

	/** called after record is deleted. 
	 * May throw {@link BreakException}, that causes update process breaking. If exception's 'rollback' flag is set, database rollback is performed; otherwise database commit status stay unchanged.
	 * 
	 * @param entity the original entity, which is deleted from persistent storage in time of this call.
	 * @param id
	 */
	protected void afterDelete(T entity) {

	}

	/**
	 * @return an entity instance; depending on persistent state it may erturn new instance or existing one.
	 * Never returns null.
	 */
	@Cached
	public T getEntity() {
		try {
			if(entity == null) {
				log.trace("getEntity(): Entity variable is null => creating new entity instance");
				T e = service().newEntityInstance();
				log.trace("Calling 'newEntityInstance' for entity {}", e);
				newEntityInstance(e);
				try {
					entity = e;
				} catch(Exception ex) {
					log.debug("Cannot set new entity instance persistent", ex);
					return e;
				}
			} else if(entity.getId() != null) {
				log.trace("getEntity(): Reading entity for ID '{}'", entity.getIdString());
				entity = service().syncRead(entity);
				if(entity == null) {
					log.warn("Entity with id {} not found", entity.getId());
					entity = service().newEntityInstance();
					newEntityInstance(entity);
				}
			}
		} catch(Exception ex) {
			alerts.error(String.format("Cannot load entity, cause: %s. See server log for more details.", ex.toString()));
			ex.printStackTrace();
			log.error("Exception during entity gettting", ex);
			if(entity == null) {
				log.warn("entity is null after exception, creating new entity instance");
				entity = service().newEntityInstance();
			}
		}
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	/**
	 * Default form handler designed to quick form handling. 
	 * 
	 * Gets here after form submission and all validations are OK. 
	 * Calls {@link #beforeSave(EntityI)}, then saves record and calls {@link #afterSave(EntityI)}. 
	 * Each of there calls may throw {@link ValidationException} that results to message, {@link BreakException} that silently stops the process, 
	 * or any other exception that results to "internal error" message (the page is rendered normally only with error message).
	 * If succeeded, performs commit and message is displayed to &lt;alerts /&gt; component. On any exception (including {@link BreakException} or {@link ValidationException}) performs rollback and corresponding error message is dislayed.
	 * 
	 * @return value of {@link #getFormReturn()} method.
	 */
	public Object onSuccess() {
		log.debug("onSuccess() called");
		try {
			ServiceI<T> service = service();
			T entity = getEntity();
			
			log.debug("onSuccess procedure for entity {} and service {}", entity, service);
			log.trace("call beforeSave(entity) ");
			beforeSave(entity);
			
			log.trace("call service.validate(entity) for entity {} ", entity);
			service.validate(entity);
			
			log.trace("call service.save(entity) for entity {}", entity);
			service.save(entity);
			
			log.trace("call afterSave(entity) for entity {}", entity);
			afterSave(entity);
			
			log.trace("call service.commit()", entity);
			service.getDao().commit();
			alerts.success(translate("record-has-been-saved-message"));
			
		} catch(BreakException ex) {
			if(log.isDebugEnabled()) {
				log.debug("Saving is interrupted by {}, rollback: {}", ex.toString(), ex.isRollback());
			}
			if(ex.isRollback()) {
				service().getDao().rollback();
			}
		}
		return getFormReturn();
	}

	public Object onAddNew() {
		log.debug("onAddNew() called");
		entity = null;
		return getReturn();
	}

	public Object onDetail(Object id) {
		log.debug("onDetail({}) called", id);
		readEntity(id);
		return getReturn();
	}

	/**
	 * called after entity is read from database.
	 * The entity variable may be null during this call. Call of {@link #getEntity()} method guarantee not null return instead.
	 */
	protected void afterLoad() {
		// does nothing - suit for successors 
	}

	/**
	 * Deletes record from persistent storage. 
	 * 
	 * Calls {@link #beforeDelete(Object)}, then deletes record and calls {@link #afterDelete(EntityI)}. Both may throw {@link BreakException} to silently stop the process and perform rollback.
	 * 
	 * Shows corresponding alert message about result: success, ID does not exists, failure.
	 * In case success, performs commit on all session (incl. previous commands). In case failure, performs rollback of all session. 
	 * 
	 * @param id Object ID to be deleted.
	 * @return true if succeeded, false otherwise.
	 */
	public Object onDelete(Object id) {
		log.debug("onDelete({}) called", id);
		beforeDelete(id);
		try {
			T entity = getService().read(id);
			if(entity == null) {
				alerts.error(translate("record-does-not-exist-message"));
				return false;
			}
			if(service().delete(id)) {
				afterDelete(entity);
				service().getDao().commit();
				alerts.success(translate("record-has-been-deleted-message"));
			} else {
				alerts.error(translate("cannot-delete-record-message"));
			}
		} catch(BreakException ex) {
			if(log.isDebugEnabled()) {
				log.debug("Deleting is interrupted by {}, rollback: {}", ex.toString(), ex.isRollback());
			}
			if(ex.isRollback()) {
				service().getDao().rollback();
			}
//		} catch(Exception ex) {
//			log.error("Cannot delete record", ex);
//			alerts.error(translate("error-while-deleting-record-message", ex.toString()));
		}
		return getReturn();
	}

	/**
	 * In case entity is loaded from persistent storage, refreshes its state (from database).
	 */
	public void refreshEntity() {
		log.debug("refreshEntity() called");
		T e = getEntity();
		if(!e.isNew()) {
			entity = service().getDao().refresh(getEntity());
		}
	}

	/**
	 * reads entity with given ID.
	 * If entityId is null or entity with the ID does not exist, new entity instance is created.
	 * 
	 * @param entityId
	 */
	public void readEntity(Object entityId) {
		if(entityId == null) {
			entity = null;
		} else {
			entity = service().read(entityId);
		}
		afterLoad();
	}

	public Class<T> getEntityType() {
		if(entityType == null) {
			entityType = findType();
		}
		return entityType;
	}

	@SuppressWarnings("unchecked")
	private Class<T> findType() {
		Class<?> clazz = this.getClass();
		while(clazz != Object.class) {
			Object gscl = clazz.getGenericSuperclass();
			if(ParameterizedType.class.isAssignableFrom(gscl.getClass())) {
				ParameterizedType pt = (ParameterizedType)gscl;
				Object[] oa = pt.getActualTypeArguments();
				if(oa != null && oa.length > 0) {
					return (Class<T>)oa[0];
				}
			}
			clazz = clazz.getSuperclass();
		}
		throw new AppException("Subclass does not parametrize generic superclass.");
	}
}
