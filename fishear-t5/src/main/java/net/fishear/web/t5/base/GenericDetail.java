package net.fishear.web.t5.base;

import java.lang.reflect.ParameterizedType;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.exceptions.AppException;
import net.fishear.exceptions.BreakException;
import net.fishear.exceptions.ValidationException;
import net.fishear.utils.Classes;
import net.fishear.utils.Exceptions;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.runtime.ComponentEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class
	GenericDetail<T extends EntityI<?>> 
extends 
	ComponentBase
{

	Logger log = LoggerFactory.getLogger(getClass());

	private Class<T> entityType;

	@Persist
	private T entity;
	
	/**
	 * @return the service that manages entities for this 
	 */
	public abstract ServiceI<T> getService();

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

	/**
	 * @return an entity instance; depending on persistent state it may erturn new instance or existing one.
	 * Never returns null.
	 */
	@Cached
	public T getEntity() {
		try {
			if(entity == null) {
				log.trace("getEntity(): Entity variable is null => creating new entity instance");
				T e = getService().newEntityInstance();
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
				entity = getService().syncRead(entity);
				if(entity == null) {
					log.warn("Entity with id {} not found", entity.getId());
					entity = getService().newEntityInstance();
					newEntityInstance(entity);
				}
			}
		} catch(Exception ex) {
			alerts.error(String.format("Cannot load entity, cause: %s. See server log for more details.", ex.toString()));
			ex.printStackTrace();
			log.error("Exception during entity gettting", ex);
			if(entity == null) {
				log.warn("entity is null after exception, creating new entity instance");
				entity = getService().newEntityInstance();
			}
		}
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	//	@CommitAfter
	public Object onSuccess() {
		log.debug("onSuccess() called");
		try {
			ServiceI<T> service = getService();
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
				getService().getDao().rollback();
			}
		}
		return getReturn();
	}

	public Object onAddNew() {
		log.debug("onAddNew() called");
		entity = null;
		return getReturn();
	}

	protected Object onDetail(Object id) {
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

	protected Object onDelete(Object id) {
		log.debug("onDelete({}) called", id);
		beforeDelete(id);
		try {
			if(getService().delete(id)) {
				getService().getDao().commit();
				alerts.success(translate("record-has-been-deleted-message"));
			} else {
				alerts.error(translate("cannot-delete-record-message"));
			}
		} catch(BreakException ex) {
			if(log.isDebugEnabled()) {
				log.debug("Deleting is interrupted by {}, rollback: {}", ex.toString(), ex.isRollback());
			}
			if(ex.isRollback()) {
				getService().getDao().rollback();
			}
		} catch(Exception ex) {
			alerts.error(translate("error-while-deleting-record-message", ex.toString()));
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
			entity = getService().getDao().refresh(getEntity());
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
			entity = getService().read(entityId);
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

	public Object onException(Throwable causingEx) {
		Throwable cause;
		if(causingEx instanceof ComponentEventException) {
			cause = Exceptions.getRootCause(((ComponentEventException)causingEx).getCause());
		} else {
			cause = Exceptions.getRootCause(causingEx);
		}
		String message;
		
		// BreakException indicates that processing was stopped but it is not error
		if(!(cause instanceof BreakException)) {
			if(cause instanceof ValidationException) {
				ValidationException vex = (ValidationException) cause;
				String msg = vex.getMessage();
				if(msg != null) {
					if(msg.regionMatches(0,	"localized:", 0, 10)) {
						message = msg.substring(10);
					} else {
						message = translate(msg, vex.getParams() == null ? new Object[0] : vex.getParams());
					}
				} else {
					String causeMsg = "(unknown)";
cont1:
					try {
						StackTraceElement[] stt = cause.getStackTrace();
						for(StackTraceElement st : stt) {
							Class<?> cl = getClass().getClassLoader().loadClass(st.getClassName());
							if(EntityI.class.isAssignableFrom(cl)) {
								causeMsg = Classes.getShortClassName(cl);
								break cont1;
							}
						}
						causeMsg = Classes.getShortClassName(getClass().getClassLoader().loadClass(stt[1].getClassName()));
					} catch(Exception ex) {}
					message = translate("validation-failed-at", causeMsg);
				}
			} else {
				message = translate("application-error-occurred", cause.toString());
				log.error("Applicatioon error occurred", causingEx);
				if(message.startsWith("[[missing key:")) {
					message = message + " :: " + cause.toString();
				}
			}
			alerts.error(message);
		}
		return getReturn();
	}
}
