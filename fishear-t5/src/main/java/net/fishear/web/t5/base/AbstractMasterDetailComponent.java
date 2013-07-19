package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.entities.EntitySourceI;
import net.fishear.web.t5.internal.SearchableI;

public abstract class
	AbstractMasterDetailComponent<T extends EntityI<Long>> 
extends 
	GenericMasterDetailComponent<T>
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
