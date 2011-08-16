package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.conditions.Conditions;

public interface 
	SearchFormI<T extends EntityI<?>>
extends
	ServiceSourceI<T>
{

	/** returns query constraints to satisfy data filled out to search form, or null if no search is required.
	 */
	Conditions getSearchConstraints();
	
	/** clear all fields of search form and set itz to initial state.
	 */
	void clearSearchForm();
	
	/** returns true if any of search constraint is set (e.g. field filled out by user).
	 * This is used for modify messages (in case no data found etc...)
	 */
	boolean isSearchConstraintsSet();
	
}
