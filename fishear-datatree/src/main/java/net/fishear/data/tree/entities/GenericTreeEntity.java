package net.fishear.data.tree.entities;

import net.fishear.data.generic.entities.GenericEntity;


public abstract class 
	GenericTreeEntity<K>
extends
	GenericEntity<K>
implements
	TreeEntityI<K>
{

	TreeEntityI<K> parent;
	private String name;
	
	public TreeEntityI<K> getParent() {
		return parent;
	}

	public void setParent(TreeEntityI<K> parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
		
	}

	public void setName(String name) {
		this.name = name;
	}

}
