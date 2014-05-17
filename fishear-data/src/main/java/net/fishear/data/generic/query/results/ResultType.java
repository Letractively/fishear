package net.fishear.data.generic.query.results;

/**
 * type of result transformation. by default. result of query is list of entities of given type for normal query, and list of maps for projections (functions ...).
 * It is possible to set type of result different of this default.
 * 
 * @author ffyxrr
 *
 */
public enum ResultType {

	/**
	 * default return type - list of entities. 
	 */
	ENTITY,
	
	/**
	 * list of maps, each map contains entities from result, key is the alias of entity, value is entity itself. 
	 */
	MAP,
	
	/**
	 * appliable only for projections. 
	 */
	PROJECTION,
}
