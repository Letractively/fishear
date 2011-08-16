package net.fishear.web.rights.services.impl;

import net.fishear.web.rights.entities.UserInfo;
import net.fishear.web.rights.entities.UserInfoI;
import net.fishear.web.rights.services.LoginLogoutService;


public class
	FakeLoginLogoutService
implements
	LoginLogoutService
{

	@Override
	public void doLogin(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLogout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getRedirectPageAfterLogin() {
		return null;
	}

	@Override
	public Object getRedirectPageAfterLogout() {
		return null;
	}

	@Override
	public UserInfoI getUserInfo() {
		UserInfo ui = new UserInfo();
		ui.setLoginName("fake");
		ui.setFirstName("Firstn");
		ui.setLastName("Lastn");
		return ui;
	}

	@Override
	public boolean hasRole(String... roleCodes) {
		return true;
	}

	@Override
	public boolean isLoggedIn() {
		return true;
	}

	@Override
	public void checkRememberMe() {

	}

	@Override
	public void doLogin(String username, String password, boolean rememberMe) {

	}

}
