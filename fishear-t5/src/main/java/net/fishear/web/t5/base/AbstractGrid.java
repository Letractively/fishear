package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.EntitySourceI;
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
	AbstractGrid<T extends EntityI<Long>> 
extends 
	GenericGrid<T>
implements 
	SearchableI<T>
{
	
	public T getRow() {
		return row;
	}

	public void setRow(T row) {
		this.row = row;
	}
}
