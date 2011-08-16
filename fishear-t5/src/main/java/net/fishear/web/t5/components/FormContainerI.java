package net.fishear.web.t5.components;

import net.fishear.data.generic.entities.EntityI;

public interface FormContainerI<T extends EntityI<?>>
{

	void setDetailForm(AbstractForm<T> form);
	
	DetailFormI<T> getDetailForm();

	Object onDelete(String id);
	
	Object onEdit(String id);
	
}
