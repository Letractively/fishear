package net.fishear.web.t5.components;

import java.lang.reflect.ParameterizedType;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.utils.SearchUtils;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.exceptions.AppException;
import net.fishear.utils.Classes;
import net.fishear.utils.Globals;
import net.fishear.web.t5.base.ComponentBase;
import net.fishear.web.t5.base.GenericMasterDetailComponent;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.runtime.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class 
	AbstractSearch<T extends EntityI<?>>
extends
	ComponentBase
implements
	SearchFormI<T>
{

	public enum EntityType {
		ENTITY,
		ENTITY2
	}
	
	private static Logger log = Globals.getLogger();
	
	private Class<T> entityType;

	@Persist
	private T entity;

	@Persist
	private T entity2;

	private SearchableI<T> searchable;
	
	@Persist
	private Conditions extraConditions;

	@Parameter(value="true")
	private boolean clearEntity;

	@SetupRender
	public void pageLoaded() {
		searchForGrid();
	}
	
	/**
	 * called after search form is submitted.Can modify entity values.
	 * @param entity
	 */
	protected void beforeSearch(T entity) {

	}

	protected void beforeSearch(T entity, T entity2) {
		if(entity2 == null) {
			beforeSearch(entity);
		}
	}

	/** called before constraints are returned.
	 * Descendant can modify constraints created by this component.
	 * @param cond
	 */
	protected void modifyConditions(Conditions cond) {

	}

	@SuppressWarnings("unchecked")
	private void searchForGrid() {
		Component ct = super.crsc.getContainer();
		while(ct != null) {
			if(ct instanceof SearchableI) {
				this.searchable = ((SearchableI<T>)ct);
				this.searchable.setSearchComponent(this);
				return;
			}
			ct = ct.getComponentResources().getContainer();
		}
		log.debug("No parent component {} found in parents tree. SSearch Automation is disabled.", SearchableI.class.getName());
//		throw new IllegalStateException(String.format("The implementation of AbstractSearch component has to be placed into implementation of %s", SearchableI.class.getName()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceI<T> getService() {
		if(this.searchable != null && this.searchable instanceof GenericMasterDetailComponent<?>) {
			return (ServiceI<T>) ((GenericMasterDetailComponent<?>)this.searchable).getService();
		} else {
			throw new IllegalStateException(String.format("The component that implements '%s' must be placed inside '%s' or must extend method 'getService()' to return propert service.", Classes.getShortClassName(AbstractSearch.class), Classes.getShortClassName(this)));
		}
	}
	
	@Cached
	public T getEntity() {
		if(entity == null) {
			entity = getService().newEntityInstance();
			newEntityInstance(entity, EntityType.ENTITY);
		}
		return entity;
	}

	@Cached
	public T getEntity2() {
		if(entity2 == null) {
			entity2 = getService().newEntityInstance();
			newEntityInstance(entity2, EntityType.ENTITY2);
		}
		return entity2;
	}

	/** called after new entity is created to allow successor to set needed values.
	 * @param entity
	 */
	public void newEntityInstance(T entity) {

	}

	public void newEntityInstance(T entity, EntityType type) {
		newEntityInstance(entity);
	}

	private void resetEntity() {
		if(clearEntity) {
			log.trace("Flag 'reserEntity' is set = reseting {} entity to null ", Classes.getShortClassName(GenericMasterDetailComponent.class));
			if(this.searchable != null && this.searchable instanceof GenericMasterDetailComponent<?>) {
				((GenericMasterDetailComponent<?>)this.searchable).setEntity(null);
			} else {
				log.debug("Flag 'reserEntity' is set but the component is not inside {}", Classes.getShortClassName(GenericMasterDetailComponent.class));
			}
		} else {

		}
	}

	public Object onSuccess() {
		resetEntity();
		return Boolean.TRUE;
	}
	
	public Object onClearSearch() {
		clearSearchForm();
		resetEntity();
		return Boolean.TRUE;
	}

	@Override
	public void clearSearchForm() {
		entity = null;
	}

	@Override
	public Conditions getSearchConstraints() {
		T entity = getEntity();
		T entity2 = this.entity2 == null ? null : getEntity2();

		log.debug("Creating search condition for entity {} and entity2 {}", entity, entity2);

		beforeSearch(entity, entity2);

		Conditions cond = SearchUtils.createSearchConditions(entity);
		Conditions conditions = getExtraConditions();

		if(conditions != null) {
			if(cond == null) {
				cond = conditions;
			} else {
				cond.add(conditions.getRootRestriction());
			}
		}
		Conditions cond2 = cond == null ? new Conditions() : cond;
		modifyConditions(cond2);
		return cond2.isEmpty() ? cond : cond2;
	}

	@Override
	public boolean isSearchConstraintsSet() {
		return false;
	}

	/**
	 * @return the conditions
	 */
	public Conditions getExtraConditions() {
		return extraConditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setExtraConditions(Conditions conditions) {
		this.extraConditions = conditions;
	}

	public QueryConstraints getQueryConstraints() {
		return QueryFactory.create(getSearchConstraints());
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

	/**
	 * for tests only
	 * @param searchable the searchable to set
	 */
	protected void setSearchable(SearchableI<T> searchable) {
		this.searchable = searchable;
	}

}