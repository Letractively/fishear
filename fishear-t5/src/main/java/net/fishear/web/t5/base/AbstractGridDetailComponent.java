package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.EntitySourceI;
import net.fishear.utils.Numbers;
import net.fishear.web.t5.data.PagingDataSource;
import net.fishear.web.t5.internal.SearchableI;

/**
 * Component that constructs {@link PagingDataSource} for given service, suit for a grid.
 * Also contains 
 * 
 * @author ffyxrr
 *
 * @param <T>
 */
public abstract class
	AbstractGridDetailComponent<T extends EntityI<Long>> 
extends 
	GenericGridDetailComponent<T>
implements 
	SearchableI<T>,
	EntitySourceI<T>
{
	
	public T getRow() {
		return row;
	}

	public void setRow(T row) {
		this.row = row;
	}

	public T getEntity() {
		return super.getEntity();
	}

	public Object onDelete(Long id) {
		return super.onDelete(id);
	}

	public Object onDelete(Object id) {
		try {
			return onDelete(Numbers.tol(id, null));
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
			// preserve "internall" call of onDetail(Long) due possibility of overriding
			return onDetail(Numbers.tol(id, null));
		} catch(Exception ex) {
			log.error("Exception during 'onDetail' method call", ex);
			return getFormReturn();
		}
	}
}
