package net.fishear.data.generic.services;

/**
 * provides current stste of the system.
 * The state involves user currently logged in and some other values
 * 
 * @author terber
 *
 */
public interface CurrentStateSourceI {

	CurrentStateI getCurrentState();
	
}
