package net.fishear.data.generic.services;

import java.util.Map;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.services.ServiceI;

public class ListDataHolder<Q extends EntityI<R>, R> {

	private String[] listColumns;
	
	private QueryConstraints queryConstraints;
	
	private ServiceI<Q> service;

	public ListDataHolder(ServiceI<Q> service, String... listColumns) {
		this.service = service;
		this.listColumns = listColumns;
	}

	public ListDataHolder(ServiceI<Q> service, QueryConstraints queryConstraints, String... listColumns) {
		this.service = service;
		this.listColumns = listColumns;
		this.queryConstraints = queryConstraints;
	}

	/**
	 * @param queryConstraints
	 * @return map created using given query constraints
	 */
	@SuppressWarnings("unchecked")
	public Map<Long, String> getIdValueMap(QueryConstraints queryConstraints) {
		return (Map<Long, String>) service.getIdValueMap(queryConstraints, listColumns);
	}

	/**
	 * @return the map created using set (internal) query constraints.
	 * @see #setQueryConstraints(QueryConstraints)
	 */
	public Map<Long, String> getIdValueMap() {
		return getIdValueMap(queryConstraints);
	}

	public Q getSelectedObject(Long key) {
		if(key == null) {
			return null;
		}
		return service.read(key);
	}

	/**
	 * @return the service
	 */
	public ServiceI<Q> getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ServiceI<Q> service) {
		this.service = service;
	}

	public Long setSelectedObject(Q entity) {
		Long key;
		if(entity == null) {
			key = null;
		} else {
			key = (Long) entity.getId();
		}
		return key;
	}

	/**
	 * @return the queryConstraints
	 */
	public QueryConstraints getQueryConstraints() {
		return queryConstraints;
	}

	/**
	 * @param queryConstraints the queryConstraints to set
	 */
	public void setQueryConstraints(QueryConstraints queryConstraints) {
		this.queryConstraints = queryConstraints;
	}
}
