package net.fishear.web.rights.services.impl;

import net.fishear.web.rights.services.LoginLogoutService;

public class 
	CheckLoginServiceImpl
{
// TODO: REMOVE CANDIDATE
	@SuppressWarnings("unused")
	private CheckLoginServiceImpl() {
		
	}
	
	private LoginLogoutService llSvc;

	public CheckLoginServiceImpl(LoginLogoutService llSvc) {
		this.llSvc = llSvc;
	}
	
//	@Override
	public Object beforeProcess() {
        llSvc.checkRememberMe();
		return null;
	}

//	@Override
	public void afterProcess(Object o) {

	}
}
