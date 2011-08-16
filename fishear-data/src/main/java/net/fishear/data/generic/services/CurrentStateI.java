package net.fishear.data.generic.services;

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
	public Object getCurrentUser();
	
}
