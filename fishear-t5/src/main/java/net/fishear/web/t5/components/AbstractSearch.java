package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.utils.SearchUtils;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.web.t5.base.ComponentBase;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

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
	
	private static Logger log = LoggerFactory.getLogger(AbstractSearch.class);
	
	private ServiceI<T> thisService;

	@Persist
	private T entity;

	@Persist
	private T entity2;

	private SearchableI<T> searchable;
	
	@Persist
	private Conditions extraConditions;

	@SetupRender
	public void pageLoaded() {
		thisService = getService();
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

	public T getEntity() {
		if(entity == null) {
			entity = thisService.newEntityInstance();
			newEntityInstance(entity);
		}
		return entity;
	}

	public T getEntity2() {
		if(entity2 == null) {
			entity2 = thisService.newEntityInstance();
			newEntityInstance(entity2);
		}
		return entity2;
	}

	/** called after new entity is created to allow successor to set needed values.
	 * @param entity
	 */
	protected void newEntityInstance(T entity) {
		newEntityInstance(entity, EntityType.ENTITY);
	}

	protected void newEntityInstance(T entity, EntityType type) {
		
	}

	public Object onSuccess() {
		return Boolean.TRUE;
	}
	
	public Object onClearSearch() {
		clearSearchForm();
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
}