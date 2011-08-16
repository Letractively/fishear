package net.fishear.web.t5.base;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;

public interface ServiceSourceI<T extends EntityI<?>>
{
	public abstract ServiceI<T> getService();
}
