package net.fishear.data.tree.entities;

import net.fishear.data.generic.entities.GenericEntity;


public class 
	GenericTreeEntityTree<X>
extends
	GenericEntity<X>
implements
	TreeEntityTreeI<X>
{
	private int treeLevel;
	
	private X parentId;
	
	private X childId;

	@Override
	public X getChildId() {
		return childId;
	}

	@Override
	public X getParentId() {
		return parentId;
	}

	@Override
	public int getTreeLevel() {
		return treeLevel;
	}

	@Override
	public void setChildId(X childId) {
		this.childId = childId;
	}

	@Override
	public void setParentId(X parentId) {
		this.parentId = parentId;
	}

	@Override
	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}
}
