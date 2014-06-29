package net.fishear.data.generic.services;

import net.fishear.data.generic.entities.EntityI;

/**
 * single method that provides service for given class.
 * 
 * @author terber
 *
 */
public interface ServiceSourceI<T extends EntityI<?>>
{
	ServiceI<T> getService();
}
