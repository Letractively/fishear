package net.fishear.web.rights.services.impl;

import net.fishear.data.generic.services.AuditServiceI;
import net.fishear.data.generic.services.CurrentStateI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentState implements CurrentStateI {

	private final AbstractLoginLogoutService abstractLoginLogoutService;
	
	public CurrentState(AbstractLoginLogoutService abstractLoginLogoutService) {
		this.abstractLoginLogoutService = abstractLoginLogoutService;
	}

	@Override
	public Object getCurrentUser() {
		if(abstractLoginLogoutService.getUserInfo() == null) {
			return null;
		}
		return abstractLoginLogoutService.getUserInfo().getLoginName();
	}

}