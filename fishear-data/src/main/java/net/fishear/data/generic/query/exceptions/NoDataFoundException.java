package net.fishear.data.generic.query.exceptions;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.exceptions.GeneralException;
import net.fishear.utils.Numbers;



public class 
	NoDataFoundException
extends
	GeneralException
{

	private static final long serialVersionUID = 1L;
	private Long id;
	private Class<? extends EntityI<?>> type;
	
	public NoDataFoundException(Class<? extends EntityI<?>> type, Long id) {
		this.setId(id);
		this.setType(type);
	}

	public void setId(Object id) {
		this.id = Numbers.tol(id, null);
	}

	public Long getId() {
		return id;
	}

	public void setType(Class<? extends EntityI<?>> type) {
		this.type = type;
	}

	public Class<? extends EntityI<?>> getType() {
		return type;
	}
}
