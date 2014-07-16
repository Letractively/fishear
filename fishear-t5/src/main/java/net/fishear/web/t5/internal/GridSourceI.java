package net.fishear.web.t5.internal;

import org.apache.tapestry5.grid.GridDataSource;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.web.t5.base.GenericDetailComponent;
import net.fishear.web.t5.base.GenericGridComponent;

/**
 * provides row interface for grid renderer.
 * 
 * Implemented by generic grid components: {@link GenericDetailComponent}, {@link GenericGridComponent}.
 * 
 * @author ffyxrr
 *
 */
public interface GridSourceI<T extends EntityI<?>> {
	
	/**
	 * @return
	 */
	T getRow();
	
	void setRow(T row);
	
	GridDataSource getDataSource();
	
}
