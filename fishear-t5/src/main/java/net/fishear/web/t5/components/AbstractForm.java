package net.fishear.web.t5.components;


import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.exceptions.ValidationException;
import net.fishear.web.t5.base.AbstractComponent;
import net.fishear.web.t5.base.ServiceSourceI;

import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.runtime.Component;


public abstract class 
	AbstractForm<T extends EntityI<?>>
extends
	AbstractComponent
implements 
	ServiceSourceI<T>,
	DetailFormI<T>
{

	private T entity;
	
	private ServiceI<T> service;

//	public abstract GenericService<T> getService();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PageAttached
	public void pageAttached() {
		service = getService();
		if(service == null) {
			throw new IllegalStateException(String.format("The implementation of method '%s' returned null", "getService()"));
		}
		Component ct = getResources().getContainer();
		while(ct != null) {
			if(ct instanceof FormContainerI) {
				((FormContainerI)ct).setDetailForm(this);
				break;
			}
			ct = ct.getComponentResources().getContainer();
		}
		if(ct == null) {
			throw new IllegalStateException(String.format("The implementation of AbstractForm component has to be placed into implementation of %s", FormContainerI.class.getName()));
		}
	}
	
	
//	
//	public void setPtype(T ptype) {
//		this.entity = ptype;
//	}

	public void load(Object id) {
		entity = service.read(id);
	}

	public T getEntity() {
		if(entity == null) {
			entity = newEntityInstance();
		}
		return entity;
	}

	/** called out when new entity instance is created. 
	 * Descendant can fill new entity with proper values.
	 * @param entity
	 */
	protected T newEntityInstance() {
		T e = service.newEntityInstance();
		prepareNewEntity(e);
		return e;
	}

	protected void prepareNewEntity(T e) {
		
	}

	public Object onActionFromAdd() {
		entity = null;
		return this;
	}
	
//	@CommitAfter
	public Object onSuccess() {
		try {
			service.getDao().transaction();
			this.entity = service.fillNewEntity(getEntity()); // to prevent hibernate from cache entity
			boolean isNew = entity.isNew();
			beforeSave(this.entity);
			service.validate(this.entity);
			service.save(this.entity);
			service.getDao().commit();
			if(isNew) {
				setMessageText(getMessage("record-successfully-added-label"));
			} else {
				setMessageText(getMessage("record-successfully-saved-label"));
			}
		} catch (ValidationException ex) {
			setErrorText(getMessage(ex.getMessage()));
		} catch (Throwable ex) {
			try {
				service.getDao().rollback();
			} catch(Exception ex2) {
				// do nothing ?
			}
			setErrorText(ex.toString());
		}
		return this;
	}

	protected void beforeSave(T entity) {

	}
}
