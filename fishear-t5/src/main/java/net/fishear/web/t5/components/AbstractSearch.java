package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.query.utils.SearchUtils;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.web.t5.base.AbstractFragment;
import net.fishear.web.t5.base.SearchFormI;
import net.fishear.web.t5.base.SearchableI;

import org.apache.tapestry5.annotations.PageAttached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.runtime.Component;


public abstract class 
	AbstractSearch<T extends EntityI<?>>
extends
	AbstractFragment
implements
	SearchFormI<T>
{
	
	private ServiceI<T> thisService;

	@Persist
	private T entity;
	
//	public abstract ServiceI<T> getService();
	
	private SearchableI<T> searchable;
	
	@PageAttached
	public void pageLoaded() {
		thisService = getService();
		searchForGrid();
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
		throw new IllegalStateException(String.format("The implementation of AbstractSearch component has to be placed into implementation of %s", SearchableI.class.getName()));
	}

	public T getEntity() {
		if(entity == null) {
			entity = thisService.newEntityInstance();
		}
		return entity;
	}

	public void onSuccessFromSearch() {

		// TODO: return the grid for ajax refresh
	}
	
	public void onActionFromSrchclr() {
		clearSearchForm();
	}

	@Override
	public void clearSearchForm() {
		entity = null;
	}

	@Override
	public Conditions getSearchConstraints() {
		return SearchUtils.createSearchConditions(entity);
	}

	@Override
	public boolean isSearchConstraintsSet() {
		return false;
	}
}