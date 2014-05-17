package net.fishear.web.t5.internal;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.web.t5.base.GenericGridDetailComponent;

public interface SearchableI<T extends EntityI<?>>
{

	void setSearchComponent(SearchFormI<T> searchComponent);
	
	SearchFormI<T> getSearchComponent();
	
	/**
	 * Gets the type of entity that this searc component is suit for.
	 * It must fit type of entity of {@link GenericGridDetailComponent} if this search is used inside it.
	 * 
	 * @return type of entity
	 */
	Class<T> getEntityType();

}
