package net.fishear.data.generic.query.results;

/**
 * type of result transformation. by default. result of query is list of entities of given type for normal query, and list of maps for projections (functions ...).
 * It is possible to set type of result different of this default.
 * 
 * @author ffyxrr
 *
 */
public enum JoinType {

	/**
	 * default return type - list of entities. 
	 */
	ENTITY,
	
	/**
	 * list of maps of entities, each with its alias as a key.
	 * Makes sense only on joined queries. 
	 */
	JOIN,
	
}
