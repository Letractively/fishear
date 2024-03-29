package net.fishear.web.t5.internal;

import net.fishear.Interfaces.EntityTypeI;
import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.query.conditions.Conditions;
import net.fishear.data.generic.services.ServiceSourceI;

public interface 
	SearchFormI<T extends EntityI<?>>
extends
	ServiceSourceI<T>,
	EntityTypeI
{

	/** returns condition to satisfy data filled out to search form, or null if no search is required.
	 */
	Conditions getSearchConstraints();
	
	/** clear all fields of search form and set itz to initial state.
	 */
	void clearSearchForm();
	
	/** returns true if any of search constraint is set (e.g. field filled out by user).
	 * This is used for modify messages (in case no data found etc...)
	 */
	boolean isSearchConstraintsSet();
	
	/**
	 * @return whole {@link QueryConstraints} instance created using {@link QueryFactory#create()}
	 */
	QueryConstraints getQueryConstraints();

	/** 
	 * finds type of entity that this master-detail  component is constructed for.
	 * 
	 * @return entity type
	 */
	Class<T> getEntityType();
}
