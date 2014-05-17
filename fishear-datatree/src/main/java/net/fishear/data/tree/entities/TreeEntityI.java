package net.fishear.data.tree.entities;

import net.fishear.data.generic.entities.EntityI;


public interface 
	TreeEntityI<K>
extends
	EntityI<K>
{

	TreeEntityI<K> getParent();

	void setParent(TreeEntityI<K> parent);

	String getName();

	void setName(String name);

}
