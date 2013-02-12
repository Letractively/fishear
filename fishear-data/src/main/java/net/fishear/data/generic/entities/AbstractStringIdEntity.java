package net.fishear.data.generic.entities;


import javax.persistence.MappedSuperclass;

/**
 * This is a superclass for all entity having ID of type "String". 
 * IDs are not generated - have to be set manually. 
 * 
 * @author terber
 * @see net.fishear.utils.EntityUtils#equals(EntityI, Object)
 * @see net.fishear.utils.EntityUtils#hashCode(EntityI)
 */
@MappedSuperclass
public class 
	AbstractStringIdEntity
extends
	GenericEntity<String>
{

}
