package net.fishear.web.t5.base;

import java.lang.reflect.ParameterizedType;

import net.fishear.Interfaces.EntityTypeI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.data.generic.services.ServiceSourceI;
import net.fishear.exceptions.AppException;
import net.fishear.exceptions.BreakException;
import net.fishear.utils.Classes;
import net.fishear.web.t5.data.PagingDataSource;
import net.fishear.web.t5.internal.GridSourceI;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

import org.apache.tapestry5.annotations.Cached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class
	GenericGridComponent<T extends EntityI<?>> 
extends 
	ComponentBaseHandled
implements
	SearchableI<T>,
	EntityTypeI,
	ServiceSourceI<T>,
	GridSourceI<T>
{

	Logger log = LoggerFactory.getLogger(getClass());

	private Class<T> entityType;

	protected T row;

	private SearchFormI<T> searchComponent;

	/**
	 * @return the service that manages entities for this 
	 */
	
	public abstract ServiceI<T> getService();
	
	@Cached
	private ServiceI<T> service() {
		ServiceI<T> svc = getService();
		if(svc == null) {
			throw new IllegalStateException(String.format("method 'getService' returned null. This method must return service for entity type '%s'.", getEntityType().getName()));
		}
		return svc;
	}

	/**
	 * method suit for modifying conditions. 
	 * 
	 * @param cond conditions either generated by search component (in case it exists in from), or empry conditions.
	 */
	protected void modifyConditions(Conditions cond) {
		
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
		try {
			T entity = getService().read(id);
			if(entity == null) {
				alerts.error(translate("record-does-not-exist-message"));
				return false;
			}

			beforeDelete(entity);
			
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
		} catch(Exception ex) {
			alerts.error(translate("error-while-deleting-record-message", ex.toString()));
		}
		return getReturn();
	}

	/** called before record is deleted. 
	 * May throw {@link BreakException}, that causes update process breaking. If exception's 'rollback' flag is set, database rollback is performed; otherwise database commit status stay unchanged.
	 * 
	 * @param id
	 */
	protected void beforeDelete(T entity) {

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
	 * @return data source
	 */
	public PagingDataSource getDataSource() {
		PagingDataSource pds = new PagingDataSource(service());
		if(searchComponent != null) {
			Conditions cond = searchComponent.getSearchConstraints();
			Conditions cond2 = cond == null ? new Conditions() : cond;
			modifyConditions(cond2);
			pds.setConditions(cond2.isEmpty() ? cond : cond2);
		} else {
			Conditions cond = new Conditions();
			modifyConditions(cond);
			if(!cond.isEmpty()) {
				pds.setConditions(cond);
			}
		}
		modifyConstraints(pds.getQueryConstraint());
		return pds;
	}

	/**
	 * <strong>USE WITH CARE</strong> - designed to allow change complete query constraints before it is used for data get.
	 * Be aware that change of some part of it may cause application is broken. 
	 * 
	 * @param queryConstraint
	 */
	protected void modifyConstraints(QueryConstraints queryConstraint) {

	}

	@Override
	public void setSearchComponent(SearchFormI<T> searchComponent) {
		if(!searchComponent.getEntityType().isAssignableFrom(getEntityType())) {
			throw new IllegalStateException(String.format(
				"Both '%s' and '%s' (included inti it) must implement the same entity, but they does not. " +
				"'%s' implements '%s' and '%s' implements '%s'", 
					Classes.getShortClassName(this), Classes.getShortClassName(SearchFormI.class),
					getClass().getName(), getEntityType().getName(),
					searchComponent.getClass().getName(), searchComponent.getEntityType().getName()
			));
		}
		this.searchComponent = searchComponent;
	}

	@Override
	public SearchFormI<T> getSearchComponent() {
		return searchComponent;
	}

	/**
	 * @return the row
	 */
	public T getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(T row) {
		this.row = row;
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
