package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.EntitySourceI;
import net.fishear.web.t5.data.PagingDataSource;

/**
 * Component that constructs {@link PagingDataSource} for given service, suit for a grid.
 * Also contains 
 * 
 * @author ffyxrr
 *
 * @param <T>
 */
public abstract class
	AbstractDetailComponent<T extends EntityI<Long>> 
extends 
	GenericDetailComponent<T>
implements 
	EntitySourceI<T>
{

	public T getEntity() {
		return super.getEntity();
	}

	public Object onDelete(Long id) {
		return super.onDelete(id);
	}

	public Object onDelete(Object id) {
		try {
			return super.onDelete(Long.parseLong(id.toString()));
		} catch(Exception ex) {
			log.error("Exception during 'onDelete' method call", ex);
			return getReturn();
		}
	}

	public Object onDetail(Long id) {
		return super.onDetail(id);
	}

	public Object onDetail(Object id) {
		try {
			return super.onDetail(Long.parseLong(id.toString()));
		} catch(Exception ex) {
			log.error("Exception during 'onDetail' method call", ex);
			return getReturn();
		}
	}
}
