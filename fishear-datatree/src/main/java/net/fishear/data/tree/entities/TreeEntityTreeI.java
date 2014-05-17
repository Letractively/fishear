package net.fishear.data.tree.entities;

import net.fishear.data.generic.entities.EntityI;

/** The abstract superclass for all MASTER-SLAVE link entities in trees.
 * Master-slave entities keep for each "master" entity all their "slave" entities for all tree levels
 * @author terber
 */
public interface 
	TreeEntityTreeI<X>
extends
	EntityI<X>
{

	public X getParentId();

	public void setParentId(X parentId);
	
	public X getChildId();
	
	public void setChildId(X childId);

	public int getTreeLevel();

	public void setTreeLevel(int treeLevel);

}
