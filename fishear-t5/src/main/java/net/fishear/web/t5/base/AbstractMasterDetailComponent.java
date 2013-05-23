package net.fishear.web.t5.base;


import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;
import net.fishear.exceptions.BreakException;
import net.fishear.web.t5.data.PagingDataSource;
import net.fishear.web.t5.internal.SearchFormI;
import net.fishear.web.t5.internal.SearchableI;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class
	AbstractMasterDetailComponent<T extends EntityI<Long>> 
extends 
	GenericMasterDetailComponent<T>
implements 
	SearchableI<T>
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

	protected Object onDelete(Long id) {
		return super.onDelete(id);
	}

	protected Object onDelete(Object id) {
		try {
			return super.onDelete(Long.parseLong(id.toString()));
		} catch(Exception ex) {
			log.error("Exception during 'onDelete' method call", ex);
			return getReturn();
		}
	}

	protected Object onDetail(Long id) {
		return super.onDetail(id);
	}

	protected Object onDetail(Object id) {
		try {
			return onDetail(Long.parseLong(id.toString()));
		} catch(Exception ex) {
			log.error("Exception during 'onDetail' method call", ex);
			return getReturn();
		}
	}
	
}
