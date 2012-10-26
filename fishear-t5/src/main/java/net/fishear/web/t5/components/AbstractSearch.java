package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.utils.SearchUtils;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.web.t5.base.AbstractFragment;
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
	AbstractFragment
implements
	SearchFormI<T>
{
	
	private static Logger log = LoggerFactory.getLogger(AbstractSearch.class);
	
	private ServiceI<T> thisService;

	@Persist
	private T entity;

	private SearchableI<T> searchable;
	
	private Conditions conditions;
	
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

	/** called before constraints are returned.
	 * @param cond
	 */
	private void modifyConstraints(Conditions cond) {

	}

	@SuppressWarnings("unchecked")
	private void searchForGrid() {
		Component ct = getResources().getContainer();
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
		}
		return entity;
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
		beforeSearch(entity);
		Conditions cond = SearchUtils.createSearchConditions(entity);
		Conditions conditions = getConditions();
		if(conditions != null) {
			if(cond == null) {
				cond = conditions;
			} else {
				cond.add(conditions.getRootRestriction());
			}
		}
		modifyConstraints(cond);
		return cond;
	}

	@Override
	public boolean isSearchConstraintsSet() {
		return false;
	}

	/**
	 * @return the conditions
	 */
	public Conditions getConditions() {
		return conditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}
}