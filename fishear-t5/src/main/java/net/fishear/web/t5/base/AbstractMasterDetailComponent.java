package net.fishear.web.t5.base;


import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.exceptions.BreakException;
import net.fishear.web.t5.data.PagingDataSource;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class
	AbstractMasterDetailComponent<T extends EntityI<?>> 
extends 
	ComponentBase
implements 
	SearchableI<T>
{

	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Property
	protected T row;

	@Persist
	protected T entity;
	
	private SearchFormI<T> searchComponent;

	/**
	 * @return the service that manages entities for this 
	 */
	protected abstract ServiceI<T> getService();

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
	private void beforeDelete(Object id) {

	}


	/**
	 * @return an entity instance; depending on persistent state it may erturn new instance or existing one.
	 * Never returns null.
	 */
	@Cached
	public T getEntity() {
		if(entity == null) {
			log.trace("getEntity(): Entity variable is null => creating new entity instance");
			entity = getService().newEntityInstance();
			log.trace("Calling 'newEntityInstance' for entity {}", entity);
			newEntityInstance(entity);
		} else if(entity.getId() != null) {
			log.trace("getEntity(): Reading entity for ID '{}'", entity.getIdString());
			entity = getService().syncRead(entity);
			if(entity == null) {
				log.warn("Entity with id {} not found", entity.getId());
				entity = getService().newEntityInstance();
				newEntityInstance(entity);
			}
		}
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	/**
	 * @return data source
	 */
	public PagingDataSource getDataSource() {
		PagingDataSource pds = new PagingDataSource(getService());
		if(searchComponent != null) {
			pds.setConditions(searchComponent.getSearchConstraints());
		}
		return pds;
	}

//	@CommitAfter
	public Object onSuccess() {
		log.debug("onSuccess() called");
		try {
			T entity = getEntity();
			log.trace("beforeSave(entity) called fofr entity {}", entity);
			beforeSave(entity);
			getService().save(entity);
			log.trace("afterSave(entity) called fofr entity {}", entity);
			afterSave(entity);
			getService().getDao().commit();
			alerts.success(translate("record-has-been-saved-message"));
		} catch(BreakException ex) {
			if(log.isDebugEnabled()) {
				log.debug("Saving is interrupted by {}, rollback: {}", ex.toString(), ex.isRollback());
			}
			if(ex.isRollback()) {
				getService().getDao().rollback();
			}
		} catch(Exception ex) {
			log.error(String.format("Error while saving entity %s", entity), ex);
			alerts.error(translate("error-while-saving-entity-message", ex.toString()));
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
		entity = id == null ? null : getService().read(id);
		return getReturn();
	}

//	@CommitAfter
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

	public void refreshEntity() {
		log.debug("refreshEntity() called");
		T e = getEntity();
		if(!e.isNew()) {
			entity = getService().getDao().refresh(getEntity());
		}
	}

	@Override
	public void setSearchComponent(SearchFormI<T> searchComponent) {
		this.searchComponent = searchComponent;
	}

	@Override
	public SearchFormI<T> getSearchComponent() {
		return searchComponent;
	}
}
