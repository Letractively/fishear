package net.fishear.data.generic.services;

import net.fishear.data.generic.entities.EntityI;

/**
 * The most common generic service implementation, which supposes Long as entity ID.
 * 
 * @author ffyxrr
 *
 * @param <K>
 */
public abstract class 
	AbstractService<K extends EntityI<Long>>
extends
	GenericService<K>
{

}
