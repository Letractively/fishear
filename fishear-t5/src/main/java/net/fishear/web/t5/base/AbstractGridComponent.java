package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
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
	AbstractGridComponent<T extends EntityI<Long>> 
extends 
	GenericGridComponent<T>
implements 
	SearchableI<T>
{
	
	public T getRow() {
		return row;
	}

	public void setRow(T row) {
		this.row = row;
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
}
