package net.fishear.data.generic.services;

import net.fishear.data.generic.entities.EntityI;

/** Interface provides basic information about running system and environment.
 * 
 * @author terber
 *
 */
public interface CurrentStateI
{

	/**
	 * @return information about who does current operation (identification of user currently logged into system...)
	 */
	Object getCurrentUser();
	
	
}
