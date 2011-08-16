package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;

public interface SearchableI<T extends EntityI<?>>
{

	void setSearchComponent(SearchFormI<T> searchComponent);
	
	SearchFormI<T> getSearchComponent();

}
