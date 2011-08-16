package net.fishear.data.tree.services;

import net.fishear.data.tree.entities.TreeEntityI;
import net.fishear.data.tree.entities.TreeEntityTreeI;

public abstract class 
	AbstractTreeService<K extends TreeEntityI<Long>, X extends TreeEntityTreeI<Long>>
extends
	GenericTreeService<K, X, Long>
{

}
