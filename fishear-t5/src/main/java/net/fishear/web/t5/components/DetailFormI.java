package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;
import net.fishear.data.generic.services.ServiceI;

public interface DetailFormI<T extends EntityI<?>>
{

	void load(Object id);

	ServiceI<T> getService();

}
