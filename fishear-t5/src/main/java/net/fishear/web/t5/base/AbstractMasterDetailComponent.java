package net.fishear.web.t5.base;


import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.web.t5.annotations.ForZone;
import net.fishear.web.t5.data.PagingDataSource;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ForZone("TransportTimeZone")
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
	 * 
	 * @param entity the entity. Either new instance (call {@link EntityI#isNew()} or existing one read from DB.
	 */
	protected abstract void beforeSave(T entity);

	/**
	 * called after entity is successfully saved. 
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
	 * 
	 * @param id
	 */
	private void beforeDelete(Long id) {

	}


	/**
	 * @return an entity instance; depending on persistent state it may erturn new instance or existing one.
	 * Never returns null.
	 */
	public T getEntity() {
		if(entity == null) {
			entity = getService().newEntityInstance();
			newEntityInstance(entity);
		} else if(entity.getId() != null) {
			entity = getService().syncRead(entity);
			if(entity == null) {
				log.warn("Entity with id {} not found", entity.getId());
				entity = getService().newEntityInstance();
				newEntityInstance(entity);
			}
		}
		return entity;
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
		try {
			T entity = getEntity();
			beforeSave(entity);
			getService().save(entity);
			afterSave(entity);
			getService().getDao().commit();
			alerts.info(translate("record-has-been-saved-message"));
		} catch(Exception ex) {
			log.error(String.format("Error while saving entity %s", entity), ex);
			alerts.error(translate("error-while-saving-entity-message", ex.toString()));
		}
		return getReturn();
	}

	public Object onAddNew() {
		entity = null;
		return getReturn();
	}

	protected Object onDetail(Long id) {
		entity = id == null ? null : getService().read(id);
		return getReturn();
	}

//	@CommitAfter
	protected Object onDelete(Long id) {
		beforeDelete(id);
		try {
			if(getService().delete(id)) {
				getService().getDao().commit();
				alerts.info(translate("record-has-been-deleted-message"));
			} else {
				alerts.error(translate("cannot-delete-record-message"));
			}
		} catch(Exception ex) {
			alerts.error(translate("error-while-deleting-record-message", ex.toString()));
		}
		return getReturn();
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
