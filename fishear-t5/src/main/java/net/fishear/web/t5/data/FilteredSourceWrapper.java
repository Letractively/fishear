package net.fishear.web.t5.data;

import java.util.List;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.query.QueryConstraints;
import net.fishear.data.generic.query.QueryFactory;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.utils.ListFilter;
import net.fishear.utils.Lists;


/**
 * similar to {@link DefaultSourceWrapper}, but, in addition, perform data
 * filtering using given filter.
 * 
 * @author terber
 */
public class 
	FilteredSourceWrapper<K extends EntityI<?>>
extends
	DefaultSourceWrapper
{

	private QueryConstraints constraints;
	
	private final ListFilter<K> filter;
	
	private List<K> list;

	public FilteredSourceWrapper(ServiceI<K> listService, ListFilter<K> filter) {
		super(listService);
		this.filter = filter;
	}

	@SuppressWarnings("unchecked")
	private List<K> getList(QueryConstraints constraints) {
		if(this.list == null || !QueryFactory.equalsWhere(this.constraints, constraints)) {
			this.constraints = constraints;
			this.list = (List<K>) super.getItems(constraints);
		}
		return this.list;
	}

	public List<K> getItems(QueryConstraints constraints) {
		return Lists.sublist(getList(constraints), filter);
	}

	public long getItemsCount(QueryConstraints constraints) {
		List<K> list = getList(constraints);
		if (list == null) {
			return 0;
		}
		return list.size();
	}
}
