package net.fishear.data.tree.services;


import java.util.List;

import net.fishear.data.generic.services.ServiceI;
import net.fishear.data.tree.entities.TreeEntityI;


public interface 
	TreeServiceI<K extends TreeEntityI<?>>
extends
	ServiceI<K>
{

	/** returns children directly under the 'parent' (but not at at sub-children).
	 * The result must not be obtained using master-slave table, because this method is called while creating those (= master-slave) records.
	 */
	List<K> getChildren(K parent);

	/** returns all parents in order direct parent, upper, ..., root.
	 * The 'child' itself is NOT contained in returned list.
	 * The result must not be obtained using master-slave table, because this method is called while creating those (= master-slave) records.
	 */
	List<K> getParents(K child);

	/** moves 'what' under 'where', so the 'where' will be new parent for 'what'.
	 * @param what element to move
	 * @param where the new parent. It it is null, 'what' will not have the parent (it will be root element)
	 */
	void move(K what, K where);
	
	/** returns relation between 'parent' and 'child'.
	 * If the 'parent' is really the parent of 'child', returns positive number representing distance between 'child' and parent.
	 * If the 'child' is the parent of 'parent' (e.g. the relation is negated), returns negative number representing distance between 'child' and parent.
	 * If the parent is the same as child (e.g. parent == child), returns  0. 
	 * In case no relation exists between parent and child (e.g. each on is in independent branch), returns null.
	 * Never should throw exception.
	 * @param parent the expected parent
	 * @param child the expected child
	 */
	Integer getRelation(K parent, K child) ;
	
	/** returns true if 'parent' id ancestor of 'child', false otherwise.
	 */
	boolean isAncestor(K parent, K child);

	/** returns true if 'child' id a child of 'parent', false otherwise.
	 */
	boolean isChild(K parent, K child);

	/** returns true if a 'parent' has at least one child, false if the 'parent' has no children.
	 */
	boolean hasChildren(K parent);
	
	/**
	 *  returns count of children under the 'parent'.
	 * @param parent top element, uber which is counted.
	 * @param maxDepth maximum level of children to count. Value 1 means only direct children are counted. 
	 * The {@link Integer#MAX_VALUE} means all children in all sublevels is count.
	 */
	int getChildrenCount(K parent, int maxDepth);
	
	
}
